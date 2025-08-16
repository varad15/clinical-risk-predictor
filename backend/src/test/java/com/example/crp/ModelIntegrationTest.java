package com.example.crp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ModelIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void trainStatusPredict() throws Exception {
        Path csv = Path.of("../sample-data/clinical-sample.csv");
        byte[] bytes = Files.readAllBytes(csv);
        MockMultipartFile file = new MockMultipartFile("file", "clinical-sample.csv", "text/csv", bytes);

        mockMvc.perform(multipart("/api/model/train")
                .file(file)
                .param("trainFraction", "0.8")
                .param("seed", "42"))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/model/status"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.loaded").value(true));

        String json = "{\"age\":60,\"heart_rate\":90,\"systolic_bp\":140,\"diastolic_bp\":90,\"creatinine\":1.2}";
        mockMvc.perform(post("/api/model/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.label").exists());
    }
}
