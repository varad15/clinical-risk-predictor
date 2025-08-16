import React, { useState } from 'react';
import { predict } from '../lib/api';

const initial = {
  age: 60,
  heart_rate: 80,
  systolic_bp: 120,
  diastolic_bp: 80,
  creatinine: 1.0
};

const Predict: React.FC = () => {
  const [features, setFeatures] = useState<Record<string, number>>(initial);
  const [result, setResult] = useState<any>();

  const submit = async () => {
    const res = await predict(features);
    setResult(res);
  };

  return (
    <div>
      {Object.entries(features).map(([k, v]) => (
        <div key={k}>
          <label>{k}: </label>
          <input
            type="number"
            value={v}
            onChange={e => setFeatures({ ...features, [k]: parseFloat(e.target.value) })}
          />
        </div>
      ))}
      <button onClick={submit}>Predict</button>
      {result && (
        <div>
          <p>Label: {result.label}</p>
          <pre>{JSON.stringify(result.probabilities, null, 2)}</pre>
        </div>
      )}
    </div>
  );
};

export default Predict;
