export enum Speed {
  FAST = 15,
  MEDIUM = 10,
  SLOW = 2,
  VERYSLOW = 1,
  STOPPED = 0,
}

export function getSpeedValue(speed: Speed): number {
  return speed as number;
}
