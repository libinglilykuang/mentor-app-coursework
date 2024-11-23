package uk.ac.nott.cs.comp2013.mentorapp.model.user;

public enum MentorType {
    ALUMNI("Alumni from the department"),
    TECH_PROFESSIONAL("Tech Industry Professional"),
    SECOND_YEAR_STUDENT("Second Year Student"),
    THIRD_YEAR_STUDENT("Third Year Student");

    private final String description;

    MentorType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}