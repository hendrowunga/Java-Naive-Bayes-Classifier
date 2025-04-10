package de.daslaboratorium.machinelearning.classifier.bayes;

import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.Classification;
import de.daslaboratorium.machinelearning.classifier.data.DataModel;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class NaiveBayesClassifier {

    private Classifier<String, String> bayes;

    public NaiveBayesClassifier() {
        bayes = new de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier<>();
    }

    public void train(List<DataModel> trainingData) {
        for (DataModel row : trainingData) {
            Collection<String> features = Arrays.asList(row.getAge(), row.getIncome(), row.getStudent(), row.getCreditRating());
            bayes.learn(row.getBuysComputer(), features);
        }
    }

    public String predict(DataModel data) {
        Collection<String> features = Arrays.asList(data.getAge(), data.getIncome(), data.getStudent(), data.getCreditRating());
        Classification<String, String> classification = bayes.classify(features);
        return classification != null ? classification.getCategory() : null; // Handle null case
    }
}