import java.time.LocalDate;
import java.util.List;

public class Guest{
    private String username;
    private String password;
    private LocalDate dateOfBirth;
    private double balance;
    private String address;
    public enum Gender {
    MALE,
    FEMALE
}
    private Gender gender; 
    private List<String> roomPreferences;

}

