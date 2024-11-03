package uk.ac.nott.cs.comp2013.mentorapp.model.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AdministratorTests {

    private Administrator admin;

    @BeforeEach
    public void setUp() {
        admin = new Administrator("adminUser", "adminPassword");
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals("adminUser", admin.getUsername());
        assertEquals("adminPassword", admin.getPassword());
        assertEquals(UserRole.ADMIN, admin.getRole());
    }

    @Test
    public void testSetAndGetUsername() {
        admin.setUsername("newAdminUser");
        assertEquals("newAdminUser", admin.getUsername());
    }

    @Test
    public void testSetAndGetPassword() {
        admin.setPassword("newAdminPassword");
        assertEquals("newAdminPassword", admin.getPassword());
    }

    @Test
    public void testSetAndGetRole() {
        admin.setRole(UserRole.MENTOR);
        assertEquals(UserRole.MENTOR, admin.getRole());
    }
}
