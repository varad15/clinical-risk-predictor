import axios from 'axios';

const base = (import.meta.env.VITE_API_URL || 'http://localhost:8080') + '/api';
const api = axios.create({ baseURL: base });

export interface TrainResponse {
  version: string;
  trainCount: number;
  testCount: number;
  accuracy: number;
  confusion: string;
  modelPath: string;
}

export async function trainModel(file: File, trainFraction: number, seed: number): Promise<TrainResponse> {
  const form = new FormData();
  form.append('file', file);
  const res = await api.post(`/model/train?trainFraction=${trainFraction}&seed=${seed}`, form, {
    headers: { 'Content-Type': 'multipart/form-data' }
  });
  return res.data;
}

export async function predict(features: Record<string, number>) {
  const res = await api.post('/model/predict', features);
  return res.data as { label: string; probabilities: Record<string, number> };
}

export async function getStatus() {
  const res = await api.get('/model/status');
  return res.data as { loaded: boolean; modelPath: string };
}
