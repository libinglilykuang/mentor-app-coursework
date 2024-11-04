package uk.ac.nott.cs.comp2013.mentorapp.controller;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminControllerTests {

    @Test
    public void testSuccessfulPairing() {
        @SuppressWarnings("unchecked")
        Repository<User, String> repo = mock(Repository.class);
        Mentee mentee = new Mentee("mentee1", "password1");
        Mentor mentor = new Mentor("mentor1", "password2");

        when(repo.selectById("mentee1")).thenReturn(Optional.of(mentee));
        when(repo.selectById("mentor1")).thenReturn(Optional.of(mentor));

        AdminController controller = new AdminController(repo);
        boolean result = controller.pairMenteeWithMentor("mentee1", "mentor1");

        assertTrue(result);
        assertEquals(mentor, mentee.getAssignedMentor());
        assertEquals(mentee, mentor.getAssignedMentee());
    }

    @Test
    public void testPairingWithAssignedMentor() {
        @SuppressWarnings("unchecked")
        Repository<User, String> repo = mock(Repository.class);
        Mentee mentee1 = new Mentee("mentee1", "password1");
        Mentee mentee2 = new Mentee("mentee2", "password2");
        Mentor mentor = new Mentor("mentor1", "password2");

        when(repo.selectById("mentee1")).thenReturn(Optional.of(mentee1));
        when(repo.selectById("mentee2")).thenReturn(Optional.of(mentee2));
        when(repo.selectById("mentor1")).thenReturn(Optional.of(mentor));

        AdminController controller = new AdminController(repo);
        assertTrue(controller.pairMenteeWithMentor("mentee1", "mentor1"));
        assertFalse(controller.pairMenteeWithMentor("mentee2", "mentor1"));
    }
}
