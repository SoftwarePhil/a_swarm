import express from 'express';
import http from 'http';
import { WebSocketServer, WebSocket } from 'ws';
import path from 'path';
import { Field } from './simulation/field/grassField/Field';
import { SwarmBehavior } from './simulation/agent/behaviors/SwarmBehavior';
import { OnBoundaryBehaviorAttraction } from './simulation/agent/behaviors/OnBoundaryBehaviorAttraction';

const PORT = 3000;
const FIELD_WIDTH = 200;
const FIELD_HEIGHT = 200;
const NUM_AGENTS = 30;
const STEP_INTERVAL_MS = 50;

const app = express();
const server = http.createServer(app);
const wss = new WebSocketServer({ server });

app.use(express.static(path.join(__dirname, '..', 'public')));

let field: Field;
let running = false;
let intervalId: ReturnType<typeof setInterval> | null = null;

function createField(): void {
  field = new Field(
    NUM_AGENTS,
    FIELD_WIDTH, FIELD_HEIGHT,
    new SwarmBehavior(false, 0.999),
    new OnBoundaryBehaviorAttraction()
  );
}

function getState() {
  const agents = field.getAgents().map(a => ({
    x: a.absoluteXPos,
    y: a.absoluteYPos,
    angle: a.newAngle,
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
  }, STEP_INTERVAL_MS);
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
  ws.send(JSON.stringify({ type: 'init', fieldWidth: width, fieldHeight: height }));
  ws.send(JSON.stringify({ type: 'state', ...getState() }));

  ws.on('message', (raw) => {
    try {
      const msg = JSON.parse(raw.toString()) as { action: string };
      if (msg.action === 'start') {
        startLoop();
      } else if (msg.action === 'stop') {
        stopLoop();
      } else if (msg.action === 'reset') {
        stopLoop();
        createField();
        broadcast({ type: 'init', fieldWidth: FIELD_WIDTH, fieldHeight: FIELD_HEIGHT });
        broadcast({ type: 'state', ...getState() });
      }
    } catch (err) {
      console.warn('Received malformed WebSocket message:', err);
    }
  });
});

server.listen(PORT, () => {
  console.log(`Swarm UI running at http://localhost:${PORT}`);
});
