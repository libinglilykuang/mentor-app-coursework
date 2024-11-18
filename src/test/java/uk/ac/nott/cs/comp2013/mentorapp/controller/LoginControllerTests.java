package uk.ac.nott.cs.comp2013.mentorapp.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;

public class LoginControllerTests {
  private Repository<User, String> model;
  private LoginController controller;

  @BeforeEach
  public void setUp() {
    model = mock();
    controller = new LoginController(model);
  }

  @Test
  public void testLoginClickMissingUser() {
    when(model.selectById("user")).thenReturn(Optional.empty());
    boolean loginResult = controller.onLoginClick("user", "password");
    assertFalse(loginResult);
    assertNull(controller.getCurrentUser());
  }

  @Test
  public void testLoginClickBadPassword() {
    when(model.selectById("user")).thenReturn(Optional.of(new Mentee("user", "password")));
    boolean loginResult = controller.onLoginClick("user", "logmein");
    assertFalse(loginResult);
    assertNull(controller.getCurrentUser());
  }

  @Test
  public void testLoginClickGood() {
    Mentee user = new Mentee("user", "password");
    when(model.selectById("user")).thenReturn(Optional.of(user));
    boolean loginResult = controller.onLoginClick("user", "password");
    assertTrue(loginResult);
    assertEquals(user, controller.getCurrentUser());
  }

  @Test
  public void testGetCurrentUserWithoutLogin() {
    assertNull(controller.getCurrentUser());
  }

  @Test
  public void testGetCurrentUserAfterFailedLogin() {
    when(model.selectById("user")).thenReturn(Optional.empty());
    controller.onLoginClick("user", "password");
    assertNull(controller.getCurrentUser());
  }

  @Test
  public void testMultipleLogins() {
    Mentee user1 = new Mentee("user1", "password1");
    Mentee user2 = new Mentee("user2", "password2");

    when(model.selectById("user1")).thenReturn(Optional.of(user1));
    when(model.selectById("user2")).thenReturn(Optional.of(user2));

    assertTrue(controller.onLoginClick("user1", "password1"));
    assertEquals(user1, controller.getCurrentUser());

    assertTrue(controller.onLoginClick("user2", "password2"));
    assertEquals(user2, controller.getCurrentUser());
  }
}
