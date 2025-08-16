package com.example.crp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.tribuo.MutableDataset;
import org.tribuo.data.csv.CSVLoader;
import org.tribuo.classification.Label;
import org.tribuo.classification.LabelFactory;
import org.tribuo.data.DataSource;
import org.tribuo.evaluation.TrainTestSplitter;

@Service
public class DatasetBuilder {

    public record DatasetSplit(MutableDataset<Label> train, MutableDataset<Label> test) {}

    public DatasetSplit build(MultipartFile file, double trainFraction, long seed) throws IOException {
        CSVLoader<Label> loader = new CSVLoader<>(new LabelFactory());
        Path temp = Files.createTempFile("upload", ".csv");
        file.transferTo(temp);
        DataSource<Label> source = loader.loadDataSource(temp, "risk");
        TrainTestSplitter<Label> splitter = new TrainTestSplitter<>(source, trainFraction, seed);
        MutableDataset<Label> train = new MutableDataset<>(splitter.getTrain());
        MutableDataset<Label> test = new MutableDataset<>(splitter.getTest());
        Files.deleteIfExists(temp);
        return new DatasetSplit(train, test);
    }
}
