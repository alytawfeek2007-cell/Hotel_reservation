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
    private List<String> reservations = new ArrayList<>();

    public Guest() {
        this.roomPreferences = new ArrayList<>();
    }

    public Guest(String username, String password, LocalDate dateOfBirthParam, double balance, String address, Gender gender)  {
        this.setUsername(username);
        this.setPassword(password);
        this.setDateOfBirth(dateOfBirthParam);
        this.setBalance(balance);
        this.setAddress(address);
        this.setGender(gender);
        this.roomPreferences = new ArrayList<>();
    }

    public Guest(String username, String password, LocalDate dateOfBirthParam, double balance, String address, Gender gender,String roomPreference)  {

        this.setUsername(username);
        this.setPassword(password);
        this.setDateOfBirth(dateOfBirthParam);
        this.setBalance(balance);
        this.setAddress(address);
        this.setGender(gender);
        this.roomPreferences = new ArrayList<>();
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
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
        this.balance = balance;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty.");
        }
        this.address = address;
    }
   
    public void setGender(Gender gender) {
         if (gender == null) {
            throw new IllegalArgumentException("Gender cannot be null.");
        }
         this.gender = gender; 
        }
    public Gender getGender() {
         return gender; 
        }
    
    public List<String> getRoomPreferences() {
        return roomPreferences;
    }
    public void setRoomPreferences(List<String> roomPreferences) {
        if (roomPreferences == null) {
            throw new IllegalArgumentException("Room preferences list cannot be null.");
        }
        this.roomPreferences = roomPreferences;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth is invalid.");
        }
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
        try {
            HotelDatabase.addGuest(this);
            System.out.println("Guest " + username + " registered successfully.");
        } catch (InvalidUserDataException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
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
    
    public void viewAvailableRooms(List<String> availableRooms) {
        if (availableRooms == null || availableRooms.isEmpty()) {
        System.out.println("No rooms are currently available.");
        } else {
            System.out.println("Available rooms:");
        for (String room : availableRooms) {
            System.out.println("Room number: " + room);
        }
    }
    }

    public void makeReservation(String roomNumber, LocalDate checkIn, LocalDate checkOut) {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            System.out.println("Reservation failed: room number is invalid.");
            return;
        }
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            System.out.println("Reservation failed: date range is invalid.");
            return;
        }

        String reservationId = roomNumber + "_" + checkIn + "_" + checkOut;
        reservations.add(reservationId);
        System.out.println("Reservation made: " + reservationId);
    }

     public void viewReservations() {
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("You have no current reservations.");
        } else {
            System.out.println("Your reservations:");
            for (String res : reservations) {
                System.out.println("Reservation available: " + res);
            }
        }
    }

    public void cancelReservation(String reservationId) {
    if (reservations.remove(reservationId)) {
        System.out.println("Reservation " + reservationId + " has been cancelled.");
    } else {
        System.out.println("Reservation " + reservationId + " not found.");
    }
}

public void checkoutAndPay(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid payment amount.");
            return;
        }
        if (this.balance >= amount) {
            this.balance -= amount;
            System.out.println("Payment of " + amount + " successful. Remaining balance: " + this.balance);
        } else {
            System.out.println("Insufficient balance. Payment failed.");
        }
    }
}