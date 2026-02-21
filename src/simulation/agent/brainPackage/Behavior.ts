import { PolarCoordinate } from '../../common/PolarCoordinate';
import { Node } from '../../common/Node';
import { Speed } from '../../common/Speed';
import { State } from '../../common/State';

export abstract class Behavior {
  protected newPositions: PolarCoordinate[] = [];
  protected newNodes: Node[] = [];

  constructor() {}

  getNextState(newPositions: PolarCoordinate[], newNodes: Node[]): State {
    this.newPositions = newPositions;
    this.newNodes = newNodes;
    const theta = this.generateAngle();
    const speed = this.generateSpeed();
    return new State(theta, speed);
  }

  abstract generateAngle(): number;
  abstract generateSpeed(): Speed;
}
