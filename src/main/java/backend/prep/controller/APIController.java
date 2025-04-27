package backend.prep.controller;

import backend.prep.service.APIService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class APIController {

    private final APIService apiService;

    @Autowired
    public APIController(APIService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/query")
    public String queryModel(@RequestParam String question, @RequestParam String context) {
        try {
            String response = apiService.queryModel(question, context);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.get("answer").asText(); // Extracts only the "answer" field
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}