package uk.ac.nott.cs.comp2013.mentorapp.model.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdministratorTests {
    @Test
    public void testConstructor() {
        Administrator admin = new Administrator("admin1", "password1");
        assertEquals("admin1", admin.getUsername());
        assertEquals("password1", admin.getPassword());
        assertEquals(UserRole.ADMIN, admin.getRole());
    }

    @Test
    public void testSetAndGetUsername() {
        Administrator admin = new Administrator("admin1", "password1");
        admin.setUsername("newAdmin");
        assertEquals("newAdmin", admin.getUsername());
    }

    @Test
    public void testSetAndGetPassword() {
        Administrator admin = new Administrator("admin1", "password1");
        admin.setPassword("newPassword");
        assertEquals("newPassword", admin.getPassword());
    }

    @Test
    public void testSetAndGetRole() {
        Administrator admin = new Administrator("admin1", "password1");
        admin.setRole(UserRole.MENTOR);
        assertEquals(UserRole.MENTOR, admin.getRole());
    }
}
