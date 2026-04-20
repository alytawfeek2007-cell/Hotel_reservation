import java.time.LocalDate;

public class Receptionist extends Staff{
    public Receptionist(String username,String Password,LocalDate dateOfBirth, Role role, int workingHours){
        super(username, Password, dateOfBirth, role, workingHours);
    }

     public void checkIn(Guest guest, Room room, LocalDate checkInDate) {
        if (checkInDate == null) {
            System.out.println("Check-in failed: invalid date.");
            return;
        }
        if (!room.IsAvailable()) {
            System.out.println("Room " + room.getRoomNumber() + " is not available.");
            return;
        }
        room.setIsAvailable(false);
        System.out.println("Guest " + guest.getUsername() +" checked into room " + room.getRoomNumber() +" on " + checkInDate);


    }

    public void checkOut(Guest guest, Room room, LocalDate checkOutDate) {
        if (checkOutDate == null) {
            System.out.println("Check-out failed: invalid date.");
            return;
        }
        room.setIsAvailable(true);
        System.out.println("Guest " + guest.getUsername() +
                " checked out of room " + room.getRoomNumber() +
                " on " + checkOutDate);
    }
}
