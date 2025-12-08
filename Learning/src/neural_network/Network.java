package src.neural_network;

public class Network {
    Layer[] layers;
    public Network(int... sizes) {
        layers = new Layer[sizes.length-1];
        for(int k = 0; k < sizes.length -1; k++) {
            layers[k] = new Layer(sizes[k], sizes[k+1]);
        }
    }
    public static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }
    public static double[] activateLayer(Layer layer, double[] input) {
        //given a layer and an input, calculate its given output
        double[] output = new double[layer.currentSize];
        for(int i = 0; i < layer.currentSize; i++) {
            double sum = layer.biases[i];
            for(int j = 0; j < layer.previousSize; j++) {
                sum += input[j] * layer.weights[i][j];
            }
            output[i] = sigmoid(sum);
        }
        layer.outputs = output;
        return output;
    }
    public double[] forward(double[] inputs) {
        double[] outputs = inputs;
        for(Layer layer : layers) {
            outputs = activateLayer(layer, outputs);
        }
        return outputs;
    }

    public static double calculateMSE(double[] predicted, double[] target) {
        double sum = 0.0;
        for(int k =0; k < predicted.length; k++) {
            double difference = target[k] - predicted[k];
            sum += difference*difference;
        }
        return sum;
    }
    public static void main(String[] args) {
        
    }
    
}
