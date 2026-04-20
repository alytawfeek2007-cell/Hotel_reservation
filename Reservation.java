import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {

    private Guest guest;              // Guest reference
    private Room room;                // Room reference
    private LocalDate checkInDate;    // Check-in date
    private LocalDate checkOutDate;   // Check-out date
    private ReservationStatus status; // Reservation status

    public Reservation(Guest guest, Room room,
                       LocalDate checkInDate, LocalDate checkOutDate) {
        setGuest(guest);
        setRoom(room);
        setCheckInDate(checkInDate);
        setCheckOutDate(checkOutDate);
        this.status = ReservationStatus.PENDING;
    }

    public Reservation(Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate, ReservationStatus status) {
        this(guest, room, checkInDate, checkOutDate);
        setStatus(status);
    }

    // Getters
    public Guest getGuest() { return guest; }
    public Room getRoom() { return room; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public ReservationStatus getStatus() { return status; }

    public void setGuest(Guest guest) {
        if (guest == null) {
            throw new IllegalArgumentException("Guest cannot be null.");
        }
        this.guest = guest;
    }

    public void setRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null.");
        }
        this.room = room;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        if (checkInDate == null) {
            throw new IllegalArgumentException("Check-in date cannot be null.");
        }
        if (this.checkOutDate != null && !this.checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        if (checkOutDate == null) {
            throw new IllegalArgumentException("Check-out date cannot be null.");
        }
        if (this.checkInDate != null && !checkOutDate.isAfter(this.checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        this.checkOutDate = checkOutDate;
    }

    public void setStatus(ReservationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Reservation status cannot be null.");
        }
        this.status = status;
    }

    public long getNumberOfNights() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public double calculateTotalPrice() {
        return room.getTotalPricePerNight() * getNumberOfNights();
    }
}
