import { Behavior } from './Behavior';
import { Node } from '../../common/Node';
import { PolarCoordinate } from '../../common/PolarCoordinate';
import { State } from '../../common/State';

export class BehaviorManager {
  private swarmBehavior: Behavior;
  private crashBehavior: Behavior;
  private currentBehavior: Behavior;
  protected nodeList: Node[] = [];
  private newPositions: PolarCoordinate[] = [];

  constructor(swarmBehavior: Behavior, crashBehavior: Behavior) {
    this.swarmBehavior = swarmBehavior;
    this.crashBehavior = crashBehavior;
    this.currentBehavior = swarmBehavior;
    this.newPositions = [];
  }

  setSwarmState(): void {
    this.currentBehavior = this.swarmBehavior;
  }

  setCrashedState(): void {
    this.currentBehavior = this.crashBehavior;
  }

  getCurrentState(): State {
    return this.currentBehavior.getNextState(this.newPositions, this.nodeList);
  }

  updateDeltaPositions(polarCoordinates: PolarCoordinate[]): void {
    this.newPositions = polarCoordinates;
  }

  updateNodeList(nodeList: Node[]): void {
    this.nodeList = nodeList;
  }
}
