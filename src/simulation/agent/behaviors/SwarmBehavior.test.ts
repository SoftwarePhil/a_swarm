import { SwarmBehavior } from './SwarmBehavior';
import { PolarCoordinate } from '../../common/PolarCoordinate';
import { Speed } from '../../common/Speed';

describe('SwarmBehavior', () => {
  describe('generateSpeed', () => {
    it('should return MEDIUM (10) when no neighbors are visible', () => {
      const sb = new SwarmBehavior(false, 0.999);
      expect(sb.generateSpeed()).toBe(Speed.MEDIUM);
    });

    it('should return a speed proportional to nearest-neighbor distance', () => {
      const sb = new SwarmBehavior(false, 0.999);
      // x = sqrt(0.999 / 0.001) ≈ 31.62; place a neighbor at that exact distance
      const x = Math.sqrt(0.999 / 0.001);
      const pc = new PolarCoordinate(x, 45);
      sb.getNextState([pc], []);
      // At equilibrium distance the speed should be ≈ 1 (VERYSLOW)
      expect(sb.generateSpeed()).toBeCloseTo(1, 4);
    });

    it('should slow down when a neighbor is closer than equilibrium', () => {
      const sb = new SwarmBehavior(false, 0.999);
      const x = Math.sqrt(0.999 / 0.001);
      const pc = new PolarCoordinate(x * 0.5, 45);
      sb.getNextState([pc], []);
      expect(sb.generateSpeed()).toBeLessThan(1);
    });

    it('should speed up when neighbors are farther than equilibrium, capped at MEDIUM', () => {
      const sb = new SwarmBehavior(false, 0.999);
      const x = Math.sqrt(0.999 / 0.001);
      const pc = new PolarCoordinate(x * 20, 45);
      sb.getNextState([pc], []);
      expect(sb.generateSpeed()).toBe(Speed.MEDIUM);
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
