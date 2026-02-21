import { Speed } from './Speed';
import { PolarCoordinate } from './PolarCoordinate';

export class State {
  private angle: number = 0;
  private speed: Speed = Speed.STOPPED;

  constructor();
  constructor(angle: number, speed: Speed);
  constructor(angle?: number, speed?: Speed) {
    if (angle !== undefined && speed !== undefined) {
      this.angle = angle;
      this.speed = speed;
    }
  }

  getAngle(): number {
    return this.angle;
  }

  setAngle(angle: number): void {
    this.angle = angle;
  }

  getSpeed(): number {
    return this.speed as number;
  }

  toString(): string {
    return 'Angle : ' + this.getAngle() + '\n' + 'Speed : ' + this.getSpeed();
  }

  setSpeed(s: Speed): void {
    this.speed = s;
  }

  // where x,y is the robot and x2,y2 is the other robot
  static generateRelativePolarCoordinate(
    x: number, y: number,
    x2: number, y2: number,
    currentAngle: number
  ): PolarCoordinate {
    const i = x2 - x;
    const j = y2 - y;

    const distance = Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2));
    let angle = Math.trunc((Math.acos(j / distance) * 180) / Math.PI);

    if (i < 0 && j === 0) {
      angle = 360 - angle;
    } else if (i < 0 && j > 0) {
      angle = 360 - angle;
    } else if (i < 0 && j < 0) {
      angle = 360 - angle;
    }

    if (angle >= currentAngle) {
      angle = angle - currentAngle;
    } else {
      angle = 360 - (currentAngle - angle);
    }

    return new PolarCoordinate(distance, angle);
  }
}
