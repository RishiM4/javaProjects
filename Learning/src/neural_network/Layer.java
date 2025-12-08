package src.neural_network;

public class Layer {
    int previousSize;
    int currentSize;
    double[][] weights;
    double[] biases;
    double[] outputs;

    public Layer(int previousSize, int currentSize) {
        this.previousSize = previousSize;
        this.currentSize = currentSize;

        this.weights = new double[currentSize][previousSize];
        this.biases = new double[currentSize];
        this.outputs = new double[currentSize];

        for(int i = 0; i < currentSize; i++) {
            for(int j = 0; j< previousSize; j++) {
                weights[i][j] = (Math.random() - 0.5) * 2;
            }
        }

        for(int k =0; k < biases.length; k++) {
            biases[k] = 0;
        }
    }
}
