package com.example.crp;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

public class DatasetBuilderTest {

    private final DatasetBuilder builder = new DatasetBuilder();

    @Test
    void splitProducesData() throws Exception {
        Path csv = Path.of("../sample-data/clinical-sample.csv");
        byte[] bytes = Files.readAllBytes(csv);
        MockMultipartFile file = new MockMultipartFile("file", "clinical-sample.csv", "text/csv", bytes);
        var split = builder.build(file, 0.8, 42);
        assertTrue(split.train().size() > 0);
        assertTrue(split.test().size() > 0);
    }
}
