import java.time.LocalDate;

public abstract class Staff {
    private String Username;
    private String Password;
    private LocalDate dateOfBirth;
    private Role role;
    public enum Role {
     Admin,
      RECEPTIONIST
    }

    
    public Staff(String username, String password, LocalDate dateOfBirth, Staff.Role role, int workingHours) {
        Username = username;
        Password = password;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.workingHours = workingHours;
    }
    private int workingHours;
    public String getUsername() {
        return Username;
    }
    public void setUsername(String username) {
        Username = username;
    }
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    
    public int getWorkingHours() {
        return workingHours;
    }
    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }
}
