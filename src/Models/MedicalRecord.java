package models;

public class MedicalRecord {
    private String patientID;
    private String name;
    private String dob;
    private String gender;
    private String phoneNumber; // Separate phone number
    private String emailAddress; // Separate email address
    private String bloodType;
    private String pastDiagnoses; // Separate past diagnoses
    private String pastPrescriptions; // Separate past prescriptions
    private String newDiagnosis; // Separate new diagnosis
    private String newPrescription; // Separate new prescription

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
    public String getPatientID() { return patientID; }
    public String getName() { return name; }
    public String getDob() { return dob; }
    public String getGender() { return gender; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmailAddress() { return emailAddress; }
    public String getBloodType() { return bloodType; }
    public String getPastDiagnoses() { return pastDiagnoses; }
    public String getPastPrescriptions() { return pastPrescriptions; }
    public String getNewDiagnosis() { return newDiagnosis; }
    public String getNewPrescription() { return newPrescription; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setDob(String dob) { this.dob = dob; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    // Update diagnosis and prescription
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
