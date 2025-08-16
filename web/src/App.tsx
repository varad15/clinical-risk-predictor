import React, { useState } from 'react';
import Train from './pages/Train';
import Predict from './pages/Predict';
import Status from './pages/Status';

const App: React.FC = () => {
  const [page, setPage] = useState<'train' | 'predict' | 'status'>('train');

  const render = () => {
    switch (page) {
      case 'predict':
        return <Predict />;
      case 'status':
        return <Status />;
      default:
        return <Train />;
    }
  };

  return (
    <div style={{ padding: '1rem' }}>
      <nav style={{ marginBottom: '1rem' }}>
        <button onClick={() => setPage('train')}>Train</button>
        <button onClick={() => setPage('predict')}>Predict</button>
        <button onClick={() => setPage('status')}>Status</button>
      </nav>
      {render()}
    </div>
  );
};

export default App;
