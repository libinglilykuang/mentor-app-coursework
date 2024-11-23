package uk.ac.nott.cs.comp2013.mentorapp.model.logic;

import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.MenteeType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.MentorType;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RecommendationEngine provides functionality to recommend mentors for mentees
 * based on availability, mentee type, and mentor type preferences.
 */
public class RecommendationEngine {

    /**
     * Recommends up to three mentors for a given mentee from a list of mentors.
     *
     * @param mentee  The mentee for whom recommendations are generated.
     * @param mentors The list of available mentors.
     * @return A list of recommended mentors.
     */
    public static List<Mentor> recommendMentors(Mentee mentee, List<Mentor> mentors) {
        // Handle case where the mentee has no assigned type
        if (mentee.getMenteeType() == null) {
            System.out.println("Warning: MenteeType is null for mentee " + mentee.getUsername());
            return mentors.stream()
                    .filter(Mentor::isAvailable) // Only include available mentors
                    .limit(3) // Limit to three recommendations
                    .collect(Collectors.toList());
        }

        MenteeType menteeType = mentee.getMenteeType();
        MentorType preferredType = mentee.getPreferredMentorType();

        // Filter mentors by availability and compatibility with the mentee type
        List<Mentor> matchedMentors = mentors.stream()
                .filter(Mentor::isAvailable)
                .filter(mentor -> mentor.getMentorType() == null || matchesMenteeType(mentor.getMentorType(), menteeType))
                .collect(Collectors.toList());

        // Return all matched mentors if the count is less than or equal to 3
        if (matchedMentors.size() <= 3) {
            return matchedMentors;
        }

        // Sort mentors by preference and limit to three recommendations
        return matchedMentors.stream()
                .sorted(Comparator.comparingInt(mentor -> {
                    if (preferredType != null && preferredType.equals(mentor.getMentorType())) {
                        return 0; // Prioritize preferred mentor type
                    }
                    return 1;
                }))
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * Determines whether a mentor type matches a mentee type based on predefined rules.
     *
     * @param mentorType The type of the mentor.
     * @param menteeType The type of the mentee.
     * @return True if the mentor type matches the mentee type; otherwise, false.
     */
    private static boolean matchesMenteeType(MentorType mentorType, MenteeType menteeType) {
        switch (mentorType) {
            case ALUMNI, TECH_PROFESSIONAL:
                // Alumni and professionals can mentor all mentee types
                return menteeType == MenteeType.THIRD_YEAR_STUDENT ||
                        menteeType == MenteeType.SECOND_YEAR_STUDENT ||
                        menteeType == MenteeType.FIRST_YEAR_STUDENT;
            case SECOND_YEAR_STUDENT:
                // Second-year students can mentor first-year students
                return menteeType == MenteeType.FIRST_YEAR_STUDENT;
            case THIRD_YEAR_STUDENT:
                // Third-year students can mentor first- and second-year students
                return menteeType == MenteeType.SECOND_YEAR_STUDENT ||
                        menteeType == MenteeType.FIRST_YEAR_STUDENT;
            default:
                return false;
        }
    }
}
