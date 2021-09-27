package com.project.iknowwhatnonedoes;

public class Upload {
    private String Name, Country, Email, Experience, Date, UID;

    public Upload() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    @Override
    public String toString() {
        return "Upload{" +
                "Name='" + Name + '\'' +
                ", Country='" + Country + '\'' +
                ", Email='" + Email + '\'' +
                ", Experience='" + Experience + '\'' +
                ", Date='" + Date + '\'' +
                ", UID='" + UID + '\'' +
                '}';
    }

    public Upload(String name, String country, String email, String experience, String date, String uid) {
        Name = name;
        Country = country;
        Email = email;
        Experience = experience;
        Date = date;
        UID = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getExperience() {
        return Experience;
    }

    public void setExperience(String experience) {
        Experience = experience;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
