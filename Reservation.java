import java.time.LocalDate;

public class Reservation {

    private Guest guest;              // Guest reference
    private Room room;                // Room reference
    private LocalDate checkInDate;    // Check-in date
    private LocalDate checkOutDate;   // Check-out date
    private ReservationStatus status; // Reservation status

    public Reservation(Guest guest, Room room,
                       LocalDate checkInDate, LocalDate checkOutDate) {

        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = ReservationStatus.PENDING;
    }

    // Getters
    public Guest getGuest() { return guest; }
    public Room getRoom() { return room; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public ReservationStatus getStatus() { return status; }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    @Override
        public String toString() {
            return "Reservation{" +
                "guest='" + guest.getUsername() + '\'' +
                " | room=" + room.getRoomNumber() +
                " | checkIn=" + checkInDate +
                " | checkOut=" + checkOutDate +
                " | status=" + status +
                '}';
        }
}
