package ustitch.tensormodule;

import org.tensorflow.Tensor;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.types.TFloat32;

public class TensorAI {

    public static void runModel() {
        // Create a scalar tensor
        TFloat32 tensor = TFloat32.scalarOf(42.0f);

        // Read the value safely without calling .data()
        float value = tensor.getFloat();  // <- safe in TF 0.5.0

        System.out.println("TensorFlow output: " + value);

        tensor.close();
    }

    public static void main(String[] args) {
        runModel();
    }
}
