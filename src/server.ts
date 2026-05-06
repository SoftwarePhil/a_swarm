import express from 'express';
import http from 'http';
import { WebSocketServer, WebSocket } from 'ws';
import path from 'path';
import { Field } from './simulation/field/grassField/Field';
import { OnBoundaryBehaviorAttraction } from './simulation/agent/behaviors/OnBoundaryBehaviorAttraction';
import { SwarmBehaviorNode } from './simulation/agent/behaviors/SwarmBehaviorNode';

const PORT = 3000;
const FIELD_WIDTH = 400;
const FIELD_HEIGHT = 400;

const app = express();
const server = http.createServer(app);
const wss = new WebSocketServer({ server });

app.use(express.static(path.join(__dirname, '..', 'public')));

interface SimConfig {
  l: number;
  scalar: number;
  attractionDistanceScalar: number;
  stepIntervalMs: number;
  numAgents: number;
}

const config: SimConfig = {
  l: 0.999,
  scalar: 1.0,
  attractionDistanceScalar: 1.5,
  stepIntervalMs: 50,
  numAgents: 30,
};

let field: Field;
let swarmBehavior: SwarmBehaviorNode;
let running = false;
let intervalId: ReturnType<typeof setInterval> | null = null;

function createField(): void {
  swarmBehavior = new SwarmBehaviorNode(true);
  swarmBehavior.updateParams({
    l: config.l,
    scalar: config.scalar,
    attractionDistanceScalar: config.attractionDistanceScalar,
  });
  field = new Field(
    config.numAgents,
    FIELD_WIDTH, FIELD_HEIGHT,
    swarmBehavior,
    new OnBoundaryBehaviorAttraction()
  );
}

function getSwarmRadii() {
  const alpha = 1 - config.l;
  const x = Math.sqrt(config.l / alpha);
  return {
    repulsionRadius: x,
    attractionRadius: x * config.attractionDistanceScalar,
  };
}

function getState() {
  const agents = field.getAgents().map(a => ({
    x: a.absoluteXPos,
    y: a.absoluteYPos,
    angle: a.newAngle,
    heading: a.getRelativeRobotAngle(),
    speed: a.newDistance,
    crashed: a.getCrashed(),
    id: a.getRobotName(),
  }));
  return {
    agents,
    grass: field.getGrassData(),
    steps: field.getNumberOfSteps(),
    percentCut: field.getPercentGrassCut(),
  };
}

function broadcast(payload: object): void {
  const msg = JSON.stringify(payload);
  wss.clients.forEach(client => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(msg);
    }
  });
}

function startLoop(): void {
  if (running) return;
  running = true;
  intervalId = setInterval(() => {
    field.step();
    broadcast({ type: 'state', ...getState() });
  }, config.stepIntervalMs);
}

function stopLoop(): void {
  running = false;
  if (intervalId !== null) {
    clearInterval(intervalId);
    intervalId = null;
  }
}

createField();

wss.on('connection', (ws) => {
  const { width, height } = field.getFieldSize();
  ws.send(JSON.stringify({ type: 'init', fieldWidth: width, fieldHeight: height, config: { ...config, ...getSwarmRadii() } }));
  ws.send(JSON.stringify({ type: 'state', ...getState() }));

  ws.on('message', (raw) => {
    try {
      const msg = JSON.parse(raw.toString()) as { action: string } & Partial<SimConfig> & { pendingNumAgents?: number };
      if (msg.action === 'start') {
        startLoop();
      } else if (msg.action === 'stop') {
        stopLoop();
      } else if (msg.action === 'reset') {
        stopLoop();
        createField();
        broadcast({ type: 'init', fieldWidth: FIELD_WIDTH, fieldHeight: FIELD_HEIGHT, config: { ...config, ...getSwarmRadii() } });
        broadcast({ type: 'state', ...getState() });
      } else if (msg.action === 'config') {
        let intervalChanged = false;

        if (msg.l !== undefined) config.l = msg.l;
        if (msg.scalar !== undefined) config.scalar = msg.scalar;
        if (msg.attractionDistanceScalar !== undefined) config.attractionDistanceScalar = msg.attractionDistanceScalar;
        if (msg.numAgents !== undefined) config.numAgents = msg.numAgents;
        if (msg.stepIntervalMs !== undefined && msg.stepIntervalMs !== config.stepIntervalMs) {
          config.stepIntervalMs = msg.stepIntervalMs;
          intervalChanged = true;
        }

        swarmBehavior.updateParams({
          l: config.l,
          scalar: config.scalar,
          attractionDistanceScalar: config.attractionDistanceScalar,
        });

        if (intervalChanged && running) {
          stopLoop();
          startLoop();
        }

        broadcast({ type: 'config', config: { ...config, ...getSwarmRadii() } });
      }
    } catch (err) {
      console.warn('Received malformed WebSocket message:', err);
    }
  });
});

server.listen(PORT, () => {
  console.log(`Swarm UI running at http://localhost:${PORT}`);
});
