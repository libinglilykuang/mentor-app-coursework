package uk.ac.nott.cs.comp2013.mentorapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.MenteeType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.MentorType;

class RecommendationEngineTest {

    private Mentee mentee;
    private Mentor mentor1, mentor2, mentor3, mentor4;

    @BeforeEach
    void setUp() {
        // Mocking mentee
        mentee = mock(Mentee.class);

        // Mocking mentors
        mentor1 = mock(Mentor.class);
        mentor2 = mock(Mentor.class);
        mentor3 = mock(Mentor.class);
        mentor4 = mock(Mentor.class);

        // Setting up mentor mock behavior
        when(mentor1.isAvailable()).thenReturn(true);
        when(mentor2.isAvailable()).thenReturn(true);
        when(mentor3.isAvailable()).thenReturn(true);
        when(mentor4.isAvailable()).thenReturn(true);

        when(mentor1.getMentorType()).thenReturn(MentorType.ALUMNI);
        when(mentor2.getMentorType()).thenReturn(MentorType.TECH_PROFESSIONAL);
        when(mentor3.getMentorType()).thenReturn(MentorType.SECOND_YEAR_STUDENT);
        when(mentor4.getMentorType()).thenReturn(MentorType.THIRD_YEAR_STUDENT);
    }

    @Test
    void testRecommendMentorsWhenMenteeTypeIsNull() {
        when(mentee.getMenteeType()).thenReturn(null);

        List<Mentor> recommendedMentors = RecommendationEngine.recommendMentors(mentee, List.of(mentor1, mentor2, mentor3, mentor4));

        // Assert that up to three mentors are recommended
        assertEquals(3, recommendedMentors.size());
        assertTrue(recommendedMentors.contains(mentor1));
        assertTrue(recommendedMentors.contains(mentor2));
        assertTrue(recommendedMentors.contains(mentor3));
    }

    @Test
    void testRecommendMentorsWhenMentorsMatchMenteeType() {
        when(mentee.getMenteeType()).thenReturn(MenteeType.FIRST_YEAR_STUDENT);

        List<Mentor> recommendedMentors = RecommendationEngine.recommendMentors(mentee, List.of(mentor1, mentor2, mentor3, mentor4));

        // Assert that the mentors who can mentor first-year students are included
        assertTrue(recommendedMentors.contains(mentor1)); // ALUMNI
        assertTrue(recommendedMentors.contains(mentor2)); // TECH_PROFESSIONAL
        assertTrue(recommendedMentors.contains(mentor3)); // SECOND_YEAR_STUDENT
        assertFalse(recommendedMentors.contains(mentor4)); // THIRD_YEAR_STUDENT
    }

    @Test
    void testRecommendMentorsWithPreferredType() {
        when(mentee.getMenteeType()).thenReturn(MenteeType.FIRST_YEAR_STUDENT);
        when(mentee.getPreferredMentorType()).thenReturn(MentorType.ALUMNI);

        List<Mentor> recommendedMentors = RecommendationEngine.recommendMentors(mentee, List.of(mentor1, mentor2, mentor3, mentor4));

        // Assert that the preferred mentor type is prioritized
        assertEquals(mentor1, recommendedMentors.get(0));
    }

    @Test
    void testRecommendMentorsWithLessThanThreeAvailableMentors() {
        when(mentee.getMenteeType()).thenReturn(MenteeType.SECOND_YEAR_STUDENT);
        when(mentor1.isAvailable()).thenReturn(false);
        List<Mentor> recommendedMentors = RecommendationEngine.recommendMentors(mentee, List.of(mentor1, mentor2, mentor3));
        assertEquals(1, recommendedMentors.size());
        assertFalse(recommendedMentors.contains(mentor1));
        assertTrue(recommendedMentors.contains(mentor2));
        assertFalse(recommendedMentors.contains(mentor3));
    }

    @Test
    void testRecommendMentorsWithNoAvailableMentors() {
        when(mentee.getMenteeType()).thenReturn(MenteeType.SECOND_YEAR_STUDENT);
        when(mentor1.isAvailable()).thenReturn(false);
        when(mentor2.isAvailable()).thenReturn(false);
        when(mentor3.isAvailable()).thenReturn(false);
        when(mentor4.isAvailable()).thenReturn(false);

        List<Mentor> recommendedMentors = RecommendationEngine.recommendMentors(mentee, List.of(mentor1, mentor2, mentor3, mentor4));

        // Assert that no mentors are recommended
        assertTrue(recommendedMentors.isEmpty());
    }
}
