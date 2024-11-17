package uk.ac.nott.cs.comp2013.mentorapp.model.user;

public enum MenteeType {
    FIRST_YEAR_STUDENT("First Year Student"),
    SECOND_YEAR_STUDENT("Second Year Student"),
    THIRD_YEAR_STUDENT("Third Year Student");

    private final String description;

    MenteeType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}