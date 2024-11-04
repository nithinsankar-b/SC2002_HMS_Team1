package models;

/**
 * Represents a patient's medical record in the healthcare system.
 * This class holds various details about the patient, including personal information,
 * past diagnoses, and prescriptions.
 */
public class MedicalRecord {
    private String patientID;
    private String name;
    private String dob; // Date of Birth
    private String gender;
    private String phoneNumber; // Separate phone number
    private String emailAddress; // Separate email address
    private String bloodType;
    private String pastDiagnoses; // Separate past diagnoses
    private String pastPrescriptions; // Separate past prescriptions
    private String newDiagnosis; // Separate new diagnosis
    private String newPrescription; // Separate new prescription

    /**
     * Constructs a MedicalRecord object with the specified patient information.
     *
     * @param patientID The unique identifier for the patient.
     * @param name The name of the patient.
     * @param dob The date of birth of the patient.
     * @param gender The gender of the patient.
     * @param phoneNumber The phone number of the patient.
     * @param emailAddress The email address of the patient.
     * @param bloodType The blood type of the patient.
     * @param pastDiagnoses The past diagnoses of the patient.
     * @param pastPrescriptions The past prescriptions of the patient.
     * @param newDiagnosis The new diagnosis of the patient.
     * @param newPrescription The new prescription for the patient.
     */
    public MedicalRecord(String patientID, String name, String dob, String gender, String phoneNumber, String emailAddress, String bloodType, String pastDiagnoses, String pastPrescriptions, String newDiagnosis, String newPrescription) {
        this.patientID = patientID;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.bloodType = bloodType;
        this.pastDiagnoses = pastDiagnoses;
        this.pastPrescriptions = pastPrescriptions;
        this.newDiagnosis = newDiagnosis;
        this.newPrescription = newPrescription;
    }

    // Getters
    /**
     * Returns the patient's unique identifier.
     *
     * @return The patient ID.
     */
    public String getPatientID() { return patientID; }

    /**
     * Returns the patient's name.
     *
     * @return The name of the patient.
     */
    public String getName() { return name; }

    /**
     * Returns the patient's date of birth.
     *
     * @return The date of birth of the patient.
     */
    public String getDob() { return dob; }

    /**
     * Returns the patient's gender.
     *
     * @return The gender of the patient.
     */
    public String getGender() { return gender; }

    /**
     * Returns the patient's phone number.
     *
     * @return The phone number of the patient.
     */
    public String getPhoneNumber() { return phoneNumber; }

    /**
     * Returns the patient's email address.
     *
     * @return The email address of the patient.
     */
    public String getEmailAddress() { return emailAddress; }

    /**
     * Returns the patient's blood type.
     *
     * @return The blood type of the patient.
     */
    public String getBloodType() { return bloodType; }

    /**
     * Returns the patient's past diagnoses.
     *
     * @return The past diagnoses of the patient.
     */
    public String getPastDiagnoses() { return pastDiagnoses; }

    /**
     * Returns the patient's past prescriptions.
     *
     * @return The past prescriptions of the patient.
     */
    public String getPastPrescriptions() { return pastPrescriptions; }

    /**
     * Returns the patient's new diagnosis.
     *
     * @return The new diagnosis of the patient.
     */
    public String getNewDiagnosis() { return newDiagnosis; }

    /**
     * Returns the patient's new prescription.
     *
     * @return The new prescription of the patient.
     */
    public String getNewPrescription() { return newPrescription; }

    // Setters
    /**
     * Sets the patient's name.
     *
     * @param name The name of the patient.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Sets the patient's date of birth.
     *
     * @param dob The date of birth of the patient.
     */
    public void setDob(String dob) { this.dob = dob; }

    /**
     * Sets the patient's phone number.
     *
     * @param phoneNumber The phone number of the patient.
     */
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    /**
     * Sets the patient's email address.
     *
     * @param emailAddress The email address of the patient.
     */
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    /**
     * Sets the patient's blood type.
     *
     * @param bloodType The blood type of the patient.
     */
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    /**
     * Adds a new diagnosis to the medical record, moving the current new diagnosis to past diagnoses if applicable.
     *
     * @param newDiagnosis The new diagnosis to be added.
     */
    public void addNewDiagnosis(String newDiagnosis) {
        // Check if there is already a new diagnosis
        if (this.newDiagnosis != null && !this.newDiagnosis.isEmpty()) {
            // Move the current new diagnosis to past
            if (this.pastDiagnoses == null || this.pastDiagnoses.isEmpty()) {
                this.pastDiagnoses = this.newDiagnosis; // Initialize if empty
            } else {
                this.pastDiagnoses += "\n" + this.newDiagnosis; // Append if not empty
            }
        }

        // Set the new diagnosis
        this.newDiagnosis = newDiagnosis;
    }

    /**
     * Adds a new prescription to the medical record, moving the current new prescription to past prescriptions if applicable.
     *
     * @param newPrescription The new prescription to be added.
     */
    public void addNewPrescription(String newPrescription) {
        // Check if there is already a new prescription
        if (this.newPrescription != null && !this.newPrescription.isEmpty()) {
            // Move the current new prescription to past
            if (this.pastPrescriptions == null || this.pastPrescriptions.isEmpty()) {
                this.pastPrescriptions = this.newPrescription; // Initialize if empty
            } else {
                this.pastPrescriptions += "\n" + this.newPrescription; // Append if not empty
            }
        }

        // Set the new prescription
        this.newPrescription = newPrescription;
    }
}
