package backend.prep.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class APIService {
    // Read the key from the environment, never hard-code it:
    //   export HUGGINGFACE_API_KEY=hf_...
    private static final String HUGGINGFACE_API_KEY = System.getenv("HUGGINGFACE_API_KEY");
    private static final String MODEL_URL = "https://api-inference.huggingface.co/models/distilbert-base-uncased-distilled-squad";

    public String queryModel(String question, String context) throws Exception {
        URL url = new URL(MODEL_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + HUGGINGFACE_API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String requestBody = "{\n" +
                "  \"inputs\": {\n" +
                "    \"question\": \"" + question + "\",\n" +
                "    \"context\": \"" + context + "\"\n" +
                "  }\n" +
                "}";
        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBody.getBytes());
            os.flush();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == 503) {
            return "Service Unavailable: The Hugging Face API is currently unavailable. Please try again later.";
        } else if (responseCode != 200) {
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                StringBuilder errorResponse = new StringBuilder();
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                throw new RuntimeException("HTTP error code " + responseCode + ": " + errorResponse);
            }
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }
}