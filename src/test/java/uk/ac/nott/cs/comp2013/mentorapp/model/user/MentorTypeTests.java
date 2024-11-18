package uk.ac.nott.cs.comp2013.mentorapp.model.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MentorTypeTests {

    @Test
    public void testEnumValues() {
        assertEquals("Alumni from the department", MentorType.ALUMNI.getDescription());
        assertEquals("Tech Industry Professional", MentorType.TECH_PROFESSIONAL.getDescription());
        assertEquals("Second Year Student", MentorType.SECOND_YEAR_STUDENT.getDescription());
        assertEquals("Third Year Student", MentorType.THIRD_YEAR_STUDENT.getDescription());
    }

    @Test
    public void testEnumNames() {
        assertEquals("ALUMNI", MentorType.ALUMNI.name());
        assertEquals("TECH_PROFESSIONAL", MentorType.TECH_PROFESSIONAL.name());
        assertEquals("SECOND_YEAR_STUDENT", MentorType.SECOND_YEAR_STUDENT.name());
        assertEquals("THIRD_YEAR_STUDENT", MentorType.THIRD_YEAR_STUDENT.name());
    }

    @Test
    public void testEnumOrdinal() {
        assertEquals(0, MentorType.ALUMNI.ordinal());
        assertEquals(1, MentorType.TECH_PROFESSIONAL.ordinal());
        assertEquals(2, MentorType.SECOND_YEAR_STUDENT.ordinal());
        assertEquals(3, MentorType.THIRD_YEAR_STUDENT.ordinal());
    }
}
