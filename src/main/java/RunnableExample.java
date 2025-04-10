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


        System.out.println("Data Awal:");
        for (int i = 0; i < Math.min(5, data.size()); i++) {
            DataModel row = data.get(i);
            System.out.printf("  %d: %s%n", i + 1, formatDataModel(row));
        }
        System.out.println();

        int numFolds = 5;
        double totalAccuracy = 0.0;
        double totalAccuracyYes = 0.0;
        double totalAccuracyNo = 0.0;

        // Deklarasikan naiveBayes di luar loop
        NaiveBayesClassifier naiveBayes = null;

        for (int fold = 0; fold < numFolds; fold++) {
            List<DataModel> trainingData = new ArrayList<>();
            List<DataModel> testingData = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                if (i % numFolds == fold) {
                    testingData.add(data.get(i));
                } else {
                    trainingData.add(data.get(i));
                }
            }

            // Inisialisasi naiveBayes di dalam loop
            naiveBayes = new NaiveBayesClassifier();
            naiveBayes.train(trainingData);

            EvaluationResults results = evaluateDetailed(naiveBayes, testingData);
            System.out.printf("Akurasi Lipatan %d: %.4f (Ya: %.4f, Tidak: %.4f)%n", fold + 1,
                    results.overallAccuracy, results.accuracyYes, results.accuracyNo);

            totalAccuracy += results.overallAccuracy;
            totalAccuracyYes += results.accuracyYes;
            totalAccuracyNo += results.accuracyNo;

            // Cetak contoh prediksi yang detail hanya untuk lipatan pertama
            // if (fold == 0 && !testingData.isEmpty()) { // Hilangkan kondisi ini
            System.out.println("\nDetail Prediksi untuk Lipatan " + (fold + 1) + ":");
            for (DataModel testData : testingData) {
                System.out.println("  Instance: " + testData);
                naiveBayes.predictDetailed(testData);
                System.out.println();
            }
            // }
        }

        double averageAccuracy = totalAccuracy / numFolds;
        double averageAccuracyYes = totalAccuracyYes / numFolds;
        double averageAccuracyNo = totalAccuracyNo / numFolds;

        System.out.printf("Akurasi Rata-Rata: %.4f (Ya: %.4f, Tidak: %.4f)%n",
                averageAccuracy, averageAccuracyYes, averageAccuracyNo);

        System.out.println("\n\nPrediksi Data 31...40:");
        DataModel imageData = new DataModel("31...40", "low", "no", "fair", null);
        System.out.println("  Instance: " + formatDataModel(imageData));
        naiveBayes.predictDetailed(imageData); // Gunakan naiveBayes yang telah dilatih
        System.out.println("  Beli Komputer (Prediksi): " + naiveBayes.predict(imageData));
    }

    public static EvaluationResults evaluateDetailed(NaiveBayesClassifier naiveBayes, List<DataModel> testingData) {
        int correctPredictionsYes = 0;
        int correctPredictionsNo = 0;
        int totalYes = 0;
        int totalNo = 0;

        for (DataModel row : testingData) {
            String predictedCategory = naiveBayes.predict(row);
            String actualCategory = row.getBuysComputer();

            if (actualCategory.equals("yes")) {
                totalYes++;
                if (predictedCategory != null && predictedCategory.equals(actualCategory)) {
                    correctPredictionsYes++;
                }
            } else if (actualCategory.equals("no")) {
                totalNo++;
                if (predictedCategory != null && predictedCategory.equals(actualCategory)) {
                    correctPredictionsNo++;
                }
            }
        }

        double accuracyYes = (totalYes > 0) ? (double) correctPredictionsYes / totalYes : 0.0;
        double accuracyNo = (totalNo > 0) ? (double) correctPredictionsNo / totalNo : 0.0;
        double overallAccuracy = (double) (correctPredictionsYes + correctPredictionsNo) / testingData.size();

        return new EvaluationResults(overallAccuracy, accuracyYes, accuracyNo);
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


    // Metode pembantu untuk memformat DataModel
    private static String formatDataModel(DataModel data) {
        return String.format("Umur: %-6s Pendapatan: %-7s Pelajar: %-4s Kredit: %-9s Beli Komputer: %-3s",
                data.getAge(), data.getIncome(), data.getStudent(), data.getCreditRating(), data.getBuysComputer());
    }

    // Kelas pembantu untuk menyimpan hasil evaluasi
    static class EvaluationResults {
        double overallAccuracy;
        double accuracyYes;
        double accuracyNo;

        public EvaluationResults(double overallAccuracy, double accuracyYes, double accuracyNo) {
            this.overallAccuracy = overallAccuracy;
            this.accuracyYes = accuracyYes;
            this.accuracyNo = accuracyNo;
        }
    }
}