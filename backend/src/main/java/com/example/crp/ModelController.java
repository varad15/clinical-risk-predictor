package com.example.crp;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ModelController {

    private final ModelService modelService;
    private final DatasetBuilder datasetBuilder;

    public ModelController(ModelService modelService, DatasetBuilder datasetBuilder) {
        this.modelService = modelService;
        this.datasetBuilder = datasetBuilder;
    }

    @PostMapping("/model/train")
    public ModelService.TrainResult train(@RequestParam double trainFraction,
            @RequestParam long seed,
            @RequestPart("file") MultipartFile file) throws IOException {
        return modelService.train(file, trainFraction, seed, datasetBuilder);
    }

    @PostMapping("/model/predict")
    public ModelService.PredictionResult predict(@RequestBody Map<String,Object> features) {
        return modelService.predict(features);
    }

    @GetMapping("/model/status")
    public ModelService.Status status() {
        return modelService.status();
    }

    @GetMapping("/health")
    public Map<String,String> health() {
        return Map.of("status","UP");
    }
}
