import { AVector } from './AVector';

describe('AVector', () => {
  describe('constructor', () => {
    it('should set i and j', () => {
      const v = new AVector(3, 4);
      expect(v.i).toBe(3);
      expect(v.j).toBe(4);
    });
  });

  describe('length', () => {
    it('should calculate vector length', () => {
      const v = new AVector(3, 4);
      expect(v.length()).toBeCloseTo(5, 5);
    });

    it('should return 0 for zero vector', () => {
      const v = new AVector(0, 0);
      expect(v.length()).toBe(0);
    });
  });

  describe('getVectorAngle', () => {
    it('should return 0 for north direction (0, positive)', () => {
      const v = new AVector(0, 1);
      expect(AVector.getVectorAngle(v)).toBe(0);
    });

    it('should return 90 for east direction (positive, 0)', () => {
      const v = new AVector(1, 0);
      expect(AVector.getVectorAngle(v)).toBe(90);
    });

    it('should return 180 for south direction (0, negative)', () => {
      const v = new AVector(0, -1);
      expect(AVector.getVectorAngle(v)).toBe(180);
    });

    it('should return 270 for west direction (negative, 0)', () => {
      const v = new AVector(-1, 0);
      expect(AVector.getVectorAngle(v)).toBe(270);
    });
  });

  describe('addVector', () => {
    it('should add two vectors', () => {
      const a = new AVector(1, 2);
      const b = new AVector(3, 4);
      const result = AVector.addVector(a, b);
      expect(result.i).toBe(4);
      expect(result.j).toBe(6);
    });
  });
});
