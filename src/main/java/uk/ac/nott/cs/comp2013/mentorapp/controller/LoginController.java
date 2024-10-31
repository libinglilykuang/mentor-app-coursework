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
    Optional<User> userOptional = repo.selectById(username);
    if (userOptional.isEmpty()) {
      System.out.println("User not found: " + username);
      return false;
    }
    User u = userOptional.get();

    if (u.getPassword().equals(password)) {
      currentUser = u;
      return true;
    } else {
      System.out.println("Invalid password for user: " + username);
      return false;
    }
  }

  public User getCurrentUser() {
    return currentUser;
  }
}

