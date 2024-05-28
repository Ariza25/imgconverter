package com.example.watermark.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class WatermarkService {

    public byte[] addWatermark(byte[] imageBytes, String watermarkText, String position) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            Graphics2D g2d = (Graphics2D) image.getGraphics();

            // Configuração da marca d'água
            Font font = new Font("Arial", Font.BOLD, 30);
            g2d.setFont(font);
            g2d.setColor(new Color(255, 0, 0, 80));

            // Posição da marca d'água (é possível escolher a posição na imagem)
            FontMetrics fontMetrics = g2d.getFontMetrics();
            int x, y;
            switch (position.toLowerCase()) {
                case "top-left":
                    x = 10;
                    y = fontMetrics.getHeight();
                    break;
                case "top-right":
                    x = image.getWidth() - fontMetrics.stringWidth(watermarkText) - 10;
                    y = fontMetrics.getHeight();
                    break;
                case "bottom-left":
                    x = 10;
                    y = image.getHeight() - 10;
                    break;
                case "bottom-right":
                    x = image.getWidth() - fontMetrics.stringWidth(watermarkText) - 10;
                    y = image.getHeight() - 10;
                    break;
                default: // Center
                    x = (image.getWidth() - fontMetrics.stringWidth(watermarkText)) / 2;
                    y = image.getHeight() / 2;
                    break;
            }

            // Aplicação da marca d'água
            g2d.drawString(watermarkText, x, y);
            g2d.dispose();

            // Conversão de volta para byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar a imagem", e);
        }
    }
}
