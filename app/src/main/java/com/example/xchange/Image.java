package com.example.xchange;

public class Image {
    private String filePath; // Path to the image file (optional)
    private String description; // Optional metadata for the image

    // Constructor
    public Image(String filePath, String description) {
        this.filePath = filePath;
        this.description = description;
    }

    // Getters
    public String getFilePath() {
        return filePath;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Image{" +
                "filePath='" + filePath + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
