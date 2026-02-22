import { Behavior } from '../brainPackage/Behavior';
import { NodeBehavior } from './NodeBehavior';
import { PolarCoordinate } from '../../common/PolarCoordinate';
import { AVector } from '../../common/AVector';
import { Speed } from '../../common/Speed';

export class SwarmBehaviorNode extends Behavior {
  private angleBehavior: NodeBehavior = new NodeBehavior();
  private limit: boolean;

  private static readonly l: number = 0.999;
  private static readonly alpha: number = 1 - SwarmBehaviorNode.l;
  private static readonly SCALAR: number = 1.0;
  private static readonly attractionDistanceScalar: number = 1.5;

  private x: number = 0;

  constructor(limit: boolean) {
    super();
    this.setX();
    this.limit = limit;
  }

  setX(): void {
    this.x = Math.sqrt(SwarmBehaviorNode.l / SwarmBehaviorNode.alpha);
  }

  generateAngle(): number {
    const swarmAngle = this.curveCalculation(this.shiftAngles(0));
    this.angleBehavior.getNextState(this.newPositions, this.newNodes);
    const nodeAngle = this.angleBehavior.generateAngle();

    if (swarmAngle === 0 || Number.isNaN(swarmAngle)) {
      return nodeAngle;
    }

    const nodeVector = new AVector(
      SwarmBehaviorNode.SCALAR * Math.sin((nodeAngle * Math.PI) / 180),
      SwarmBehaviorNode.SCALAR * Math.cos((nodeAngle * Math.PI) / 180)
    );
    const swarmVector = new AVector(
      Math.sin((swarmAngle * Math.PI) / 180),
      Math.cos((swarmAngle * Math.PI) / 180)
    );
    const vectorSum = AVector.addVector(nodeVector, swarmVector);

    let angle = AVector.getVectorAngle(vectorSum);

    if (angle < 270 && angle > 90 && this.limit) {
      if (angle > 180) {
        angle = 270;
      } else {
        angle = 90;
      }
    }
    return angle;
  }

  generateSpeed(): Speed {
    return Speed.VERYSLOW;
  }

  shiftAngles(shift: number): PolarCoordinate[] {
    const p: PolarCoordinate[] = [];
    for (const deltaPosition of this.newPositions) {
      p.push(deltaPosition);
      let angle = p[p.length - 1].getTheta();
      const newAngle = angle - shift;
      if (newAngle < 0) {
        angle = 360 - Math.abs(newAngle);
      }
      p[p.length - 1].setTheta(angle);
    }
    return p;
  }

  private curveCalculation(newPositions: PolarCoordinate[]): number {
    const vector: AVector[] = [];
    const listA: PolarCoordinate[] = [];
    const listR: PolarCoordinate[] = [];
    const l = SwarmBehaviorNode.l;
    const alpha = SwarmBehaviorNode.alpha;
    const x = this.x;
    const attractionDistanceScalar = SwarmBehaviorNode.attractionDistanceScalar;

    for (const p of newPositions) {
      // Java: (((p.getTheta() > 270 || p.getTheta() < 90) && (p.getR() < x*attractionDistanceScalar)) && limit) || !limit
      if (
        ((((p.getTheta() > 270 || p.getTheta() < 90) && p.getR() < x * attractionDistanceScalar)) && this.limit) ||
        !this.limit
      ) {
        if (p.getR() > x && p.getAttraction()) {
          listA.push(p);
        } else if (p.getAttraction()) {
          listR.push(p);
        }
      }
    }

    for (const p of listA) {
      const temp = p.getR() - Math.sqrt(x);
      const a = (alpha / l) * Math.pow(temp, 2);
      vector.push(new AVector(
        a * Math.sin((p.getTheta() * Math.PI) / 180),
        a * Math.cos((p.getTheta() * Math.PI) / 180)
      ));
    }

    for (const p of listR) {
      const temp = -l / Math.pow(p.getR(), 2);
      const r = temp + alpha;
      vector.push(new AVector(
        r * Math.sin((p.getTheta() * Math.PI) / 180),
        r * Math.cos((p.getTheta() * Math.PI) / 180)
      ));
    }

    return this.getVectorAngle(vector);
  }

  private getVectorAngle(vector: AVector[]): number {
    let iSum = 0;
    let jSum = 0;
    for (const v of vector) {
      iSum += v.i;
      jSum += v.j;
    }
    return AVector.getVectorAngle(new AVector(iSum, jSum));
  }
}
