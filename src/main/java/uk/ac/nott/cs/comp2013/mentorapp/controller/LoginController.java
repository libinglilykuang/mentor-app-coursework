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
    /*if (user.isEmpty()) {
      return false;
    }

    User u = user.get();
    return u.getUsername().equals(username) && u.getPassword().equals(password);
  }*/
    if (user.isPresent()) {
      User u = user.get();
      if (u.getPassword().equals(password)) {
        currentUser = u;
        System.out.println("Login successful for user: " + username);
        return true;
      }else {
        System.out.println("Incorrect password for user: " + username);
      }
    }else {
      System.out.println("User not found: " + username);
    }

    currentUser = null;
    return false;
  }

  public User getCurrentUser() {
    return currentUser;
  }
}
