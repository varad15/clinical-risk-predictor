import React from 'react';

interface Props {
  title: string;
  value: string | number;
}

const MetricCard: React.FC<Props> = ({ title, value }) => (
  <div style={{ border: '1px solid #ccc', padding: '0.5rem', margin: '0.5rem' }}>
    <strong>{title}</strong>
    <div>{value}</div>
  </div>
);

export default MetricCard;
