package ustitch.tensormodule;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.types.TFloat32;
import org.tensorflow.ndarray.Shape;

public class GraphTestStep4 {
    public static void main(String[] args) {

        String modelPath = "src/main/models/ai_tf2";

        try (SavedModelBundle model = SavedModelBundle.load(modelPath, "serve")) {
            System.out.println("✅ Model loaded for Step 4.");

            Graph graph = model.graph();

            try (Session session = new Session(graph)) {

                // --- Create dummy input tensor ---
                // Assume the model expects input shape [1, height, width, 3]
                // We'll use a small 2x2 RGB image for test
                int H = 2, W = 2, C = 3;
                try (TFloat32 input = TFloat32.tensorOf(Shape.of(1, H, W, C))) {

                    // Fill tensor with random values between 0 and 1
                    for (int h = 0; h < H; h++) {
                        for (int w = 0; w < W; w++) {
                            for (int c = 0; c < C; c++) {
                                input.setFloat((float)Math.random(), 0, h, w, c);
                            }
                        }
                    }

                    System.out.println("✅ Dummy tensor created: " + input);

                    // --- Run the model with dummy input ---
                    // Use the actual input/output node names from your graph
                    String inputNode = "serving_default_placeholder"; // check your GraphTest output
                    String outputNode = "StatefulPartitionedCall";    // check your GraphTest output

                    try (TFloat32 output = (TFloat32) session.runner()
                            .feed(inputNode, input)
                            .fetch(outputNode)
                            .run()
                            .get(0)) {

                        System.out.println("✅ Dummy run completed, output tensor: " + output);
                        System.out.println("Output shape: " + output.shape());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
