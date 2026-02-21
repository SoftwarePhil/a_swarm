## A Swarm Simulation Program

Simulates a multi-agent system with no top level of control. All movements of swarm members are determined by each particular individual, with only local information. The idea of this program is to study how individual behavior maps to collective behavior (swarm intelligence and emergent behavior).

The codebase is written in **TypeScript** (Node.js). The original Java implementation lives in `src/simulation/**/*.java` for reference.

## How to run

### Prerequisites
- Node.js 18+
- npm

### Install dependencies
```
npm install
```

### Build
```
npm run build
```

### Run the simulation
```
npm start
```

The simulation runs headlessly and prints progress to the console every 10 steps, showing the percentage of the grass field that has been cut by the agents.

### Run tests
```
npm test
```

### Lint
```
npm run lint
```

## Project structure

```
src/simulation/
  common/          # AVector, PolarCoordinate, State, Speed, Node, HistoryNode
  agent/
    brainPackage/  # Behavior (abstract), AgentBrain, BehaviorManager
    behaviors/     # SwarmBehavior, OnBoundaryBehavior*, MetaBehavior, NodeBehavior, SwarmBehaviorNode
  field/
    grassField/    # GrassNode, GrassGrower, ActualRobot, Field, RandomData
  startSimulation.ts  # Entry point
```

## Create your own behaviors and try some things out!

Implement the `Behavior` abstract class in `src/simulation/agent/behaviors/` and pass it to the `Field` constructor in `startSimulation.ts`.
