package uk.ac.nott.cs.comp2013.mentorapp.model.user;

public class Administrator implements User {

  private UserRole role = UserRole.ADMIN;
  private String username, password;

  public Administrator(String username, String password) {
    this.username=username;
    this.password=password;
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
}
