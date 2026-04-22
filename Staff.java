import java.time.LocalDate;
import java.util.List;

public abstract class Staff {
    private String Username;
    private String Password;
    private LocalDate dateOfBirth;
    private Role role;
    public enum Role {
    ADMIN,
      RECEPTIONIST
    }
     private int workingHours;

    
    public Staff(String username, String password, LocalDate dateOfBirth, Staff.Role role, int workingHours) {
        this.Username = validateUsername(username);
        this.Password = validatePassword(password);
        this.dateOfBirth = validateDateOfBirth(dateOfBirth);
        this.role = validateRole(role);
        this.workingHours = validateWorkingHours(workingHours);
    }
   
    public String getUsername() {
        return Username;
    }
    public void setUsername(String username) {
        Username = validateUsername(username);
    }
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = validatePassword(password);
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = validateDateOfBirth(dateOfBirth);
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = validateRole(role);
    }
    
    public int getWorkingHours() {
        return workingHours;
    }
    public void setWorkingHours(int workingHours) {
        this.workingHours = validateWorkingHours(workingHours);
    }

    private String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        return username;
    }

    private String validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }
        return password;
    }

    private LocalDate validateDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth is invalid.");
        }
        return dateOfBirth;
    }

    private Role validateRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null.");
        }
        return role;
    }

    private int validateWorkingHours(int workingHours) {
        if (workingHours < 0) {
            throw new IllegalArgumentException("Working hours cannot be negative.");
        }
        return workingHours;
    }

    public void viewAllGuests(List<Guest> guests) {
        if (guests == null || guests.isEmpty()) {
            System.out.println("No guests found.");
        } else {
            System.out.println("All guests:");
            for (Guest g : guests)
                System.out.println(" - " + g.getUsername());
        }
    }

    public void viewAllRooms(List<Room> rooms) {
        if (rooms == null || rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            System.out.println("All rooms:");
            for (Room r : rooms)
                System.out.println(" - Room " + r.getRoomNumber());
        }
    }

    public void viewAllReservations(List<Reservation> reservations) {
    if (reservations == null || reservations.isEmpty()) {
        System.out.println("No reservations found.");
    } else {
         System.out.println("All reservations:");
            for (Reservation res : reservations)
            System.out.println(res);
    }

}



}

