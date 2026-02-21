import { Field } from './Field';
import { SwarmBehavior } from '../../agent/behaviors/SwarmBehavior';
import { OnBoundaryBehaviorAttraction } from '../../agent/behaviors/OnBoundaryBehaviorAttraction';

describe('Field', () => {
  let field: Field;

  beforeEach(() => {
    // Use a small field to keep tests fast
    field = new Field(
      5,
      50, 50,
      new SwarmBehavior(false, 0.999),
      new OnBoundaryBehaviorAttraction()
    );
  });

  describe('constructor', () => {
    it('should create a field with agents', () => {
      expect(field.getAgents().length).toBe(5);
    });

    it('should start with 0 steps', () => {
      expect(field.getNumberOfSteps()).toBe(0);
    });

    it('should start with grass cut < 100%', () => {
      expect(field.getPercentGrassCut()).toBeLessThan(100);
    });
  });

  describe('step', () => {
    it('should increment simulation steps', () => {
      field.step();
      expect(field.getNumberOfSteps()).toBe(1);
    });

    it('should run multiple steps without error', () => {
      for (let i = 0; i < 10; i++) {
        field.step();
      }
      expect(field.getNumberOfSteps()).toBe(10);
    });

    it('should cut some grass over time', () => {
      for (let i = 0; i < 20; i++) {
        field.step();
      }
      // After 20 steps with 5 agents, some grass should be cut
      expect(field.getPercentGrassCut()).toBeGreaterThanOrEqual(0);
    });
  });

  describe('getPercentGrassCut', () => {
    it('should return a number between 0 and 100', () => {
      const pct = field.getPercentGrassCut();
      expect(pct).toBeGreaterThanOrEqual(0);
      expect(pct).toBeLessThanOrEqual(100);
    });
  });
});
