package uk.ac.nott.cs.comp2013.mentorapp.model.user;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mentor implements User, HasAvailability {
  private UserRole role = UserRole.MENTOR;
  private String username, password;
  private LocalDateTime startAvailability, endAvailability;
  private Mentee assignedMentee;
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  public Mentor(String username, String password) {
    this(username, password, null, null);
  }

  public Mentor(String username, String password, LocalDateTime startAvailability,
      LocalDateTime endAvailability) {
    this.username = username;
    this.password = password;
    this.startAvailability = startAvailability;
    this.endAvailability = endAvailability;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public LocalDateTime getStartAvailability() {
    return startAvailability;
  }

  @Override
  public void setStartAvailability(LocalDateTime date) {
    this.startAvailability = date;
  }

  @Override
  public LocalDateTime getEndAvailability() {
    return endAvailability;
  }

  @Override
  public void setEndAvailability(LocalDateTime date) {
    if (this.startAvailability != null && date.isBefore(this.startAvailability)) {
      throw new IllegalArgumentException("End availability cannot be earlier than start availability.");
    }
    this.endAvailability = date;
  }

  @Override
  public UserRole getRole() {
    return role;
  }

  @Override
  public void setRole(UserRole role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Mentor m) {
      return username.equals(m.username);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return username.hashCode();
  }

  public Mentee getAssignedMentee() {
    return assignedMentee;
  }

  public boolean assignMentee(Mentee mentee) {
    if (isAvailable()) {
      this.assignedMentee = mentee;
      return true;
    } else {
      throw new IllegalStateException("This mentor is already assigned to a mentee for the current period.");
    }
  }

  public boolean isAvailable() {
    if (assignedMentee != null) {
      System.out.println("Mentor " + username + " is not available because they already have an assigned mentee.");
      return false;
    }

    if (startAvailability == null || endAvailability == null) {
      System.out.println("Mentor " + username + " is not available because availability times are not set.");
      return false;
    }

    LocalDateTime now = LocalDateTime.now();

    boolean withinAvailability = !now.isAfter(endAvailability);
    if (!withinAvailability) {
      return false;
    }

    return true;
  }

  @Override
  public String toString() {
    String availability = "Unavailable";
    if (startAvailability != null && endAvailability != null) {
      availability = startAvailability.format(formatter) + " - " + endAvailability.format(formatter);
    }
    return username + "       " + availability;
  }


}
