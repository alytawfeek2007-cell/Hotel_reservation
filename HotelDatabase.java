
import java.util.ArrayList;
import java.time.LocalDate;

public class HotelDatabase {
    private static ArrayList<Guest> guests = new ArrayList<>();
    private static ArrayList<Room> rooms = new ArrayList<>();
    private static ArrayList<Reservation> reservations = new ArrayList<>();
    private static ArrayList<Invoice> invoices = new ArrayList<>();

    static {
        prePopulate();
    }

    // --- CRUD Methods with Exceptions ---
    public static void addGuest(Guest guest) throws InvalidUserDataException {
        if (guest == null || guest.getUsername() == null || guest.getUsername().isEmpty()) {
            throw new InvalidUserDataException("Guest username cannot be empty.");
        }
        if (guest.getBalance() < 0) {
            throw new InvalidUserDataException("Guest balance cannot be negative.");
        }
        guests.add(guest);
    }

    public static ArrayList<Guest> getGuests() {
        return new ArrayList<>(guests);
    }

    public static void addRoom(Room room) throws InvalidUserDataException {
        if (room == null) {
            throw new InvalidUserDataException("Room cannot be null.");
        }
        rooms.add(room);
    }

    public static ArrayList<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public static void addReservation(Reservation reservation) throws RoomNotAvailableException {
        if (reservation == null) {
            throw new RoomNotAvailableException("Reservation cannot be null.");
        }
        // Check if room is already reserved for overlapping dates
        for (Reservation existing : reservations) {
            if (existing.getRoom().equals(reservation.getRoom()) &&
                existing.getStatus() == ReservationStatus.CONFIRMED &&
                !(reservation.getCheckOutDate().isBefore(existing.getCheckInDate()) ||
                  reservation.getCheckInDate().isAfter(existing.getCheckOutDate()))) {
                throw new RoomNotAvailableException("Room is not available for the selected dates.");
            }
        }
        reservations.add(reservation);
    }

    public static ArrayList<Reservation> getReservations() {
        return new ArrayList<>(reservations);
    }

    public static void addInvoice(Invoice invoice) throws InvalidPaymentException {
        if (invoice == null) {
            throw new InvalidPaymentException("Invoice cannot be null.");
        }
        if (invoice.getTotalAmount() < 0) {
            throw new InvalidPaymentException("Invoice amount cannot be negative.");
        }
        invoices.add(invoice);
    }

    public static ArrayList<Invoice> getInvoices() {
        return new ArrayList<>(invoices);
    }

    // --- Pre-population ---
    private static void prePopulate() {
        try {
            Guest g1 = new Guest("jdoe", "pass123", LocalDate.of(1990, 5, 15), 500.0, "123 Maple St", Gender.MALE, "High floor");
            Guest g2 = new Guest("asmith", "secure456", LocalDate.of(1985, 10, 20), 1200.0, "456 Oak Ave", Gender.FEMALE, "Near elevator");
            addGuest(g1);
            addGuest(g2);

            RoomType suiteType = new RoomType(4, "Luxury Suite", 250.0);
            RoomType singleType = new RoomType(1, "Single Cozy", 85.0);

            Room r101 = new Room(101, suiteType);
            Room r202 = new Room(202, singleType);
            addRoom(r101);
            addRoom(r202);

            Reservation res1 = new Reservation(g1, r101, LocalDate.now(), LocalDate.now().plusDays(3), ReservationStatus.CONFIRMED);
            addReservation(res1);

            Invoice inv1 = new Invoice(750.0);
            addInvoice(inv1);
        } catch (Exception e) {
            System.out.println("Pre-population error: " + e.getMessage());
        }
    }

    public static void setGuests(ArrayList<Guest> guests) {
        HotelDatabase.guests = guests;
    }

    public static void setRooms(ArrayList<Room> rooms) {
        HotelDatabase.rooms = rooms;
    }

    public static void setReservations(ArrayList<Reservation> reservations) {
        HotelDatabase.reservations = reservations;
    }

    public static void setInvoices(ArrayList<Invoice> invoices) {
        HotelDatabase.invoices = invoices;
    }
}
