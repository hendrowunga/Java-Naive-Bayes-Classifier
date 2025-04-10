package de.daslaboratorium.machinelearning.classifier.bayes;

import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.Classification;
import de.daslaboratorium.machinelearning.classifier.data.DataModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class NaiveBayesClassifier {

    private Classifier<String, String> bayes;

    public NaiveBayesClassifier() {
        bayes = new BayesClassifier<>();
    }

    public void train(List<DataModel> trainingData) {
        for (DataModel row : trainingData) {
            Collection<String> feature = Arrays.asList(row.getAge(), row.getIncome(), row.getStudent(), row.getCreditRating());
            bayes.learn(row.getBuysComputer(), feature);
        }
    }

    public String predict(DataModel data) {
        Collection<String> feature = Arrays.asList(data.getAge(), data.getIncome(), data.getStudent(), data.getCreditRating());
        Classification<String, String> classification = bayes.classify(feature);
        return classification != null ? classification.getCategory() : null;  // Handel null case
    }

    public String predictDetailed(DataModel data) {
        Collection<String> features = Arrays.asList(data.getAge(), data.getIncome(), data.getStudent(), data.getCreditRating());
        System.out.println("\nMemproses instance: " + data);

        String predictedCategory = null;
        double maxPosterior = Double.NEGATIVE_INFINITY;

        // 1. Ambil semua kelas yang mungkin
        Set<String> categories = bayes.getCategories();

        // Loop melalui setiap kelas
        for (String category : categories) {
            System.out.println("\nKelas: " + category);

            // 2. Hitung probabilitas prior untuk kelas
            double priorProbability = (double) bayes.getCategoryCount(category) / bayes.getCategoriesTotal();
            System.out.printf("  Probabilitas Prior P(%s) = %.4f%n", category, priorProbability);

            // 3. Hitung probabilitas likelihood
            double likelihood = 1.0;
            System.out.println("  Probabilitas Likelihood:");
            for (String feature : features) {
                double conditionalProbability = bayes.featureProbability(feature, category);
                System.out.printf("    P(%s | %s) = %.4f%n", feature, category, conditionalProbability);
                likelihood *= conditionalProbability;
            }
            System.out.printf("  Total Likelihood P(fitur | %s) = %.4f%n", category, likelihood);

            // 4. Hitung probabilitas posterior
            double posteriorProbability = priorProbability * likelihood;
            System.out.printf("  Probabilitas Posterior P(%s | fitur) = %.4f%n", category, posteriorProbability);

            // Cek apakah ini adalah probabilitas posterior tertinggi
            if (posteriorProbability > maxPosterior) {
                maxPosterior = posteriorProbability;
                predictedCategory = category;
            }
        }

        System.out.println("\nPrediksi Akhir: " + predictedCategory);
        return predictedCategory;
    }
}