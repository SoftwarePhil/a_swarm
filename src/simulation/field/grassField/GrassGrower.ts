import * as fs from 'fs';
import * as path from 'path';
import { GrassNode } from './GrassNode';
import { AVector } from '../../common/AVector';
import { Node } from '../../common/Node';
import { HistoryNode } from '../../common/HistoryNode';

export class GrassGrower {
  private grass: GrassNode[];
  private growTime: number;
  private grow: boolean;
  private xSize: number;
  private ySize: number;
  private history: HistoryNode[];

  constructor(
    grass: GrassNode[],
    growTime: number,
    grow: boolean,
    xSize: number,
    ySize: number
  ) {
    this.grass = grass;
    this.growTime = growTime;
    this.grow = grow;
    this.xSize = xSize;
    this.ySize = ySize;
    this.history = [];
  }

  getPercentOfGrassCut(): number {
    const amountOfGrass = this.grass.length;
    let amountOfGrassCut = 0;
    for (const node of this.grass) {
      if (node.getGrassHeight() === 0) {
        amountOfGrassCut++;
      }
    }
    return 100 * (amountOfGrassCut / amountOfGrass);
  }

  growAllGrass(): void {
    if (!this.grow) return;
    for (const node of this.grass) {
      node.growGrass();
    }
  }

  getCutPath(start: GrassNode, end: GrassNode): string[] {
    const nodes: string[] = [];
    const vector = new AVector(end.x - start.x, end.y - start.y);

    const dPlus = 1 / vector.length();
    let pointX = start.x;
    let pointY = start.y;
    nodes.push(Math.round(pointX) + ' ' + Math.round(pointY));

    for (let j = 0; j < 1.0; j = j + dPlus) {
      pointX = pointX + dPlus * vector.i;
      pointY = pointY + dPlus * vector.j;
      nodes.push(Math.round(pointX) + ' ' + Math.round(pointY));
    }

    return nodes;
  }

  getNearestNodes(center: Node): Node[] {
    const nodes: Node[] = [];
    const x = center.x;
    const y = center.y;

    for (let i = -1; i < 2; i++) {
      for (let j = -1; j < 2; j++) {
        if (x + j === x && y + i === y) {
          // skip center
        } else {
          const n = new Node();
          n.x = x + j;
          n.y = y + i;
          if (n.x < 1 || n.y < 1 || n.x > this.xSize - 1 || n.y > this.ySize - 1) {
            // out of bounds, skip
          } else {
            nodes.push(n);
          }
        }
      }
    }
    return nodes;
  }

  recordHistory(step: number): void {
    const percentCut = this.getPercentOfGrassCut();
    this.history.push(new HistoryNode(step, percentCut));
    console.log(step);

    if (percentCut >= 99.99) {
      const now = new Date();
      const pad = (n: number) => String(n).padStart(2, '0');
      const dateStr =
        `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())}` +
        `_${pad(now.getHours())}_${pad(now.getMinutes())}_${pad(now.getSeconds())}`;

      const dir = 'simulation';
      if (!fs.existsSync(dir)) {
        fs.mkdirSync(dir, { recursive: true });
      }
      const filePath = path.join(dir, `swarm_${dateStr}.csv`);

      const lines: string[] = ['step,percent cut'];
      for (const point of this.history) {
        lines.push(`${point.step},${point.grassCut}`);
      }
      fs.writeFileSync(filePath, lines.join('\n') + '\n');

      console.log('over --- shutting down');
    }
  }
}
