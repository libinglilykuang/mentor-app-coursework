package uk.ac.nott.cs.comp2013.mentorapp.model.user;

import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Mentor implements User, HasAvailability {
  private UserRole role = UserRole.MENTOR;
  private String username, password;
  private LocalDateTime startAvailability, endAvailability;
  private Mentee assignedMentee;
  private List<SupportSession> assignedSessions;
  private MentorType mentorType;
  private String realName;
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  public Mentor(String username, String password) {
    this.username = username;
    this.password = password;
    this.assignedSessions = new ArrayList<>();
  }

  public Mentor(String username, String password, String realName) {
    this.username = username;
    this.password = password;
    this.assignedSessions = new ArrayList<>();
    this.realName = realName;
  }

  public Mentor(String username, String password, LocalDateTime startAvailability,
                LocalDateTime endAvailability) {
    this.username = username;
    this.password = password;
    this.startAvailability = startAvailability;
    this.endAvailability = endAvailability;
  }

  public Mentor(String username, String password, LocalDateTime startAvailability,
      LocalDateTime endAvailability,MentorType mentorType) {
    this.username = username;
    this.password = password;
    this.startAvailability = startAvailability;
    this.endAvailability = endAvailability;
    this.mentorType = mentorType;
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
      return false;
    }
    if (startAvailability == null || endAvailability == null) {
      return false;
    }
    LocalDateTime now = LocalDateTime.now();
      return !now.isAfter(endAvailability);
  }

  public String getRealName() {
    return realName;
  }

  public void setRealName(String realName) {
    this.realName = realName;
  }
  public MentorType getMentorType() {
    return mentorType;
  }

  public void setAvailability(LocalDateTime start, LocalDateTime end) {
    if (start.isAfter(end)) {
      throw new IllegalArgumentException("Start availability cannot be later than end availability.");
    }
    this.startAvailability = start;
    this.endAvailability = end;
  }

  public List<SupportSession> getAssignedSessions() {
    return assignedSessions != null ? new ArrayList<>(assignedSessions) : new ArrayList<>();
  }

}
