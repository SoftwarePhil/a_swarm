## A Swarm Simulation Program

Simulates a multi-agent system with no top level of control. All movements of swarm members are determined by each particular individual, with only local information. The idea of this program is to study how individual behavior maps to collective behavior (swarm intelligence and emergent behavior).

The codebase is written in **TypeScript** (Node.js). The original Java implementation is preserved in the `java/` directory for reference.

## Web UI

![Swarm UI](https://github.com/user-attachments/assets/95d6add8-d8a6-4ace-9c91-22372eaa5516)

The UI visualises the 200×200 grass field in real-time on an HTML5 Canvas:
- 🟩 **Green circles** — active agents with an orange direction indicator
- 🔴 **Red circle** — crashed agent
- **Grass tiles** — gradient from purple (tall/uncut) → teal (cut), dark border = boundary

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

### Launch the web UI
```
npm run start:ui
```
Then open **http://localhost:3000** in your browser.
Use the **Start / Stop / Reset** buttons to control the simulation.

### Run headlessly (CLI)
```
npm start
```
Prints step-by-step progress to the console.

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
src/
  server.ts                   # Express + WebSocket server (UI mode)
  simulation/
    common/                   # AVector, PolarCoordinate, State, Speed, Node, HistoryNode
    agent/
      brainPackage/           # Behavior (abstract), AgentBrain, BehaviorManager
      behaviors/              # SwarmBehavior, OnBoundaryBehavior*, MetaBehavior, NodeBehavior, SwarmBehaviorNode
    field/
      grassField/             # GrassNode, GrassGrower, ActualRobot, Field, RandomData
    startSimulation.ts        # CLI entry point

public/
  index.html                  # Canvas UI served by the web server

java/                         # Original Java source (for reference only)
  simulation/
  jars/
```

## Create your own behaviors and try some things out!

Implement the `Behavior` abstract class in `src/simulation/agent/behaviors/` and pass it to the `Field` constructor in `src/server.ts` (UI) or `src/simulation/startSimulation.ts` (CLI).
