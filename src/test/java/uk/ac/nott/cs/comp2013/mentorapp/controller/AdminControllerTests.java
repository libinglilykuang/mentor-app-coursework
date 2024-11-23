package uk.ac.nott.cs.comp2013.mentorapp.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminControllerTests {
    private Repository<User, String> repo;
    private Mentee mentee1;
    private Mentee mentee2;
    private Mentor mentor;
    private AdminController controller;

    @BeforeEach
    public void setUp() {
        repo = mock();
        mentee1 = new Mentee("mentee1", "password1", "CV Text for mentee1", SupportType.INTERVIEW_PREPARATION);
        mentee2 = new Mentee("mentee2", "password2", "CV Text for mentee2", SupportType.CV_REVIEW);
        mentor = new Mentor("mentor1", "password2", LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1), MentorType.TECH_PROFESSIONAL);
        controller = new AdminController(repo);

        when(repo.selectById("mentee1")).thenReturn(Optional.of(mentee1));
        when(repo.selectById("mentor1")).thenReturn(Optional.of(mentor));
    }

    @Test
    public void testSetAndGetAdmin() {
        Administrator admin = new Administrator("admin1", "password");
        controller.setCurrentAdmin(admin);
        assertEquals(admin, controller.getAdmin());
    }

    @Test
    public void testGetAdminWithoutLogin() {
        Exception exception = assertThrows(IllegalStateException.class, () -> controller.getAdmin());
        assertEquals("No admin is currently logged in.", exception.getMessage());
    }

    @Test
    public void testSuccessfulPairing() {
        boolean result = controller.pairMenteeWithMentor("mentee1", "mentor1");
        assertTrue(result);
        assertEquals(mentor, mentee1.getAssignedMentor());
        assertEquals(mentee1, mentor.getAssignedMentee());
    }

    @Test
    public void testPairingWithAssignedMentor() {
        assertTrue(controller.pairMenteeWithMentor("mentee1", "mentor1"));
        assertFalse(controller.pairMenteeWithMentor("mentee2", "mentor1"));
    }

    @Test
    public void testPairingWithInvalidIds() {
        when(repo.selectById("mentee1")).thenReturn(Optional.empty());
        when(repo.selectById("mentor1")).thenReturn(Optional.empty());
        boolean result = controller.pairMenteeWithMentor("mentee1", "mentor1");
        assertFalse(result);
    }

    @Test
    public void testGetAllUnpairedMentees() {
        mentee1.assignMentor(mentor);
        mentee2.setSupportRequest(null);
        when(repo.selectAll()).thenReturn(List.of(mentee1, mentee2, mentor));
        List<Mentee> unpairedMentees = controller.getAllUnpairedMentees();
        assertEquals(1, unpairedMentees.size());
        assertEquals("mentee2", unpairedMentees.get(0).getUsername());
    }

    @Test
    public void testGetAvailableMentors() {
        mentee1.assignMentor(mentor);
        when(repo.selectAll()).thenReturn(List.of(mentor, mentee1, mentee2));
        List<Mentor> availableMentors = controller.getAvailableMentors();
        assertEquals(1, availableMentors.size());
    }

    @Test
    public void testGetAssignedPairs() {
        mentee1.assignMentor(mentor);
        when(repo.selectAll()).thenReturn(List.of(mentor, mentee1));
        var assignedPairs = controller.getAssignedPairs();
        assertEquals(1, assignedPairs.size());
        assertEquals(mentor, assignedPairs.getFirst().getKey());
        assertEquals(mentee1, assignedPairs.getFirst().getValue());
    }

    @Test
    public void testGetMentorTypeByName() {
        Mentor mentor = new Mentor("mentor1", "password");
        when(repo.selectById("mentor1")).thenReturn(Optional.of(mentor));
        String result = controller.getMentorTypeByName("mentor1");
        assertEquals("", result);
    }

    @Test
    public void testGetMentorAvailabilityByName() {
        mentor.setAvailability(LocalDate.now().atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());
        when(repo.selectById("mentor1")).thenReturn(Optional.of(mentor));
        String result = controller.getMentorAvailabilityByName("mentor1");
        assertNotNull(result);
        assertTrue(result.contains(" - "));
    }

    @Test
    public void testGetMenteeCVByName() {
        mentee1.setCvText("Experienced Java Developer");
        when(repo.selectById("mentee1")).thenReturn(Optional.of(mentee1));
        String result = controller.getMenteeCVByName("mentee1");
        assertEquals("Experienced Java Developer", result);
    }

    @Test
    public void testGetMenteeCVByNameNotFound() {
        when(repo.selectById("mentee1")).thenReturn(Optional.empty());
        String result = controller.getMenteeCVByName("mentee1");
        assertNull(result);
    }
}
