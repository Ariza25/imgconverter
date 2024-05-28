package com.example.watermark.controller;

import com.example.watermark.service.WatermarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/watermark")
public class WatermarkController {

    @Autowired
    private WatermarkService watermarkService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> addWatermark(@RequestBody Map<String, String> request) {
        String base64Image = request.get("image");
        String watermarkText = request.get("text");
        String position = request.get("position");

        if (base64Image.startsWith("data:image/png;base64,")) {
            base64Image = base64Image.substring("data:image/png;base64,".length());
        } else if (base64Image.startsWith("data:image/jpeg;base64,")) {
            base64Image = base64Image.substring("data:image/jpeg;base64,".length());
        }

        byte[] decodedImage = Base64.getDecoder().decode(base64Image);
        byte[] watermarkedImage = watermarkService.addWatermark(decodedImage, watermarkText, position);
        String base64WatermarkedImage = Base64.getEncoder().encodeToString(watermarkedImage);

        return ResponseEntity.ok(Map.of("image", base64WatermarkedImage));
    }
}
