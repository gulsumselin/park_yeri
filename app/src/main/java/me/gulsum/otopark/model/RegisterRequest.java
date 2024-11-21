package me.gulsum.otopark.model;

public class RegisterRequest {
    private String name;
    private String email;
    private String phone;
    private String password;
    private String carPlate;
    private String carType;

    // Constructor
    public RegisterRequest(String name, String email, String phone, String password, String carPlate, String carType) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.carPlate = carPlate;
        this.carType = carType;
    }

    // Getter ve Setter metodları

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
