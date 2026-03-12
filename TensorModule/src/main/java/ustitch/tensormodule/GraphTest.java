package ustitch.tensormodule;

import java.util.Iterator;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Graph;
import org.tensorflow.GraphOperation;
import org.tensorflow.Operation;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.types.TFloat32;

public class GraphTest {
    public static void main(String[] args) {

        String modelPath = "src/main/models/ai_tf2";


        try (SavedModelBundle model = SavedModelBundle.load(modelPath, "serve")) {
            System.out.println("SUCCESS! Model loaded.");

            Graph graph = model.graph();
            
            
             // --- Step 2: Minimal Tensor Test ---
            try (Session session = new Session(graph)) {
                // Create a tiny 1x1 float tensor
                try (TFloat32 t = TFloat32.tensorOf(Shape.of(1,1))) {
                    t.setFloat(0.5f, 0, 0);   // assign valu
                    System.out.println("✅ Tensor created: " + t);

                    // Optional: just run it in a dummy fetch (if your graph has any compatible node)
                    // session.runner().feed("serving_default_placeholder", t).fetch("StatefulPartitionedCall").run();
                }
            }

            System.out.println("---- OPERATIONS IN GRAPH ----");

            // graph.operations() returns a GraphOperationIterator (not Iterable)
            Iterator<GraphOperation> it = graph.operations();

            while (it.hasNext()) {
                Operation op = it.next();
                System.out.println(op.name() + "  |  Type = " + op.type());

                int numOut = op.numOutputs();
                for (int i = 0; i < numOut; i++) {
                    System.out.println("   Output[" + i + "]: dtype=" + op.output(i).dataType());
                }

                System.out.println("-----------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
