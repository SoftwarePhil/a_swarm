let count = 0;

export class PolarCoordinate {
  private id: number = 0;
  private name: string = '';
  private theta: number;
  private r: number;
  private attraction: boolean = true;

  constructor();
  constructor(r: number, theta: number);
  constructor(r?: number, theta?: number) {
    if (r !== undefined && theta !== undefined) {
      count++;
      this.id = count;
      this.r = r;
      this.theta = theta;
    } else {
      this.r = 0;
      this.theta = 0;
    }
  }

  getR(): number {
    return this.r;
  }

  getTheta(): number {
    return this.theta;
  }

  setR(r: number): void {
    this.r = r;
  }

  setTheta(theta: number): void {
    this.theta = theta;
  }

  setName(name: string): void {
    this.name = name;
  }

  getName(): string {
    return this.name;
  }

  setAttractionFalse(): void {
    this.attraction = false;
  }

  getAttraction(): boolean {
    return this.attraction;
  }

  getId(): number {
    return this.id;
  }

  setId(id: number): void {
    this.id = id;
  }

  toString(): string {
    return 'r : ' + this.r + ' ' + 'theta: ' + this.theta;
  }
}
