package uk.ac.nott.cs.comp2013.mentorapp.model.user;

/**
 * Enum for different types of mentees.
 */
public enum MenteeType {
    FIRST_YEAR_STUDENT("First Year Student"),
    SECOND_YEAR_STUDENT("Second Year Student"),
    THIRD_YEAR_STUDENT("Third Year Student");

    private final String description;

    /**
     * Constructor to set the description.
     *
     * @param description Description of the mentee type.
     */
    MenteeType(String description) {
        this.description = description;
    }

    /**
     * Gets the description of the mentee type.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }
}
