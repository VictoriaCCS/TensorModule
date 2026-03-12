package ustitch.tensormodule;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.types.TFloat32;

public class ImageHelpers {

    /**
     * Converts a JavaFX Image to a TFloat32 tensor.
     * Assumes image is RGB and values scaled 0..1
     */
    public static TFloat32 imageToTensor(Image img) {
        int width = (int) img.getWidth();
        int height = (int) img.getHeight();

        PixelReader reader = img.getPixelReader();

        // Create a tensor of shape [1, height, width, 3]
        TFloat32 tensor = TFloat32.tensorOf(Shape.of(1, height, width, 3));

        // Read pixels
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = reader.getArgb(x, y);

                // Extract RGB and normalize to 0..1
                float r = ((argb >> 16) & 0xFF) / 255.0f;
                float g = ((argb >> 8) & 0xFF) / 255.0f;
                float b = (argb & 0xFF) / 255.0f;

                // Set tensor values: [0, y, x, channel]
                tensor.setFloat(r, 0, y, x, 0);
                tensor.setFloat(g, 0, y, x, 1);
                tensor.setFloat(b, 0, y, x, 2);
            }
        }

        return tensor;
    }

    /**
     * Helper for tiny dummy float arrays → TFloat32
     * Example: 2x2 image with RGB
     */
    public static TFloat32 dummyTensor() {
        TFloat32 tensor = TFloat32.tensorOf(Shape.of(1, 2, 2, 3));

        // Top-left pixel (red)
        tensor.setFloat(1.0f, 0, 0, 0, 0);
        tensor.setFloat(0.0f, 0, 0, 0, 1);
        tensor.setFloat(0.0f, 0, 0, 0, 2);

        // Top-right pixel (green)
        tensor.setFloat(0.0f, 0, 0, 1, 0);
        tensor.setFloat(1.0f, 0, 0, 1, 1);
        tensor.setFloat(0.0f, 0, 0, 1, 2);

        // Bottom-left pixel (blue)
        tensor.setFloat(0.0f, 0, 1, 0, 0);
        tensor.setFloat(0.0f, 0, 1, 0, 1);
        tensor.setFloat(1.0f, 0, 1, 0, 2);

        // Bottom-right pixel (white)
        tensor.setFloat(1.0f, 0, 1, 1, 0);
        tensor.setFloat(1.0f, 0, 1, 1, 1);
        tensor.setFloat(1.0f, 0, 1, 1, 2);

        return tensor;
    }
}
