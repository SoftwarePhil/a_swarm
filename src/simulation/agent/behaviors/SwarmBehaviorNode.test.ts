import { SwarmBehaviorNode } from './SwarmBehaviorNode';
import { PolarCoordinate } from '../../common/PolarCoordinate';
import { Speed } from '../../common/Speed';

// Default l=0.999, alpha=0.001, x = sqrt(999) ≈ 31.62
const DEFAULT_X = Math.sqrt(0.999 / 0.001);

describe('SwarmBehaviorNode', () => {
  describe('generateSpeed (acceleration)', () => {
    it('should return MEDIUM when no neighbors are visible', () => {
      const sbn = new SwarmBehaviorNode(false);
      sbn.getNextState([], []);
      expect(sbn.generateSpeed()).toBe(Speed.MEDIUM);
    });

    it('should return ~1 (VERYSLOW) when nearest neighbor is at equilibrium distance', () => {
      const sbn = new SwarmBehaviorNode(false);
      const pc = new PolarCoordinate(DEFAULT_X, 45);
      sbn.getNextState([pc], []);
      expect(sbn.generateSpeed()).toBeCloseTo(1, 4);
    });

    it('should slow down when a neighbor is closer than equilibrium', () => {
      const sbn = new SwarmBehaviorNode(false);
      const pc = new PolarCoordinate(DEFAULT_X * 0.25, 30);
      sbn.getNextState([pc], []);
      expect(sbn.generateSpeed()).toBeLessThan(1);
    });

    it('should speed up when neighbor is farther than equilibrium', () => {
      const sbn = new SwarmBehaviorNode(false);
      const pc = new PolarCoordinate(DEFAULT_X * 2, 30);
      sbn.getNextState([pc], []);
      expect(sbn.generateSpeed()).toBeGreaterThan(1);
    });

    it('should cap speed at MEDIUM regardless of distance', () => {
      const sbn = new SwarmBehaviorNode(false);
      const pc = new PolarCoordinate(DEFAULT_X * 1000, 30);
      sbn.getNextState([pc], []);
      expect(sbn.generateSpeed()).toBe(Speed.MEDIUM);
    });

    it('should ignore non-attractive (crashed) neighbors when computing speed', () => {
      const sbn = new SwarmBehaviorNode(false);
      // One crashed neighbor very close, one attractive neighbor very far (beyond MEDIUM cap)
      const crashed = new PolarCoordinate(1, 10);
      crashed.setAttractionFalse();
      const far = new PolarCoordinate(DEFAULT_X * 15, 30);
      sbn.getNextState([crashed, far], []);
      // Should use the far attractive neighbor only, so speed is capped at MEDIUM
      expect(sbn.generateSpeed()).toBe(Speed.MEDIUM);
    });

    it('should use the closest attractive neighbor to set speed', () => {
      const sbn = new SwarmBehaviorNode(false);
      const near = new PolarCoordinate(DEFAULT_X * 0.5, 20);
      const far = new PolarCoordinate(DEFAULT_X * 5, 40);
      sbn.getNextState([near, far], []);
      // Speed is determined by nearest neighbor (0.5x), should be ~0.5
      expect(sbn.generateSpeed()).toBeCloseTo(0.5, 4);
    });
  });
});
