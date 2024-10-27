package models;
public class Staff {
    private String id;
    private String name;
    private String gender;
    private String role;
    private int age;

    // Constructor
    public Staff(String id, String name, String gender, String role, int age) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.role = role;
        this.age = age;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

