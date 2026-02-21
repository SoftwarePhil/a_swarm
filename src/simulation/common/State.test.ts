import { State } from './State';
import { Speed } from './Speed';

describe('State', () => {
  describe('constructor', () => {
    it('should create empty state with defaults', () => {
      const s = new State();
      expect(s.getAngle()).toBe(0);
      expect(s.getSpeed()).toBe(0);
    });

    it('should create state with angle and speed', () => {
      const s = new State(90, Speed.FAST);
      expect(s.getAngle()).toBe(90);
      expect(s.getSpeed()).toBe(15);
    });
  });

  describe('setters', () => {
    it('should set angle', () => {
      const s = new State();
      s.setAngle(45);
      expect(s.getAngle()).toBe(45);
    });

    it('should set speed', () => {
      const s = new State();
      s.setSpeed(Speed.MEDIUM);
      expect(s.getSpeed()).toBe(10);
    });
  });

  describe('generateRelativePolarCoordinate', () => {
    it('should generate a relative polar coordinate', () => {
      const pc = State.generateRelativePolarCoordinate(14, 21, 2, -7, 77);
      expect(pc.getR()).toBeGreaterThan(0);
    });

    it('should handle agent directly ahead', () => {
      // other agent is directly north (j positive), current angle 0
      const pc = State.generateRelativePolarCoordinate(0, 0, 0, 10, 0);
      expect(pc.getTheta()).toBe(0);
      expect(pc.getR()).toBeCloseTo(10, 4);
    });

    it('should handle agent directly east', () => {
      // other agent is to the east (i positive), current angle 0
      const pc = State.generateRelativePolarCoordinate(0, 0, 10, 0, 0);
      expect(pc.getTheta()).toBe(90);
      expect(pc.getR()).toBeCloseTo(10, 4);
    });
  });
});
