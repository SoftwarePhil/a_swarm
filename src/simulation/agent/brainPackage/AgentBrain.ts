import { BehaviorManager } from './BehaviorManager';
import { Node } from '../../common/Node';
import { PolarCoordinate } from '../../common/PolarCoordinate';
import { State } from '../../common/State';

export class AgentBrain {
  private behaviorManager: BehaviorManager;
  private isCrashed: boolean = false;
  private newNodes: Node[] = [];
  private newPositions: PolarCoordinate[] = [];

  constructor(behaviorManager: BehaviorManager) {
    this.behaviorManager = behaviorManager;
  }

  writeDataToRobot(p: PolarCoordinate[], n: Node[]): void {
    this.newPositions = p;
    this.newNodes = n;
  }

  calculateNextRobotState(): State {
    this.behaviorManager.updateDeltaPositions(this.newPositions);
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
    if (this.newPositions.length > 0) {
      if (this.newPositions[0].getName() === 'CRASH') {
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
