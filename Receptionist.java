import java.time.LocalDate;

public class Receptionist extends Staff{
    public Receptionist(String username,String Password,LocalDate dateOfBirth, Role role, int workingHours){
        super(username, Password, dateOfBirth, role, workingHours);
    }

     public void checkIn(Guest guest, Room room, LocalDate checkInDate) {
        if (!room.isAvailable()) {
            System.out.println("Room " + room.getRoomNumber() + " is not available.");
            return;
        }
        room.setAvailable(false);
        System.out.println("Guest " + guest.getUsername() +" checked into room " + room.getRoomNumber() +" on " + checkInDate);


    }

    public void checkOut(Guest guest, Room room, LocalDate checkOutDate) {
        room.setAvailable(true);
        System.out.println("Guest " + guest.getUsername() +
                " checked out of room " + room.getRoomNumber() +
                " on " + checkOutDate);
    }
}

