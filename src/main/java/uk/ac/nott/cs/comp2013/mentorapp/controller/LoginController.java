package uk.ac.nott.cs.comp2013.mentorapp.controller;

import java.util.Optional;
import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;

public class LoginController {
  private final Repository<User, String> repo;
  private User currentUser;

  public LoginController(Repository<User, String> model) {
    this.repo = model;
  }

  public boolean onLoginClick(String username, String password) {
    Optional<User> user = repo.selectById(username);
    if (user.isPresent()) {
      User u = user.get();
      if (u.getPassword().equals(password)) {
        currentUser = u;
        return true;
      }
    }
    currentUser = null;
    return false;
  }

  public User getCurrentUser() {
    return currentUser;
  }
}
