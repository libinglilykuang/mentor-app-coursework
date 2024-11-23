package uk.ac.nott.cs.comp2013.mentorapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MentorControllerTests {

    private Repository<User, String> mentorRepository;
    private Mentor mentor;

    @BeforeEach
    public void setUp() {
        mentorRepository = mock();
        mentor = new Mentor("mentor1", "password", null, null);
    }

    @Test
    public void testConstructorWithValidMentor() {
        MentorController controller = new MentorController(mentorRepository, mentor);
        assertEquals(mentor, controller.getMentor());
        assertEquals(mentorRepository, controller.getRepository());
    }

    @Test
    public void testConstructorWithNullMentor() {
        MentorController controller = new MentorController(mentorRepository, null);
        assertNotNull(controller.getMentor());
        assertEquals("default_mentor", controller.getMentor().getUsername());
        assertEquals("password", controller.getMentor().getPassword());
        assertEquals(mentorRepository, controller.getRepository());
    }

    @Test
    public void testGetMentor() {
        MentorController controller = new MentorController(mentorRepository, mentor);
        assertEquals(mentor, controller.getMentor());
    }

    @Test
    public void testGetRepository() {
        MentorController controller = new MentorController(mentorRepository, mentor);
        assertEquals(mentorRepository, controller.getRepository());
    }
}
