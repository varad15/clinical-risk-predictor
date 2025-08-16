# Clinical Risk Predictor

This project provides a small clinical risk predictor. Upload a CSV with clinical features and a `risk` column (`LOW`, `MEDIUM`, `HIGH`) to train a Tribuo multiclass logistic regression model. The backend exposes train, predict and status endpoints, while a minimal React app offers pages to interact with the model locally.

## Prerequisites

- Java 21 and Maven
- Node.js 18+

## How to Run

### Backend

```bash
cd backend
# Windows
.\mvnw.cmd -U spring-boot:run
# macOS/Linux
./mvnw -U spring-boot:run
```

### Frontend

```bash
cd web
npm install
npm run dev
```

Open <http://localhost:5173> in your browser.

### Try it

1. On the Train page upload `../sample-data/clinical-sample.csv`.
2. Switch to Predict and submit a record.
3. Check Status for model information.
4. Swagger UI available at <http://localhost:8080/swagger-ui.html>.

### Troubleshooting

- Ensure the backend runs on port 8080 and frontend on 5173.
- CORS is configured for `http://localhost:5173`.
- If ports are occupied, stop other services.

## License

[MIT](LICENSE)
