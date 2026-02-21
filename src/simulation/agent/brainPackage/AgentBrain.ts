import { BehaviorManager } from './BehaviorManager';
import { Node } from '../../common/Node';
import { PolarCoordinate } from '../../common/PolarCoordinate';
import { State } from '../../common/State';

export class AgentBrain {
  private behaviorManager: BehaviorManager;
  private isCrashed: boolean = false;
  private newNodes: Node[] = [];
  private newPostions: PolarCoordinate[] = [];

  constructor(behaviorManager: BehaviorManager) {
    this.behaviorManager = behaviorManager;
  }

  writeDataToRobot(p: PolarCoordinate[], n: Node[]): void {
    this.newPostions = p;
    this.newNodes = n;
  }

  calcauteNextRobotState(): State {
    this.behaviorManager.updateDeltaPostions(this.newPostions);
    this.behaviorManager.updateNodeList(this.newNodes);

    this.isCrashed = this.checkIfCrashed();

    if (this.isCrashed) {
      this.behaviorManager.setCrashedState();
    } else {
      this.behaviorManager.setSwarmState();
    }

    return this.behaviorManager.getCurrentState();
  }

  private checkIfCrashed(): boolean {
    if (this.newPostions.length > 0) {
      if (this.newPostions[0].getName() === 'CRASH') {
        return true;
      }
    }
    return false;
  }

  hasCrashed(): void {
    this.isCrashed = true;
  }

  notCrashed(): void {
    this.isCrashed = false;
  }

  getCrashed(): boolean {
    return this.isCrashed;
  }
}
