export class AVector {
  public i: number;
  public j: number;

  constructor(i: number, j: number) {
    this.i = i;
    this.j = j;
  }

  length(): number {
    return Math.sqrt(Math.pow(this.i, 2) + Math.pow(this.j, 2));
  }

  static getVectorAngle(vector: AVector): number {
    const i = vector.i;
    const j = vector.j;

    const distance = Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2));
    let angle = Math.trunc((Math.acos(j / distance) * 180) / Math.PI);

    if (i < 0 && j === 0) {
      angle = 360 - angle;
    } else if (i < 0 && j > 0) {
      angle = 360 - angle;
    } else if (i < 0 && j < 0) {
      angle = 360 - angle;
    }

    if (angle === 360) {
      angle = 0;
    }
    return angle;
  }

  static addVector(a: AVector, b: AVector): AVector {
    return new AVector(a.i + b.i, a.j + b.j);
  }
}
