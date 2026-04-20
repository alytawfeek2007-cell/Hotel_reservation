import java.time.LocalDate;
import java.util.List;

public abstract class Staff {
    private String Username;
    private String Password;
    private LocalDate dateOfBirth;
    private Role role;
    public enum Role {
     Admin,
      Receptionist
    }
     private int workingHours;

    
    public Staff(String username, String password, LocalDate dateOfBirth, Staff.Role role, int workingHours) {
        Username = username;
        Password = password;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.workingHours = workingHours;
    }
   
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
            System.out.println(" Guest: " + res.getGuest().getUsername() +"  Room: " + res.getRoom().getRoomNumber() +" Check-in: " + res.getCheckInDate() +" Check-out: " + res.getCheckOutDate() +" Status: " + res.getStatus());
    }

}



}

