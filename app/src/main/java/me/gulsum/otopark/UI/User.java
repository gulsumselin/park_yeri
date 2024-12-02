package me.gulsum.otopark.UI;

public class User {
    private String name;
    private String email;
    private String number;
    private String password;
    private String carPlate;
    private String carType;

    public User(String name, String email, String number, String password, String carPlate, String carType) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.password = password;
        this.carPlate = carPlate;
        this.carType = carType;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }
}
