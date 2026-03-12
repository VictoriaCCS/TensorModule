package ustitch.tensormodule;

import java.io.File;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.types.TFloat32;

public class PatternAIApp extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // === NetBeans model path ===
            File modelPath = new File("src/main/models/ai_tf2");
            if (!modelPath.exists()) {
                throw new RuntimeException("AI model folder not found at src/main/models/ai_tf2");
            }

            // Load TensorFlow model
            try (SavedModelBundle model = SavedModelBundle.load(modelPath.getAbsolutePath(), "serve");
                 Session session = model.session()) {

                // Load images from resources (NetBeans)
                Image contentImg = new Image(getClass().getResourceAsStream("/styles/content1.jpg"));
                Image styleImg   = new Image(getClass().getResourceAsStream("/styles/style1.jpg"));

                // Convert images to TFloat32 tensors
                TFloat32 contentTensor = TensorUtils.imageToTensor(contentImg);
                TFloat32 styleTensor   = TensorUtils.imageToTensor(styleImg);

                // Run model
                TFloat32 outputTensor = (TFloat32) session.runner()
                        .feed("serving_default_placeholder", contentTensor)
                        .feed("serving_default_placeholder_1", styleTensor)
                        .fetch("StatefulPartitionedCall")
                        .run().get(0);

                // Convert tensor result to Image
                Image outputImg = TensorUtils.tensorToImage(outputTensor);

                // Show images
                ImageView ivContent = new ImageView(contentImg);
                ImageView ivStyle   = new ImageView(styleImg);
                ImageView ivOutput  = new ImageView(outputImg);

                VBox root = new VBox(10, ivContent, ivStyle, ivOutput);
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.setTitle("Pattern AI Demo - NetBeans");
                stage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
