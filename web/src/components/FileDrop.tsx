import React, { useCallback } from 'react';

interface Props {
  onFile: (file: File) => void;
}

const FileDrop: React.FC<Props> = ({ onFile }) => {
  const handle = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      const file = e.target.files?.[0];
      if (file) onFile(file);
    },
    [onFile]
  );

  return <input type="file" accept=".csv" onChange={handle} />;
};

export default FileDrop;
