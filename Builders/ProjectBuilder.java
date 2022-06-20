package Builders;

import Entities.Project;

public class ProjectBuilder {
    private String projectName;
    private String projectAddress;
    private String projectType;
    private int erfNumber;
    private double projectCost;
    public ProjectBuilder(String projectName) {
        this.projectName = projectName;
    }

    public ProjectBuilder withAddress(String address) {
        this.projectAddress = address;
        return this;
    }

    public ProjectBuilder withType(String type) {
        this.projectType = type;
        return this;
    }

    public ProjectBuilder withErfNumber(int erfNumber) {
        this.erfNumber = erfNumber;
        return this;
    }

    public ProjectBuilder withCost(double cost) {
        this.projectCost = cost;
        return this;
    }

    public Project build() {
        return new Project(projectName, projectAddress, projectType, erfNumber, projectCost);
    }
}
