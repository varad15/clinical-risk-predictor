import React, { useState } from 'react';
import FileDrop from '../components/FileDrop';
import MetricCard from '../components/MetricCard';
import { trainModel, TrainResponse } from '../lib/api';

const Train: React.FC = () => {
  const [file, setFile] = useState<File>();
  const [trainFraction, setTrainFraction] = useState(0.8);
  const [seed, setSeed] = useState(42);
  const [result, setResult] = useState<TrainResponse>();

  const submit = async () => {
    if (!file) return;
    const res = await trainModel(file, trainFraction, seed);
    setResult(res);
  };

  return (
    <div>
      <FileDrop onFile={setFile} />
      <div>
        <label>Train Fraction: {trainFraction.toFixed(2)}</label>
        <input type="range" min="0.5" max="0.9" step="0.1" value={trainFraction} onChange={e => setTrainFraction(parseFloat(e.target.value))} />
      </div>
      <div>
        <label>Seed: </label>
        <input type="number" value={seed} onChange={e => setSeed(parseInt(e.target.value))} />
      </div>
      <button onClick={submit} disabled={!file}>Train</button>
      {result && (
        <div style={{ display: 'flex', flexWrap: 'wrap' }}>
          <MetricCard title="Train Count" value={result.trainCount} />
          <MetricCard title="Test Count" value={result.testCount} />
          <MetricCard title="Accuracy" value={result.accuracy.toFixed(3)} />
          <MetricCard title="Confusion" value={result.confusion} />
        </div>
      )}
    </div>
  );
};

export default Train;
