package com.VectorCartProject.services.weaviate;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Base64;
import javax.imageio.ImageIO;

/**
 * Utility class with common methods for Weaviate operations
 */
public class WeaviateUtil {

    /**
     * Convert an image URL to Base64 string
     * @param imageUrl URL of the image
     * @return Base64 encoded string
     */
    public static String encodeImageToBase64(String imageUrl) {
        try {
            // Read image from URL
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);

            // Convert image to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] imageBytes = baos.toByteArray();

            // Encode to Base64
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generate a vector of specified dimension with random values
     * @param dimension vector dimension
     * @return array of Float values
     */
    public static Float[] generateRandomVector(int dimension) {
        Float[] vector = new Float[dimension];
        for (int i = 0; i < dimension; i++) {
            vector[i] = (float) Math.random();
        }
        return vector;
    }
}