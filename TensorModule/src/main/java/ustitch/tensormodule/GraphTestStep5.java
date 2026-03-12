package ustitch.tensormodule;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.types.TFloat32;
import org.tensorflow.ndarray.Shape;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class GraphTestStep5 {

    public static void main(String[] args) {

        String modelPath = "src/main/models/ai_tf2";

        try (SavedModelBundle model = SavedModelBundle.load(modelPath, "serve")) {
            Graph graph = model.graph();

            try (Session session = new Session(graph)) {

                // --- Load a tiny JavaFX image (for test, 64x64) ---
                Image inputImage = new Image("file:src/main/models/style/style1.jpg"); 
                int width = (int) inputImage.getWidth();
                int height = (int) inputImage.getHeight();

                // --- Convert Image → float array → Tensor ---
                TFloat32 inputTensor = TFloat32.tensorOf(Shape.of(1, height, width, 3));
                PixelReader reader = inputImage.getPixelReader();
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int argb = reader.getArgb(x, y);
                        float r = ((argb >> 16) & 0xFF) / 255f;
                        float g = ((argb >> 8) & 0xFF) / 255f;
                        float b = (argb & 0xFF) / 255f;
                        inputTensor.setFloat(r, 0, y, x, 0);
                        inputTensor.setFloat(g, 0, y, x, 1);
                        inputTensor.setFloat(b, 0, y, x, 2);
                    }
                }

                // --- Run model ---
                String inputNode = "serving_default_placeholder"; // check GraphTest
                String outputNode = "StatefulPartitionedCall";    // check GraphTest

                TFloat32 outputTensor = (TFloat32) session.runner()
                        .feed(inputNode, inputTensor)
                        .fetch(outputNode)
                        .run()
                        .get(0);

                // --- Convert Tensor → WritableImage ---
                WritableImage outputImage = new WritableImage(width, height);
                PixelWriter writer = outputImage.getPixelWriter();

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        float r = outputTensor.getFloat(0, y, x, 0);
                        float g = outputTensor.getFloat(0, y, x, 1);
                        float b = outputTensor.getFloat(0, y, x, 2);

                        int ir = Math.min(255, Math.max(0, (int) (r * 255)));
                        int ig = Math.min(255, Math.max(0, (int) (g * 255)));
                        int ib = Math.min(255, Math.max(0, (int) (b * 255)));

                        int argb = (0xFF << 24) | (ir << 16) | (ig << 8) | ib;
                        writer.setArgb(x, y, argb);
                    }
                }

                System.out.println("✅ Image processed successfully!");

                // You can now display `outputImage` in an ImageView
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
