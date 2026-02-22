import { Field } from './field/grassField/Field';
import { SwarmBehavior } from './agent/behaviors/SwarmBehavior';
import { OnBoundaryBehaviorAttraction } from './agent/behaviors/OnBoundaryBehaviorAttraction';
import { SwarmBehaviorNode } from './agent/behaviors/SwarmBehaviorNode';

async function main() {
  const field = new Field(
    100,
    500, 500,
    new SwarmBehaviorNode(true), 
    new OnBoundaryBehaviorAttraction()
  );

  console.log('Starting swarm simulation...');
  const MAX_STEPS = 100;
  for (let i = 0; i < MAX_STEPS; i++) {
    field.step();
    if (i % 10 === 0) {
      console.log(`Step ${i}: ${field.getPercentGrassCut().toFixed(2)}% grass cut`);
    }
  }
  console.log('Simulation complete.');
}

main().catch(console.error);
