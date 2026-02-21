import { Behavior } from '../brainPackage/Behavior';
import { Speed } from '../../common/Speed';

export class MetaBehavior extends Behavior {
  private behaviors: Behavior[] = [];

  constructor(b: Behavior[]) {
    super();
    this.behaviors.push(...b);
  }

  generateAngle(): number {
    let metaAngle = 0;
    for (const b of this.behaviors) {
      metaAngle += b.getNextState(this.newPostions, this.newNodes).getAngle();
    }
    return Math.trunc(metaAngle / this.behaviors.length);
  }

  generateSpeed(): Speed {
    return Speed.VERYSLOW;
  }
}
