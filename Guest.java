import java.time.LocalDate;
import java.util.List;

public class Guest{
    private String username;
    private String password;
    private LocalDate dateOfBirth;
    private double balance;
    private String address;
    private Gender gender; 
    private String roomPreferences;


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
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) throws InvalidUserDataException {
     if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
         throw new InvalidUserDataException("Date of birth cannot be in the future.");
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
    public enum Gender {
    MALE,
    FEMALE
}
    
    

    public void setgender(Gender gender){
        this.gender=gender;
    }
    public Gender getgender(){
        return gender;
    }
    
    public String getroomPreferences() {
        return roomPreferences;
    }
    public void setroomPreferences(String roomPreferences) {
        this.roomPreferences = roomPreferences;
    }

    
}

