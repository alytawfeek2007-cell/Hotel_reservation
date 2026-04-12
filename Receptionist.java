import java.time.LocalDate;

public class Receptionist extends Staff{
    public Receptionist(String username,String Password,LocalDate dateOfBirth, Role role, int workingHours){
        super(username, Password, dateOfBirth, role, workingHours);
    }
}
