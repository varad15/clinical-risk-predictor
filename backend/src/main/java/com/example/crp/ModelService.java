package com.example.crp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.tribuo.MutableDataset;
import org.tribuo.Prediction;
import org.tribuo.Model;
import org.tribuo.classification.Label;
import org.tribuo.classification.evaluation.LabelEvaluator;
import org.tribuo.classification.sgd.linear.LogisticRegressionTrainer;
import org.tribuo.data.example.ListExample;
import org.tribuo.Feature;
import org.tribuo.serialization.ModelSerializer;

import jakarta.annotation.PostConstruct;

@Service
public class ModelService {

    private final Path modelDir;
    private volatile Model<Label> model;
    private volatile boolean training = false;

    public ModelService(@Value("${app.modelDir}") String modelDir) throws IOException {
        this.modelDir = Paths.get(modelDir);
        Files.createDirectories(this.modelDir);
    }

    @PostConstruct
    public void loadIfExists() {
        Path modelPath = modelDir.resolve("latest.model");
        if (Files.exists(modelPath)) {
            try {
                this.model = ModelSerializer.deserialize(modelPath);
            } catch (Exception ignored) {}
        }
    }

    public TrainResult train(MultipartFile file, double trainFraction, long seed, DatasetBuilder builder) throws IOException {
        synchronized (this) {
            if (training) {
                throw new IllegalStateException("Training in progress");
            }
            training = true;
        }
        try {
            DatasetBuilder.DatasetSplit split = builder.build(file, trainFraction, seed);
            MutableDataset<Label> train = split.train();
            MutableDataset<Label> test = split.test();

            LogisticRegressionTrainer trainer = new LogisticRegressionTrainer();
            Model<Label> newModel = trainer.train(train);

            LabelEvaluator evaluator = new LabelEvaluator();
            var evaluation = evaluator.evaluate(newModel, test);

            Path modelPath = modelDir.resolve("latest.model");
            ModelSerializer.write(newModel, modelPath);
            this.model = newModel;

            return new TrainResult(
                Instant.now().toString(),
                train.size(),
                test.size(),
                evaluation.accuracy(),
                evaluation.getConfusionMatrix().toString(),
                modelPath.toString()
            );
        } finally {
            training = false;
        }
    }

    public PredictionResult predict(Map<String,Object> features) {
        if (model == null) {
            throw new IllegalStateException("Model not loaded");
        }
        List<Feature> featureList = new ArrayList<>();
        features.forEach((k,v) -> {
            if (v instanceof Number n) {
                featureList.add(new Feature(k, n.doubleValue()));
            }
        });
        ListExample<Label> example = new ListExample<>(new Label("unknown"), featureList);
        Prediction<Label> prediction = model.predict(example);
        String label = prediction.getOutput().getLabel();
        Map<String,Double> probs = new HashMap<>();
        prediction.getOutputScores().forEach((lbl, score) -> probs.put(lbl.getLabel(), score));
        return new PredictionResult(label, probs);
    }

    public Status status() {
        Path modelPath = modelDir.resolve("latest.model");
        return new Status(model != null, modelPath.toString());
    }

    public record TrainResult(String version, int trainCount, int testCount, double accuracy, String confusion, String modelPath) {}
    public record PredictionResult(String label, Map<String,Double> probabilities) {}
    public record Status(boolean loaded, String modelPath) {}
}
