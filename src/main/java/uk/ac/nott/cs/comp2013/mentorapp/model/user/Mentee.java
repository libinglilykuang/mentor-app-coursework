package uk.ac.nott.cs.comp2013.mentorapp.model.user;

import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;

public class Mentee implements User, HasCv {

  private UserRole role = UserRole.MENTEE;
  private String username, password, cvText;
  private Mentor assignedMentor;
  private SupportType supportRequest;
  private MentorType preferredMentorType;
  private String realName;
  private MenteeType menteeType;

  public Mentee(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public Mentee(String username, String password, String realName) {
    this.username = username;
    this.password = password;
    this.realName = realName;
  }

  /*public Mentee(String username, String password, String cvText) {
    this.username = username;
    this.password = password;
    this.cvText = cvText;
  }*/

  public Mentee(String username, String password, String cvText,SupportType supportType) {
    this.username = username;
    this.password = password;
    this.cvText = cvText;
    this.supportRequest=supportType;
  }

  public Mentee(String username, String password, String cvText,SupportType supportType,MentorType preferredMentorType,MenteeType menteeType) {
    this.username = username;
    this.password = password;
    this.cvText = cvText;
    this.supportRequest=supportType;
    this.preferredMentorType = preferredMentorType;
    this.menteeType=menteeType;
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

  public SupportType getSupportRequest() {
    return supportRequest;
  }

  public void setSupportRequest(SupportType supportRequest) {
    this.supportRequest = supportRequest;
  }

  public MentorType getPreferredMentorType() {
    return preferredMentorType;
  }

  public void setPreferredMentorType(MentorType preferredMentorType) {
    this.preferredMentorType = preferredMentorType;
  }

  public String getRealName() {
    return realName;
  }

  public void setRealName(String realName) {
    this.realName = realName;
  }

  public MenteeType getMenteeType() {
    return menteeType;
  }

  public void setMenteeType(MenteeType menteeType) {
    this.menteeType = menteeType;
  }

  @Override
  public String toString() {
    return "Mentee{" +
            "username='" + getUsername() + '\'' +
            ", realName='" + (realName != null ? realName : "Not Set") + '\'' +
            '}';
  }
}
