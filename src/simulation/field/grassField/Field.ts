import { Behavior } from '../../agent/brainPackage/Behavior';
import { BehaviorManager } from '../../agent/brainPackage/BehaviorManager';
import { Node } from '../../common/Node';
import { PolarCoordinate } from '../../common/PolarCoordinate';
import { State } from '../../common/State';
import { ActualRobot } from './ActualRobot';
import { GrassGrower } from './GrassGrower';
import { GrassNode } from './GrassNode';

const GROWTIME = 10000;
const GROW = false;
const GRASS_HEIGHT = 10;

export class Field {
  private xSize: number;
  private ySize: number;
  private grass: Map<string, GrassNode> = new Map();
  private agents: ActualRobot[] = [];
  private grassGrower: GrassGrower;
  private count: number = 1;
  private simulationSteps: number = 0;

  constructor(
    numOfAgents: number,
    xSize: number,
    ySize: number,
    b1: Behavior,
    b2: Behavior
  ) {
    this.xSize = xSize;
    this.ySize = ySize;

    for (let i = 1; i < xSize; i++) {
      for (let j = 1; j < ySize; j++) {
        this.grass.set(i + ' ' + j, new GrassNode(i, j, GRASS_HEIGHT));
      }
    }

    // top row boundary
    for (let i = 1; i < xSize; i++) {
      const n = this.grass.get(i + ' ' + 1);
      if (n) { n.setBoundaryTrue(); n.setBoundaryColor(); }
      const n2 = this.grass.get(i + ' ' + 2);
      if (n2) { n2.setBoundaryTrue(); n2.setBoundaryColor(); }
    }

    // bottom row boundary
    for (let i = 1; i < xSize; i++) {
      const n = this.grass.get(i + ' ' + (Math.trunc(ySize) - 1));
      if (n) { n.setBoundaryTrue(); n.setBoundaryColor(); }
      const n2 = this.grass.get(i + ' ' + (Math.trunc(ySize) - 2));
      if (n2) { n2.setBoundaryTrue(); n2.setBoundaryColor(); }
    }

    // left column boundary
    for (let i = 1; i < ySize; i++) {
      const n = this.grass.get(1 + ' ' + i);
      if (n) { n.setBoundaryTrue(); n.setBoundaryColor(); }
      const n2 = this.grass.get(2 + ' ' + i);
      if (n2) { n2.setBoundaryTrue(); n2.setBoundaryColor(); }
    }

    // right column boundary
    for (let i = 1; i < ySize; i++) {
      const n = this.grass.get((Math.trunc(xSize) - 1) + ' ' + i);
      if (n) { n.setBoundaryTrue(); n.setBoundaryColor(); }
      const n2 = this.grass.get((Math.trunc(xSize) - 2) + ' ' + i);
      if (n2) { n2.setBoundaryTrue(); n2.setBoundaryColor(); }
    }

    const grassList: GrassNode[] = [];
    for (const node of this.grass.values()) {
      grassList.push(node);
    }

    this.grassGrower = new GrassGrower(grassList, GROWTIME, GROW, Math.trunc(xSize), Math.trunc(ySize));

    for (let i = 0; i < numOfAgents; i++) {
      this.addRobot(new ActualRobot(new BehaviorManager(b1, b2)));
    }
  }

  addRobot(r: ActualRobot): void {
    this.agents.push(r);
    r.absoluteXPos = this.xSize - this.count * 5;
    r.absoluteYPos = (this.ySize - this.ySize / 7) - this.count * 5;
    r.lastPositionX = r.absoluteXPos;
    r.lastPositionY = r.absoluteYPos;
    this.count++;
  }

  step(): void {
    // Write sensor data to all agents first (sense → decide → act)
    if (this.agents.length > 0) {
      for (const a of this.agents) {
        a.writeDataToRobot(
          this.getListOfRelativePolarCoordinates(a),
          this.getListOfRelativeNodes(a)
        );
      }
    }

    for (const a of this.agents) {
      a.move();
    }

    for (const a of this.agents) {
      a.notCrashed();
      if (a.absoluteYPos > this.ySize) a.hasCrashed();
      if (a.absoluteYPos < 0) a.hasCrashed();
      if (a.absoluteXPos > this.xSize) a.hasCrashed();
      if (a.absoluteXPos < 0) a.hasCrashed();

      let keys: string[] = [];
      const pos = Math.round(a.absoluteXPos) + ' ' + Math.round(a.absoluteYPos);
      const pos2 = Math.round(a.lastPositionX) + ' ' + Math.round(a.lastPositionY);

      const hasPos = this.grass.has(pos);
      const hasPos2 = this.grass.has(pos2);

      if (hasPos && hasPos2) {
        const grassNode1 = this.grass.get(pos)!;
        const grassNode2 = this.grass.get(pos2)!;
        keys = this.grassGrower.getCutPath(grassNode2, grassNode1);
      }

      for (const str of keys) {
        if (this.grass.has(str)) {
          this.grass.get(str)!.cutGrass();
        }
      }
    }

    this.simulationSteps++;
  }

  getListOfRelativePolarCoordinates(robot: ActualRobot): PolarCoordinate[] {
    const pL: PolarCoordinate[] = [];

    if (robot.getCrashed()) {
      const crash = new PolarCoordinate(0, 0);
      crash.setName('CRASH');
      pL.push(crash);
    }

    for (const a of this.agents) {
      if (a.getRobotName() === robot.getRobotName()) {
        continue;
      }
      pL.push(State.generateRelativePolarCoordinate(
        robot.absoluteXPos, robot.absoluteYPos,
        a.absoluteXPos, a.absoluteYPos,
        robot.getRelativeRobotAngle()
      ));
      const last = pL.length - 1;
      pL[last].setName(a.getRobotName());
      if (a.getCrashed()) {
        pL[last].setAttractionFalse();
        pL[last].setName('C' + a.getRobotName());
      }
    }

    return pL;
  }

  getListOfRelativeNodes(robot: ActualRobot): Node[] {
    const nodes: Node[] = [];
    const x = Math.round(robot.absoluteXPos);
    const y = Math.round(robot.absoluteYPos);
    const key = x + ' ' + y;

    if (this.grass.has(key)) {
      const closestNode = this.grass.get(key)!;
      const tempNodes = this.grassGrower.getNearestNodes(closestNode);

      for (const g of tempNodes) {
        const tempNode = this.grass.get(g.x + ' ' + g.y);
        if (tempNode) {
          const tempPC = State.generateRelativePolarCoordinate(
            robot.absoluteXPos, robot.absoluteYPos,
            g.x, g.y,
            robot.getRelativeRobotAngle()
          );
          tempNode.setRTheta(tempPC.getR(), tempPC.getTheta());
          nodes.push(tempNode);
        }
      }
    }

    return nodes;
  }

  getAgents(): ActualRobot[] {
    return this.agents;
  }

  getNumberOfSteps(): number {
    return this.simulationSteps;
  }

  getPercentGrassCut(): number {
    return this.grassGrower.getPercentOfGrassCut();
  }

  getFieldSize(): { width: number; height: number } {
    return { width: this.xSize, height: this.ySize };
  }

  getGrassData(): Array<{ x: number; y: number; height: number; boundary: boolean }> {
    const data: Array<{ x: number; y: number; height: number; boundary: boolean }> = [];
    for (const node of this.grass.values()) {
      data.push({ x: node.x, y: node.y, height: node.getGrassHeight(), boundary: node.boundary });
    }
    return data;
  }
}
