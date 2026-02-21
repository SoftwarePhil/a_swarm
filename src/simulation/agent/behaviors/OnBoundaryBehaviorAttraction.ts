import { Behavior } from '../brainPackage/Behavior';
import { PolarCoordinate } from '../../common/PolarCoordinate';
import { AVector } from '../../common/AVector';
import { Speed } from '../../common/Speed';

export class OnBoundaryBehaviorAttraction extends Behavior {
  public others: PolarCoordinate[] = [];
  private l: number = 0.999;
  private alpha: number = 1 - 0.999;
  private x: number = Math.sqrt(0.999 / (1 - 0.999));

  generateSpeed(): Speed {
    return Speed.VERYSLOW;
  }

  generateAngle(): number {
    return this.getBestAngle();
  }

  private getAttractionAngle(): number {
    const vector: AVector[] = [];
    const temp: PolarCoordinate[] = [];

    for (const d of this.newPositions) {
      if (d.getAttraction()) {
        temp.push(d);
      }
    }
    this.others = temp;

    for (const p of temp) {
      const atemp = p.getR() - Math.sqrt(this.x);
      const a = (this.alpha / this.l) * Math.pow(atemp, 2);
      vector.push(new AVector(
        a * Math.sin((p.getTheta() * Math.PI) / 180),
        a * Math.cos((p.getTheta() * Math.PI) / 180)
      ));
    }

    return this.getVectorAngle(vector);
  }

  private getBestAngle(): number {
    return this.getAttractionAngle();
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
