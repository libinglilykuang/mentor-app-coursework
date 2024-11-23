package uk.ac.nott.cs.comp2013.mentorapp.model.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MenteeTypeTests {

    @Test
    public void testEnumValues() {
        assertEquals("First Year Student", MenteeType.FIRST_YEAR_STUDENT.getDescription());
        assertEquals("Second Year Student", MenteeType.SECOND_YEAR_STUDENT.getDescription());
        assertEquals("Third Year Student", MenteeType.THIRD_YEAR_STUDENT.getDescription());
    }

    @Test
    public void testEnumNames() {
        assertEquals("FIRST_YEAR_STUDENT", MenteeType.FIRST_YEAR_STUDENT.name());
        assertEquals("SECOND_YEAR_STUDENT", MenteeType.SECOND_YEAR_STUDENT.name());
        assertEquals("THIRD_YEAR_STUDENT", MenteeType.THIRD_YEAR_STUDENT.name());
    }

    @Test
    public void testEnumOrdinal() {
        assertEquals(0, MenteeType.FIRST_YEAR_STUDENT.ordinal());
        assertEquals(1, MenteeType.SECOND_YEAR_STUDENT.ordinal());
        assertEquals(2, MenteeType.THIRD_YEAR_STUDENT.ordinal());
    }
}
