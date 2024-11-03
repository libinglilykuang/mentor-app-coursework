package uk.ac.nott.cs.comp2013.mentorapp.model.user;

import static org.junit.Assume.assumeFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitQuickcheck.class)
public class MenteeTests {
  private Mentee mentee;

  @BeforeEach
  public void setUp() {
    mentee = new Mentee("testUser", "testPassword");
  }

  @Property
  public void testConstructor(String username, String password) {
    Mentee m = new Mentee(username, password);
    assertEquals(username, m.getUsername());
    assertEquals(password, m.getPassword());
    assertEquals(0, m.getCvText().length());
  }

  @Property
  public void testEquals_Succeed(String username) {
    Mentee m1 = new Mentee(username, "password");
    Mentee m2 = new Mentee(username, "password");
    assertEquals(m1, m2);
  }

  @Property
  public void testEquals_Fail(String username1, String username2) {
    assumeFalse(username1.equals(username2));
    Mentee m1 = new Mentee(username1, "password");
    Mentee m2 = new Mentee(username2, "password");
    assertNotEquals(m1, m2);
  }

  @Test
  public void testSetAndGetCvText() {
    mentee.setCvText("Sample CV text");
    assertEquals("Sample CV text", mentee.getCvText());
  }

  @Test
  public void testSetAndGetUsername() {
    mentee.setUsername("newUser");
    assertEquals("newUser", mentee.getUsername());
  }

  @Test
  public void testSetAndGetPassword() {
    mentee.setPassword("newPassword");
    assertEquals("newPassword", mentee.getPassword());
  }

  @Test
  public void testGetRole() {
    assertEquals(UserRole.MENTEE, mentee.getRole());
  }

  @Test
  public void testSetRole() {
    mentee.setRole(UserRole.MENTOR);
    assertEquals(UserRole.MENTOR, mentee.getRole());
  }

  @Test
  public void testHashCodeConsistency() {
    Mentee m1 = new Mentee("testUser", "testPassword");
    Mentee m2 = new Mentee("testUser", "testPassword");
    assertEquals(m1.hashCode(), m2.hashCode());
  }
}
