package uk.ac.nott.cs.comp2013.mentorapp.model.user;

public class UserProfile {
    private String username;
    private String password;
    private String realName;

    public UserProfile(String username, String password, String realName) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }
        this.username = username;
        this.password = password;
        this.realName = realName;
    }

    public UserProfile(String username, String password) {
        this(username, password, null);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "username='" + username + '\'' +
                ", realName='" + (realName != null ? realName : "Not Set") + '\'' +
                '}';
    }
}
