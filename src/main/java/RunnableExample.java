import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.daslaboratorium.machinelearning.classifier.bayes.NaiveBayesClassifier;
import de.daslaboratorium.machinelearning.classifier.data.DataHelper;
import de.daslaboratorium.machinelearning.classifier.data.DataModel;

public class RunnableExample {

    public static void main(String[] args) {

//        String filePath = "src/dataset/comp.csv"; // Ganti dengan path yang sesuai
         String filePath = "/home/endos/Documents/IntelijIdea/Java-Naive-Bayes-Classifier/ee/data.txt";
        String fileType = "csv"; // Dapat berupa "csv" atau "txt"
        String delimiter = ";"; // Hanya digunakan untuk file TXT
        char separator = ';'; // Hanya digunakan untuk file CSV

        List<DataModel> data = DataHelper.loadData(filePath, fileType, delimiter, separator);

        int numFolds = 5; // Jumlah lipatan dalam validasi silang
        double totalAccuracy = 0.0;

        for (int fold = 0; fold < numFolds; fold++) {
            // Bagi data menjadi set training dan pengujian untuk lipatan saat ini
            List<DataModel> trainingData = new ArrayList<>();
            List<DataModel> testingData = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                if (i % numFolds == fold) {
                    testingData.add(data.get(i));
                } else {
                    trainingData.add(data.get(i));
                }
            }

            // Latih classifier
            NaiveBayesClassifier naiveBayes = new NaiveBayesClassifier();
            naiveBayes.train(trainingData);

            // Evaluasi classifier
            double accuracy = evaluate(naiveBayes, testingData);
            System.out.println("Akurasi Lipatan " + (fold + 1) + ": " + accuracy);
            totalAccuracy += accuracy;
        }

        double averageAccuracy = totalAccuracy / numFolds;
        System.out.println("Akurasi Rata-Rata: " + averageAccuracy);
    }

    public static double evaluate(NaiveBayesClassifier naiveBayes, List<DataModel> testingData) {
        int correctPredictions = 0;
        for (DataModel row : testingData) {
            String predictedCategory = naiveBayes.predict(row);
            if (predictedCategory != null && predictedCategory.equals(row.getBuysComputer())) {
                correctPredictions++;
            }
        }

        return (double) correctPredictions / testingData.size();
    }

}