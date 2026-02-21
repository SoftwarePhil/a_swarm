import { Node } from '../../common/Node';

const MAX_GRASS_HEIGHT = 10;
const RED = 149;
const GREEN = 250;
const BLUE = 235;
const GROWTH_INTERVAL_RED = Math.floor(RED / MAX_GRASS_HEIGHT);
const GROWTH_INTERVAL_GREEN = Math.floor(GREEN / MAX_GRASS_HEIGHT);

export class GrassNode extends Node {
  static readonly maxGrassHeight: number = MAX_GRASS_HEIGHT;

  constructor(x: number, y: number, grassHeight: number) {
    super();
    this.object = 'grass';
    this.x = x;
    this.y = y;
    this.attribute = grassHeight;
  }

  growGrass(): void {
    if (this.attribute < MAX_GRASS_HEIGHT) {
      this.attribute++;
    }
  }

  cutGrass(): void {
    this.attribute = 0;
  }

  getGrassHeight(): number {
    return this.attribute;
  }

  toString(): string {
    return this.x + ' ' + this.y;
  }

  // no-op: colour is not used in headless simulation
  setBoundaryColor(): void {}
}

export { RED, GREEN, BLUE, GROWTH_INTERVAL_RED, GROWTH_INTERVAL_GREEN };
