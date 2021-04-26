package com.project.iknowwhatnonedoes;

public class Upload {
    private String name, country, email, experience;

    public Upload(String name, String country, String email, String experience) {
        this.name = name;
        this.country = country;
        this.email = email;
        this.experience = experience;
    }

    public Upload() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
