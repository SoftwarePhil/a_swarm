import { Behavior } from './Behavior';
import { Node } from '../../common/Node';
import { PolarCoordinate } from '../../common/PolarCoordinate';
import { State } from '../../common/State';

export class BehaviorManager {
  private swarmBehavior: Behavior;
  private crashBehavior: Behavior;
  private currentBehavior: Behavior;
  protected nodeList: Node[] = [];
  private newPostions: PolarCoordinate[] = [];

  constructor(swarmBehavior: Behavior, crashBehavior: Behavior) {
    this.swarmBehavior = swarmBehavior;
    this.crashBehavior = crashBehavior;
    this.currentBehavior = swarmBehavior;
    this.newPostions = [];
  }

  setSwarmState(): void {
    this.currentBehavior = this.swarmBehavior;
  }

  setCrashedState(): void {
    this.currentBehavior = this.crashBehavior;
  }

  getCurrentState(): State {
    return this.currentBehavior.getNextState(this.newPostions, this.nodeList);
  }

  updateDeltaPostions(polarCoordinates: PolarCoordinate[]): void {
    this.newPostions = polarCoordinates;
  }

  updateNodeList(nodeList: Node[]): void {
    this.nodeList = nodeList;
  }
}
