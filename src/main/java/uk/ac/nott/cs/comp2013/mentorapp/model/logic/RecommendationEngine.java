package uk.ac.nott.cs.comp2013.mentorapp.model.logic;

import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.MenteeType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.MentorType;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RecommendationEngine {

    public static List<Mentor> recommendMentors(Mentee mentee, List<Mentor> mentors) {
        if (mentee.getMenteeType() == null) {
            System.out.println("Warning: MenteeType is null for mentee " + mentee.getUsername());
            return mentors.stream()
                    .filter(Mentor::isAvailable)
                    .limit(3)
                    .collect(Collectors.toList());
        }

        MenteeType menteeType = mentee.getMenteeType();
        MentorType preferredType = mentee.getPreferredMentorType();
        List<Mentor> matchedMentors = mentors.stream()
                .filter(Mentor::isAvailable)
                .filter(mentor -> mentor.getMentorType() == null || matchesMenteeType(mentor.getMentorType(), menteeType)) // 匹配规则
                .collect(Collectors.toList());
        if (matchedMentors.size() <= 3) {
            return matchedMentors;
        }
        return matchedMentors.stream()
                .sorted(Comparator.comparingInt(mentor -> {
                    if (preferredType != null && preferredType.equals(mentor.getMentorType())) {
                        return 0;
                    }
                    return 1;
                }))
                .limit(3)
                .collect(Collectors.toList());
    }

    private static boolean matchesMenteeType(MentorType mentorType, MenteeType menteeType) {
        switch (mentorType) {
            case ALUMNI, TECH_PROFESSIONAL:
                return menteeType == MenteeType.THIRD_YEAR_STUDENT || menteeType == MenteeType.SECOND_YEAR_STUDENT|| menteeType == MenteeType.FIRST_YEAR_STUDENT;
            case SECOND_YEAR_STUDENT:
                return menteeType == MenteeType.FIRST_YEAR_STUDENT;
            case THIRD_YEAR_STUDENT:
                return menteeType == MenteeType.SECOND_YEAR_STUDENT || menteeType == MenteeType.FIRST_YEAR_STUDENT;
            default:
                return false;
        }
    }

}
