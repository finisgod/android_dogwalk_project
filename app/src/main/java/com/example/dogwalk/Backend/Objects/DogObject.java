package com.example.dogwalk.Backend.Objects;

public class DogObject {

    private String id = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String name = "";
    private String age = "";
    private String breed = "";

    public DogObject(String name, String age, String breed , String id) {
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.id = id;
    }

    public DogObject() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getBreed() {
        return breed;
    }
}
