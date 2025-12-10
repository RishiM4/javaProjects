package src.neural_network;

import java.io.*;
import java.util.*;

public class FlowerIrisNetwork {

    public static double[][] loadInputs(String path) throws Exception {
        List<double[]> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                // Four numeric features, normalize a bit
                double[] in = new double[4];
                for (int i = 0; i < 4; i++) {
                    in[i] = Double.parseDouble(parts[i]) / 10.0;
                }
                list.add(in);
            }
        }

        return list.toArray(new double[0][]);
    }

    public static double[][] loadTargets(String path) throws Exception {
        List<double[]> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                double[] out = new double[3];

                switch (parts[4]) {
                    case "Iris-setosa":     out[0] = 1; break;
                    case "Iris-versicolor": out[1] = 1; break;
                    case "Iris-virginica":  out[2] = 1; break;
                }

                list.add(out);
            }
        }

        return list.toArray(new double[0][]);
    }
    public static void main(String[] args) throws Exception {
        
        String path = "Learning/src/neural_network/datasets/iris_dataset.csv";

        double[][] inputs = loadInputs(path);
        double[][] targets = loadTargets(path);

        Network net = new Network(4, 8, 3);

        double lr = 0.2;
        int epochs = 5000;

        for (int e = 0; e < epochs; e++) {
            net.train(inputs, targets, lr);

            if (e % 500 == 0) {
                System.out.println("Epoch " + e + 
                    " Loss = " + net.computeLoss(inputs, targets));
            }
        }

        System.out.println("\nFinal accuracy: " + net.accuracy(inputs, targets) + "%");
    }
}
