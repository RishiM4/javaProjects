package src.neural_network;

import java.util.Arrays;

public class Network {

    Layer[] layers;

    public Network(int... sizes) {
        layers = new Layer[sizes.length - 1];
        for (int i = 0; i < sizes.length - 1; i++) {
            layers[i] = new Layer(sizes[i], sizes[i + 1]);
        }
    }

    // Activation
    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    private double sigmoidDerivative(double out) {
        return out * (1 - out);
    }

    // Softmax for output layer
    private double[] softmax(double[] x) {
        double max = Arrays.stream(x).max().getAsDouble();
        double sum = 0;
        double[] out = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            out[i] = Math.exp(x[i] - max);
            sum += out[i];
        }
        for (int i = 0; i < x.length; i++) {
            out[i] /= sum;
        }
        return out;
    }
    public static double crossEntropyLoss(double[] yPred, double[] yTrue) {
        double loss = 0;
        for (int i = 0; i < yPred.length; i++) {
            loss -= yTrue[i] * Math.log(yPred[i] + 1e-12); // avoid log(0)
        }
        return loss;
    }
    public double computeLoss(double[][] inputs, double[][] targets) {
        double sum = 0;
        for (int i = 0; i < inputs.length; i++) {
            double[] output = forward(inputs[i]);
            sum += crossEntropyLoss(output, targets[i]);
        }
        return sum / inputs.length;
    }

    // Forward pass
    private double[] activateLayer(Layer layer, double[] input, boolean isOutput) {
        double[] out = new double[layer.currentSize];
        for (int i = 0; i < layer.currentSize; i++) {
            double sum = layer.biases[i];
            for (int j = 0; j < layer.previousSize; j++) {
                sum += input[j] * layer.weights[i][j];
            }
            out[i] = sigmoid(sum);
        }
        if (isOutput) out = softmax(out); // softmax only on output layer
        layer.outputs = out;
        return out;
    }

    public double[] forward(double[] input) {
        double[] out = input;
        for (int i = 0; i < layers.length; i++) {
            out = activateLayer(layers[i], out, i == layers.length - 1);
        }
        return out;
    }

    // Backpropagation with cross-entropy + softmax
    public void backPropagate(double[] input, double[] target, double lr) {

        double[][] deltas = new double[layers.length][];

        // Output layer delta = output - target (cross-entropy derivative)
        Layer outputLayer = layers[layers.length - 1];
        deltas[layers.length - 1] = new double[outputLayer.currentSize];
        for (int i = 0; i < outputLayer.currentSize; i++) {
            deltas[layers.length - 1][i] = outputLayer.outputs[i] - target[i];
        }

        // Hidden layers
        for (int k = layers.length - 2; k >= 0; k--) {
            Layer current = layers[k];
            Layer next = layers[k + 1];
            deltas[k] = new double[current.currentSize];
            for (int i = 0; i < current.currentSize; i++) {
                double sum = 0;
                for (int j = 0; j < next.currentSize; j++) {
                    sum += next.weights[j][i] * deltas[k + 1][j];
                }
                deltas[k][i] = sum * sigmoidDerivative(current.outputs[i]);
            }
        }

        // Update weights
        for (int k = 0; k < layers.length; k++) {
            Layer layer = layers[k];
            double[] prevOutput = (k == 0 ? input : layers[k - 1].outputs);
            for (int i = 0; i < layer.currentSize; i++) {
                for (int j = 0; j < layer.previousSize; j++) {
                    layer.weights[i][j] -= lr * deltas[k][i] * prevOutput[j];
                }
                layer.biases[i] -= lr * deltas[k][i];
            }
        }
    }
    public void train(double[][] inputs, double[][] targets, double rate) {
        for (int i = 0; i < inputs.length; i++) {
            backPropagate(inputs[i], targets[i], rate);
        }
    }
    // Mini-batch training
    public void trainBatch(double[][] inputs, double[][] targets, int batchSize, double lr) {
        int n = inputs.length;
        for (int start = 0; start < n; start += batchSize) {
            int end = Math.min(start + batchSize, n);
            for (int i = start; i < end; i++) {
                backPropagate(inputs[i], targets[i], lr);
            }
        }
    }

    // Accuracy
    public double accuracy(double[][] inputs, double[][] targets) {
        int correct = 0;
        for (int i = 0; i < inputs.length; i++) {
            int pred = argmax(forward(inputs[i]));
            int actual = argmax(targets[i]);
            if (pred == actual) correct++;
        }
        return 100.0 * correct / inputs.length;
    }

    private int argmax(double[] arr) {
        int best = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[best]) best = i;
        }
        return best;
    }
}
