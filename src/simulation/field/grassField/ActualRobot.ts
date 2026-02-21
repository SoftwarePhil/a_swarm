import { AgentBrain } from '../../agent/brainPackage/AgentBrain';
import { BehaviorManager } from '../../agent/brainPackage/BehaviorManager';
import { Node } from '../../common/Node';
import { PolarCoordinate } from '../../common/PolarCoordinate';
import { State } from '../../common/State';

let robotCount = 0;

export class ActualRobot {
  public state: State = new State();
  public xPos: number = 0;
  public yPos: number = 0;
  public absoluteXPos: number = 0;
  public absoluteYPos: number = 0;
  public newAngle: number = 0;
  public newDistance: number = 0;
  public robotID: string = '';
  private absoluteAngle: number = 0;
  private relativeAngle: number = 0;
  public lastPostionX: number = 0;
  public lastPostionY: number = 0;

  private agentBrain: AgentBrain;

  constructor(behaviorManager: BehaviorManager) {
    this.robotID = 'Swarm' + robotCount;
    robotCount++;
    this.agentBrain = new AgentBrain(behaviorManager);
  }

  getRelativeRobotAngle(): number {
    return this.absoluteAngle;
  }

  updateState(): void {
    this.relativeAngle = this.state.getAngle();
    this.newDistance = this.state.getSpeed();
  }

  writeDataToRobot(p: PolarCoordinate[], n: Node[]): void {
    this.agentBrain.writeDataToRobot(p, n);
  }

  move(): void {
    this.state = this.agentBrain.calcauteNextRobotState();
    this.updateState();
    this.getRelativeValues();
    this.updateXPos();
    this.updateYPos();
  }

  updateXPos(): void {
    this.lastPostionX = this.absoluteXPos;
    this.xPos = this.newDistance * Math.cos((this.newAngle * Math.PI) / 180);

    if (this.xPos < 0.025 && this.xPos > -0.025) {
      this.xPos = 0;
    }

    this.absoluteXPos = this.absoluteXPos + this.xPos;
  }

  updateYPos(): void {
    this.lastPostionY = this.absoluteYPos;
    this.yPos = this.newDistance * Math.sin((this.newAngle * Math.PI) / 180);

    if (this.yPos < 0.05 && this.yPos > -0.05) {
      this.yPos = 0;
    }

    this.absoluteYPos = this.absoluteYPos + this.yPos;
  }

  getRelativeValues(): void {
    this.absoluteAngle = this.relativeAngle + this.absoluteAngle;

    if (this.absoluteAngle > 360) {
      this.absoluteAngle = this.absoluteAngle - 360;
    }

    this.newDistance = this.state.getSpeed();

    if (this.absoluteAngle < 90) {
      this.newAngle = 90 - this.absoluteAngle;
    } else if (this.absoluteAngle < 180) {
      this.newAngle = 360 - (this.absoluteAngle - 90);
    } else if (this.absoluteAngle < 270) {
      this.newAngle = 180 + (90 - (this.absoluteAngle - 180));
    } else if (this.absoluteAngle <= 360) {
      this.newAngle = 90 + (90 - (this.absoluteAngle - 270));
    }
  }

  hasCrashed(): void {
    this.agentBrain.hasCrashed();
  }

  notCrashed(): void {
    this.agentBrain.notCrashed();
  }

  getCrashed(): boolean {
    return this.agentBrain.getCrashed();
  }

  calcauteNextRobotState(): State {
    return this.agentBrain.calcauteNextRobotState();
  }

  getRobotName(): string {
    return this.robotID;
  }
}
