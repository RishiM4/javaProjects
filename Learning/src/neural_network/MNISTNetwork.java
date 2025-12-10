package src.neural_network;

import java.io.*;
import java.util.*;

public class MNISTNetwork {

    public static double[][] loadInputsCSV(String csvPath) throws Exception {
        List<double[]> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(csvPath));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            double[] pixels = new double[parts.length];
            for (int i = 0; i < parts.length; i++) {
                pixels[i] = Double.parseDouble(parts[i]) / 255.0; // normalize
            }
            list.add(pixels);
        }
        br.close();
        return list.toArray(new double[0][]);
    }

    public static double[][] loadLabelsIDX(String idxPath) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(idxPath));

        int magic = dis.readInt();
        if (magic != 2049) {
            dis.close();
            throw new IOException("Invalid label file magic number");
        }
        int numLabels = dis.readInt();
        double[][] labels = new double[numLabels][10];

        for (int i = 0; i < numLabels; i++) {
            int label = dis.readUnsignedByte();
            labels[i][label] = 1.0; // one-hot encoding
        }

        dis.close();
        return labels;
    }
    public static void main(String[] args) throws Exception {

        double[][] trainImages = loadInputs("mnist_train.csv");
        double[][] trainLabels = loadLabels("mnist_train.csv");

        double[][] testImages = loadInputs("mnist_test.csv");
        double[][] testLabels = loadLabels("mnist_test.csv");

        Network net = new Network(784, 128, 64, 10);

        int epochs = 5;
        int batchSize = 32;
        double lr = 0.01;

        for (int e = 0; e < epochs; e++) {
            net.trainBatch(trainImages, trainLabels, batchSize, lr);
            double loss = net.computeLoss(trainImages, trainLabels);
            double acc = net.accuracy(testImages, testLabels);
            System.out.println("Epoch " + e + " | Loss: " + loss + " | Test accuracy: " + acc + "%");
        }
    }
}
