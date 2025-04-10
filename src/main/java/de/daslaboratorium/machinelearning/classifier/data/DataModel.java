package de.daslaboratorium.machinelearning.classifier.data;

public class DataModel {
    private String age;
    private String income;
    private String student;
    private String creditRating;
    private String buysComputer;

    public DataModel(String age, String income, String student, String creditRating, String buysComputer) {
        this.age = age;
        this.income = income;
        this.student = student;
        this.creditRating = creditRating;
        this.buysComputer = buysComputer;
    }

    public String getAge() {
        return age;
    }

    public String getIncome() {
        return income;
    }

    public String getStudent() {
        return student;
    }

    public String getCreditRating() {
        return creditRating;
    }

    public String getBuysComputer() {
        return buysComputer;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "age='" + age + '\'' +
                ", income='" + income + '\'' +
                ", student='" + student + '\'' +
                ", creditRating='" + creditRating + '\'' +
                ", buysComputer='" + buysComputer + '\'' +
                '}';
    }
}