package uk.ac.nott.cs.comp2013.mentorapp.controller;

import java.util.Optional;
import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;

/**
 * Handles user login and tracks the current logged-in user.
 */
public class LoginController {
  private final Repository<User, String> repo; // User data repository
  private User currentUser;

  /**
   * Initializes the controller with a user repository.
   *
   * @param model the user data repository
   */
  public LoginController(Repository<User, String> model) {
    this.repo = model;
  }

  /**
   * Attempts to log in with the given username and password.
   *
   * @param username the username to log in with
   * @param password the password to log in with
   * @return true if login succeeds, false otherwise
   */
  public boolean onLoginClick(String username, String password) {
    Optional<User> user = repo.selectById(username);
    if (user.isPresent() && user.get().getPassword().equals(password)) {
      currentUser = user.get();
      return true;
    }
    currentUser = null;
    return false;
  }

  /**
   * Gets the currently logged-in user.
   *
   * @return the current user or null if not logged in
   */
  public User getCurrentUser() {
    return currentUser;
  }
}
