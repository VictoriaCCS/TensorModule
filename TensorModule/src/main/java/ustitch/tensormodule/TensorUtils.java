package ustitch.tensormodule;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.types.TFloat32;

public class TensorUtils {

    public static TFloat32 createDummyTensor() {
        TFloat32 tensor = TFloat32.tensorOf(Shape.of(1,1,1,1));
        tensor.setFloat(0.5f, 0,0,0,0);
        return tensor;
    }

    public static TFloat32 imageToTensor(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        TFloat32 tensor = TFloat32.tensorOf(Shape.of(1, height, width, 3));
        PixelReader reader = image.getPixelReader();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = reader.getArgb(x, y);
                float r = ((argb >> 16) & 0xFF) / 255f;
                float g = ((argb >> 8) & 0xFF) / 255f;
                float b = (argb & 0xFF) / 255f;

                tensor.setFloat(r, 0, y, x, 0);
                tensor.setFloat(g, 0, y, x, 1);
                tensor.setFloat(b, 0, y, x, 2);
            }
        }

        return tensor;
    }

    public static Image tensorToImage(TFloat32 tensor) {
        long[] shape = tensor.shape().asArray();
        int height = (int) shape[1];
        int width = (int) shape[2];

        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float r = tensor.getFloat(0, y, x, 0);
                float g = tensor.getFloat(0, y, x, 1);
                float b = tensor.getFloat(0, y, x, 2);

                int ir = Math.min(255, Math.max(0, (int) (r*255)));
                int ig = Math.min(255, Math.max(0, (int) (g*255)));
                int ib = Math.min(255, Math.max(0, (int) (b*255)));

                int argb = (255 << 24) | (ir << 16) | (ig << 8) | ib;
                writer.setArgb(x, y, argb);
            }
        }

        return image;
    }
    
    /** 
 * Resize a JavaFX Image to a specific width and height
 */
public static Image resizeImage(Image input, int targetWidth, int targetHeight) {
    WritableImage output = new WritableImage(targetWidth, targetHeight);
    PixelReader reader = input.getPixelReader();
    PixelWriter writer = output.getPixelWriter();

    double scaleX = input.getWidth() / targetWidth;
    double scaleY = input.getHeight() / targetHeight;

    for (int y = 0; y < targetHeight; y++) {
        for (int x = 0; x < targetWidth; x++) {
            int srcX = (int) (x * scaleX);
            int srcY = (int) (y * scaleY);
            writer.setArgb(x, y, reader.getArgb(srcX, srcY));
        }
    }
    return output;
}

}
