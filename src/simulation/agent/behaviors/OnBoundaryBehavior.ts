import { Behavior } from '../brainPackage/Behavior';
import { Speed } from '../../common/Speed';

export class OnBoundaryBehavior extends Behavior {
  generateSpeed(): number {
    return Speed.SLOW;
  }

  generateAngle(): number {
    return 21;
  }
}
