package models;

/**
 * The Staff class represents a staff member in the hospital system,
 * including their personal details and role.
 */
public class Staff {

    private String id;
    private String name;
    private String role;
    private String gender;
    private int age;

    /**
     * Constructs a Staff object with the specified details.
     *
     * @param id     The unique identifier for the staff member.
     * @param name   The name of the staff member.
     * @param role   The role of the staff member in the hospital.
     * @param gender The gender of the staff member.
     * @param age    The age of the staff member.
     */
    public Staff(String id, String name, String role, String gender, int age) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.age = age;
    }

    /**
     * Retrieves the unique identifier of the staff member.
     *
     * @return The staff member's ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the staff member.
     *
     * @param id The new ID for the staff member.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the staff member.
     *
     * @return The staff member's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the staff member.
     *
     * @param name The new name for the staff member.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the role of the staff member in the hospital.
     *
     * @return The staff member's role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the staff member in the hospital.
     *
     * @param role The new role for the staff member.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Retrieves the gender of the staff member.
     *
     * @return The staff member's gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the staff member.
     *
     * @param gender The new gender for the staff member.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Retrieves the age of the staff member.
     *
     * @return The staff member's age.
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the staff member.
     *
     * @param age The new age for the staff member.
     */
    public void setAge(int age) {
        this.age = age;
    }
}



