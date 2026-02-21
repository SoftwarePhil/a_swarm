import { Behavior } from '../brainPackage/Behavior';
import { PolarCoordinate } from '../../common/PolarCoordinate';
import { AVector } from '../../common/AVector';
import { Speed } from '../../common/Speed';

export class SwarmBehavior extends Behavior {
  private limit: boolean;
  private static l: number = 0;
  private static alpha: number = 0;
  private x: number = 0;

  constructor(limit: boolean, l: number) {
    super();
    this.limit = limit;
    SwarmBehavior.l = l;
    SwarmBehavior.alpha = 1 - SwarmBehavior.l;
    this.setX();
  }

  setX(): void {
    this.x = Math.sqrt(SwarmBehavior.l / SwarmBehavior.alpha);
  }

  generateAngle(): number {
    let angle = this.curveCalculation(this.shiftAngles(0));
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

  curveCalculation(newPositions: PolarCoordinate[]): number {
    const vector: AVector[] = [];
    const listA: PolarCoordinate[] = [];
    const listR: PolarCoordinate[] = [];
    const l = SwarmBehavior.l;
    const alpha = SwarmBehavior.alpha;
    const x = this.x;

    for (const p of newPositions) {
      // Java: (p.getTheta() > 270 || p.getTheta() < 90 && limit) || !limit
      // && has higher precedence than ||, so: (p.getTheta() > 270 || (p.getTheta() < 90 && limit)) || !limit
      if ((p.getTheta() > 270 || (p.getTheta() < 90 && this.limit)) || !this.limit) {
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
