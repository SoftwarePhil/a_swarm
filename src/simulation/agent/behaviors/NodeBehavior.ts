import { Behavior } from '../brainPackage/Behavior';
import { Node } from '../../common/Node';
import { Speed } from '../../common/Speed';

export class NodeBehavior extends Behavior {
  generateAngle(): number {
    return this.pickBestNodeAngle();
  }

  generateSpeed(): Speed {
    return Speed.VERYSLOW;
  }

  pickBestNodeAngle(): number {
    const goodNodes: Node[] = [];
    for (const n of this.newNodes) {
      if (
        n.attribute !== 0 &&
        !n.boundary &&
        !n.collision &&
        n.object === 'grass' &&
        !(n.theta > 60 && n.theta < 300)
      ) {
        goodNodes.push(n);
      }
    }

    // Fisher-Yates shuffle
    for (let i = goodNodes.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [goodNodes[i], goodNodes[j]] = [goodNodes[j], goodNodes[i]];
    }

    goodNodes.sort((a, b) => a.compareTo(b));

    if (goodNodes.length === 0) {
      return 0;
    } else {
      let bestNodeAngle = 420;

      for (const n of goodNodes) {
        if (n.theta === 0) {
          bestNodeAngle = 0;
        }
        if (n.theta < 10 && bestNodeAngle !== 0) {
          bestNodeAngle = n.theta;
        }
      }

      if (bestNodeAngle === 420) {
        return goodNodes[goodNodes.length - 1].theta;
      } else {
        return bestNodeAngle;
      }
    }
  }
}
