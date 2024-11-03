package uk.ac.nott.cs.comp2013.mentorapp.model.user;

import static org.junit.Assume.assumeFalse;
import static org.junit.jupiter.api.Assertions.*;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import java.time.LocalDateTime;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@RunWith(JUnitQuickcheck.class)
public class MentorTests {
  private Mentor mentor;

  @BeforeEach
  public void setUp() {
    mentor = new Mentor("testUser", "testPassword",
            LocalDateTime.of(2023, 1, 1, 9, 0),
            LocalDateTime.of(2024, 1, 1, 17, 0));
  }

  @Test
  public void testConstructorWithoutAvailability() {
    Mentor mentor = new Mentor("testUser", "testPassword");
    assertEquals("testUser", mentor.getUsername());
    assertEquals("testPassword", mentor.getPassword());
    assertNull(mentor.getStartAvailability());
    assertNull(mentor.getEndAvailability());
  }

  @Property
  public void testConstructor(String username, String password, LocalDateTime start,
      LocalDateTime end) {
    Mentor m = new Mentor(username, password, start, end);
    assertEquals(username, m.getUsername());
    assertEquals(password, m.getPassword());
    assertEquals(start, m.getStartAvailability());
    assertEquals(end, m.getEndAvailability());
  }

  @Property
  public void testEquals_Succeed(String username) {
    Mentor m1 = new Mentor(username, "password");
    Mentor m2 = new Mentor(username, "password");
    assertEquals(m1, m2);
  }

  @Property
  public void testEquals_Fail(String username1, String username2) {
    assumeFalse(username1.equals(username2));
    Mentor m1 = new Mentor(username1, "password");
    Mentor m2 = new Mentor(username2, "password");
    assertNotEquals(m1, m2);
  }

  @Test
  public void testSetAndGetStartAvailability() {
    LocalDateTime newStart = LocalDateTime.of(2023, 1, 2, 9, 0);
    mentor.setStartAvailability(newStart);
    assertEquals(newStart, mentor.getStartAvailability());
  }

  @Test
  public void testSetAndGetEndAvailability() {
    LocalDateTime newEnd = LocalDateTime.of(2023, 1, 2, 17, 0);
    mentor.setEndAvailability(newEnd);
    assertEquals(newEnd, mentor.getEndAvailability());
  }

  @Test
  public void testSetEndAvailabilityBeforeStart() {
    LocalDateTime start = LocalDateTime.of(2024, 1, 2, 17, 0);
    LocalDateTime end = LocalDateTime.of(2023, 1, 2, 9, 0);
    mentor.setStartAvailability(start);

    assertThrows(IllegalArgumentException.class, () -> mentor.setEndAvailability(end));
  }

  @Test
  public void testSetAndGetUsername() {
    mentor.setUsername("newUser");
    assertEquals("newUser", mentor.getUsername());
  }

  @Test
  public void testSetAndGetPassword() {
    mentor.setPassword("newPassword");
    assertEquals("newPassword", mentor.getPassword());
  }

  @Test
  public void testGetRole() {
    assertEquals(UserRole.MENTOR, mentor.getRole());
  }

  @Test
  public void testSetRole() {
    mentor.setRole(UserRole.ADMIN); //change the role
    assertEquals(UserRole.ADMIN, mentor.getRole());
  }

  @Test
  public void testHashCodeConsistency() {
    Mentor m1 = new Mentor("testUser", "testPassword");
    Mentor m2 = new Mentor("testUser", "testPassword");
    assertEquals(m1.hashCode(), m2.hashCode());
  }
}
