import { PolarCoordinate } from '../../common/PolarCoordinate';

export class RandomData {
  generateSpeed(): number {
    return Math.random() * 3;
  }

  generateAngle(): number {
    return Math.floor(Math.random() * 360);
  }

  getRandomPolarCoordinate(): PolarCoordinate {
    return new PolarCoordinate(this.generateSpeed(), this.generateAngle());
  }

  getArray(): PolarCoordinate[] {
    const size = Math.floor(Math.random() * 31);
    const p: PolarCoordinate[] = [];
    for (let i = 0; i < size; i++) {
      p.push(this.getRandomPolarCoordinate());
    }
    return p;
  }
}
