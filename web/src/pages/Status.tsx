import React, { useEffect, useState } from 'react';
import { getStatus } from '../lib/api';

const Status: React.FC = () => {
  const [status, setStatus] = useState<{ loaded: boolean; modelPath: string }>();

  useEffect(() => {
    getStatus().then(setStatus);
  }, []);

  return (
    <div>
      {status ? (
        status.loaded ? <p>Model: {status.modelPath}</p> : <p>No model loaded</p>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
};

export default Status;
