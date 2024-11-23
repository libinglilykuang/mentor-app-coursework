package uk.ac.nott.cs.comp2013.mentorapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MenteeControllerTests {
    private Repository<User, String> menteeRepository;
    private Mentee mentee;
    private MenteeController controller;

    @BeforeEach
    public void setUp() {
        menteeRepository = mock();
        mentee = new Mentee("mentee1", "password", "CV Text", null);
        controller = new MenteeController(menteeRepository, mentee);
    }

    @Test
    public void testRequestSupport() {
        SupportType supportType = SupportType.INTERVIEW_PREPARATION;
        controller.requestSupport(supportType);
        assertEquals(supportType, mentee.getSupportRequest());
        verify(menteeRepository, times(1)).update(mentee);
    }

    @Test
    public void testSetAndGetMentee() {
        Mentee newMentee = new Mentee("mentee2", "password2", "Another CV Text", SupportType.CV_REVIEW);
        controller.setMentee(newMentee);
        assertEquals(newMentee, controller.getMentee());
    }

    @Test
    public void testGetRepository() {
        assertEquals(menteeRepository, controller.getRepository());
    }
}
