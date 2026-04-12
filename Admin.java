import java.time.LocalDate;

public class Admin extends Staff {

    public Admin(String username,String Password,LocalDate dateOfBirth, Role role, int workingHours){
        super(username, Password, dateOfBirth, role, workingHours);
    }
}
