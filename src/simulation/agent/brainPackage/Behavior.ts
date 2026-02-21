import { PolarCoordinate } from '../../common/PolarCoordinate';
import { Node } from '../../common/Node';
import { Speed } from '../../common/Speed';
import { State } from '../../common/State';

export abstract class Behavior {
  protected newPostions: PolarCoordinate[] = [];
  protected newNodes: Node[] = [];

  constructor() {}

  getNextState(newPostions: PolarCoordinate[], newNodes: Node[]): State {
    this.newPostions = newPostions;
    this.newNodes = newNodes;
    const theta = this.generateAngle();
    const speed = this.generateSpeed();
    return new State(theta, speed);
  }

  abstract generateAngle(): number;
  abstract generateSpeed(): Speed;
}
