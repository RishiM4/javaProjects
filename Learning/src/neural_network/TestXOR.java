package src.neural_network;

import src.neural_network.*;

public class TestXOR {
    public static void main(String[] args) {

        // XOR Dataset
        double[][] inputs = {
            {0, 0},
            {0, 1},
            {1, 0},
            {1, 1}
        };

        double[][] targets = {
            {0},
            {1},
            {1},
            {0}
        };

        // Create network: 2 → 3 → 1
        Network net = new Network(2, 3, 1);

        // Train the network
        int epochs = 20000;
        double lr = 0.1;

        for (int epoch = 0; epoch < epochs; epoch++) {
            net.train(inputs, targets, lr);

            // Print loss occasionally
            if (epoch % 2000 == 0) {
                double loss = net.computeLoss(inputs, targets);
                System.out.println("Epoch " + epoch + "  Loss: " + loss);
            }
        }

        // Test the trained network
        System.out.println("\nTesting XOR after training:");
        for (int i = 0; i < inputs.length; i++) {
            double[] output = net.forward(inputs[i]);
            System.out.printf("%f XOR %f = %f%n",
                inputs[i][0], inputs[i][1], output[0]);
        }
    }
}
