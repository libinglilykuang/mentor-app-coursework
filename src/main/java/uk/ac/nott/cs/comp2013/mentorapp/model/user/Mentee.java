package uk.ac.nott.cs.comp2013.mentorapp.model.user;

public class Mentee implements User, HasCv {

  private UserRole role = UserRole.MENTEE;
  private String username, password, cvText;
  private Mentor assignedMentor;

  public Mentee(String username, String password) {
    this(username, password, "");
  }

  public Mentee(String username, String password, String cvText) {
    this.username = username;
    this.password = password;
    this.cvText = cvText;
  }
  @Override
  public String getCvText() {
    return cvText;
  }

  @Override
  public void setCvText(String text) {
    cvText = text;
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
  public UserRole getRole() {
    return role;
  }

  @Override
  public void setRole(UserRole role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Mentee m) {
      return username.equals(m.username);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return username.hashCode();
  }

  public Mentor getAssignedMentor() {
    return assignedMentor;
  }

  public void assignMentor(Mentor mentor) {
    this.assignedMentor = mentor;
  }

  @Override
  public String toString() {
    return username;
  }
}
