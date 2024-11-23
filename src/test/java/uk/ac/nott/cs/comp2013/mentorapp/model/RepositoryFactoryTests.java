package uk.ac.nott.cs.comp2013.mentorapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.*;
import java.io.IOException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class RepositoryFactoryTests {
    private Repository<User, String> repo;

    @BeforeEach
    public void setUp() throws IOException {
        var resource = getClass().getResource("/TEST_DATA.csv");
        assertNotNull(resource);
        String filePath = resource.getPath();
        repo = RepositoryFactory.createRepositoryFromCsv(filePath);
    }

    @Test
    public void testRepositoryCsvMissing() {
        assertThrows(IOException.class, () -> RepositoryFactory.createRepositoryFromCsv("/MISSING_DATA.csv"));
    }

    @Test
    public void testMenteeParsedCorrectly() {
        Optional<User> user = repo.selectById("labarough0");
        assertTrue(user.isPresent());
        assertInstanceOf(Mentee.class, user.get());
    }

    @Test
    public void testMentorParsedCorrectly() {
        Optional<User> user = repo.selectById("mentor1");
        assertTrue(user.isPresent());
        assertInstanceOf(Mentor.class, user.get());
        Mentor mentor = (Mentor) user.get();
        assertEquals("mentor1", mentor.getUsername());
        assertEquals("password2", mentor.getPassword());
        assertNotNull(mentor.getStartAvailability());
        assertNotNull(mentor.getEndAvailability());
        assertEquals(MentorType.TECH_PROFESSIONAL, mentor.getMentorType());
    }

    @Test
    public void testAdministratorParsedCorrectly() {
        Optional<User> user = repo.selectById("admin1");
        assertTrue(user.isPresent());
        assertInstanceOf(Administrator.class, user.get());
        Administrator admin = (Administrator) user.get();
        assertEquals("admin1", admin.getUsername());
        assertEquals("adminPassword", admin.getPassword());
    }

    @Test
    public void testHandlesInvalidRole() {
        Optional<User> user = repo.selectById("invalidRoleUser");
        assertFalse(user.isPresent());
    }

    @Test
    public void testHandlesInvalidDateRange(){
        Optional<User> user = repo.selectById("mentor2");
        assertTrue(user.isPresent());
        assertInstanceOf(Mentor.class, user.get());
        Mentor mentor = (Mentor) user.get();
        assertNull(mentor.getStartAvailability());
        assertNull(mentor.getEndAvailability());
    }

    @Test
    public void testRepositoryContainsValidUsers() {
        assertNotNull(repo);
        assertTrue(repo.selectById("labarough0").isPresent());
        assertTrue(repo.selectById("mentor1").isPresent());
        assertTrue(repo.selectById("admin1").isPresent());
        assertFalse(repo.selectById("invalidUser").isPresent());
    }
}
