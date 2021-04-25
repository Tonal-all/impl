package xyz.tonalall.impl.entity;

public class Log {
    private String username;
    private String password;

    public Log() {
    }

    @Override
    public String toString() {
        return "Log{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Log(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
