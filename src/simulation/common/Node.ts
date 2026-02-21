let nodeCounter = 0;

export class Node {
  public nodeId: number;
  public r: number = 0;
  public theta: number = 0;
  public object: string = '';
  public attribute: number = 0;
  public collision: boolean = false;
  public boundary: boolean = false;
  public x: number = 0;
  public y: number = 0;

  constructor() {
    this.nodeId = nodeCounter;
    nodeCounter++;
  }

  setBoundaryTrue(): void {
    this.boundary = true;
  }

  setCollisionTrue(): void {
    this.collision = true;
  }

  setRTheta(r: number, theta: number): void {
    this.r = r;
    this.theta = theta;
  }

  defaultNode(): void {
    this.r = 0;
    this.theta = 0;
    this.object = 'defaultNode';
    this.attribute = 0;
  }

  toString(): string {
    return 'Node ID : ' + this.nodeId + ' Object : ' + this.object + ' x : ' + this.x + ' y : ' + this.y;
  }

  compareTo(b: Node): number {
    if (this.attribute < b.attribute) {
      return -1;
    }
    if (this.attribute === b.attribute) {
      return 0;
    }
    return 1;
  }
}
