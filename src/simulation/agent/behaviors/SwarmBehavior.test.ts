import { SwarmBehavior } from './SwarmBehavior';
import { PolarCoordinate } from '../../common/PolarCoordinate';

describe('SwarmBehavior', () => {
  describe('generateSpeed', () => {
    it('should return VERYSLOW (1)', () => {
      const sb = new SwarmBehavior(false, 0.999);
      expect(sb.generateSpeed()).toBe(1);
    });
  });

  describe('curveCalculation', () => {
    it('should return a number for empty list', () => {
      const sb = new SwarmBehavior(false, 0.999);
      // Provide newPositions via getNextState
      sb.getNextState([], []);
      const result = sb.curveCalculation([]);
      expect(typeof result).toBe('number');
    });

    it('should compute angle for attraction positions', () => {
      const sb = new SwarmBehavior(false, 0.999);
      const pc = new PolarCoordinate(50, 45);
      sb.getNextState([pc], []);
      const result = sb.curveCalculation([pc]);
      expect(result).toBeGreaterThanOrEqual(0);
      expect(result).toBeLessThan(360);
    });

    it('should compute angle for repulsion positions', () => {
      const sb = new SwarmBehavior(false, 0.999);
      const pc = new PolarCoordinate(1, 45);
      sb.getNextState([pc], []);
      const result = sb.curveCalculation([pc]);
      expect(result).toBeGreaterThanOrEqual(0);
      expect(result).toBeLessThan(360);
    });
  });

  describe('generateAngle', () => {
    it('should return an angle between 0 and 359', () => {
      const sb = new SwarmBehavior(false, 0.999);
      const pc = new PolarCoordinate(50, 30);
      sb.getNextState([pc], []);
      const angle = sb.generateAngle();
      expect(angle).toBeGreaterThanOrEqual(0);
      expect(angle).toBeLessThan(360);
    });

    it('should clamp angle when limit is true and result is in restricted range', () => {
      const sb = new SwarmBehavior(true, 0.999);
      // With limit=true, only coords with theta > 270 or theta < 90 are visible.
      // Place a position at theta=45 (forward-left arc) far enough to be in attraction zone.
      const pc = new PolarCoordinate(50, 45);
      sb.getNextState([pc], []);
      const angle = sb.generateAngle();
      // Result should never be strictly between 90 and 270 due to clamping.
      expect(angle <= 90 || angle >= 270).toBe(true);
    });
  });
});
