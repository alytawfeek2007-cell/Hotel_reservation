import java.time.LocalDate;

public class Receptionist extends Staff{
    public Receptionist(String username,String Password,LocalDate dateOfBirth, int workingHours){
        super(username, Password, dateOfBirth, Role.RECEPTIONIST, workingHours);
    }

    public void checkIn(Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate) 
                    throws RoomNotAvailableException {
        Reservation res = new Reservation(guest, room, checkInDate, checkOutDate);
        guest.makeReservation(res);
        System.out.println("Guest " + guest.getUsername() + " checked in to room " + room.getRoomNumber());
}

    public void checkOut(Guest guest, Room room, LocalDate checkOutDate) {
        room.setAvailable(true);
        System.out.println("Guest " + guest.getUsername() +
                " checked out of room " + room.getRoomNumber() +
                " on " + checkOutDate);
    }
}

