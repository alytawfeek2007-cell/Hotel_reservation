package hotel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Guest{

    public enum Gender {
        MALE,
        FEMALE
    }

    private String username;
    private String password;
    private LocalDate dateOfBirth;
    private double balance;
    private String address;
    private Gender gender; 
    private List<String> roomPreferences;
    private List<Reservation> reservations = new ArrayList<>();

    public Guest() {
        this.roomPreferences = new ArrayList<>();
    }

    public Guest(String username, String password, LocalDate dateOfBirthParam, double balance, String address, Gender gender)  {
       if (username == null || username.trim().isEmpty())
            throw new IllegalArgumentException("Username cannot be empty or null.");
        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        this.username = username;  
        this.password = password;
        this.dateOfBirth = dateOfBirthParam;
        this.balance = balance;
        this.address = address;
        this.gender = gender;
        this.roomPreferences = new ArrayList<>();
    }

    public Guest(String username, String password, LocalDate dateOfBirthParam, double balance, String address, Gender gender,String roomPreference)  {
        this(username, password, dateOfBirthParam, balance, address, gender);
        this.roomPreferences.add(roomPreference);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
        throw new IllegalArgumentException("Username cannot be empty or null.");
    }
    this.username = username;
}
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        if (password != null && password.length() >= 6) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }  
    }
    
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
   
    public void setGender(Gender gender) {
         this.gender = gender; 
        }
    public Gender getGender() {
         return gender; 
        }
    
    public List<String> getRoomPreferences() {
        return roomPreferences;
    }
    public void setRoomPreferences(List<String> roomPreferences) {
        this.roomPreferences = roomPreferences;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void register() {
        if (username == null || username.trim().isEmpty()) {
        System.out.println("Registration failed: username is missing.");
        return;
        }
        if (password == null || password.length() < 6) {
        System.out.println("Registration failed: password too short.");
        return;
        }
        System.out.println("Guest " + username + " registered successfully.");
    }
    
    public boolean login(String username, String password){
        if (username == null || password == null) {
            System.out.println("Login failed: username or password is null.");
            return false;
        }
        boolean checking = this.username.equals(username) && this.password.equals(password);
        if (checking) {
            System.out.println("Login successful. Welcome, " + this.username + "!");
        } else {
            System.out.println("Login failed: incorrect username or password.");
        }
        return checking;
    }
    
    public void viewAvailableRooms(List<Room> availableRooms) {
        if (availableRooms == null || availableRooms.isEmpty()) {
        System.out.println("No rooms are currently available.");
        } else {
            System.out.println("Available rooms:");
        for (Room room : availableRooms) {
            System.out.println("Room number: " + room);
        }
    }
    }

   public void makeReservation(Reservation reservation) {
    if (reservation.getStatus() == ReservationStatus.PENDING) {
        reservation.confirm(); 
    }
    if (!reservations.contains(reservation)) {
        reservations.add(reservation); 
    }
    System.out.println("Reservation made for room " + 
                       reservation.getRoom().getRoomNumber());
}

     public void viewReservations() {
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("You have no current reservations.");
        } else {
            System.out.println("Your reservations:");
            for (Reservation res : reservations) {
                System.out.println("Reservation available: " + res);
            }
        }
    }

    public void cancelReservation(Reservation reservation) {
    if (reservations.remove(reservation)) {
        reservation.cancel();
        
    } else {
        System.out.println("Reservation " + reservation + " not found.");
    }
}

public void checkoutAndPay(Reservation reservation, PaymentMethod method) {
    if (!reservations.contains(reservation)) {
        System.out.println("This reservation does not belong to you.");
        return;
    }
    Invoice inv = reservation.getInvoice();
    if (inv == null) {
        System.out.println("No invoice found for this reservation.");
        return;
    }
    if (inv.isFullyPaid()) {
        System.out.println("Invoice already fully paid.");
        return;
    }
    double remaining = inv.getRemainingAmount();
    if (this.balance >= remaining) {
        this.balance -= remaining;
        inv.pay(remaining, method);
        System.out.println("Checkout complete. Remaining balance: $" + this.balance);
    } else {
        System.out.println("Insufficient balance. Need $" + remaining + " but have $" + this.balance);
    }
}
    @Override
public String toString() {
    return "Guest{" +
            "username='" + username + '\'' +
            ", dateOfBirth=" + dateOfBirth +
            ", balance=" + balance +
            ", address='" + address + '\'' +
            ", gender=" + gender +
            ", roomPreferences=" + roomPreferences +
            '}';
}
}