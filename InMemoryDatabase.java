public class InMemoryDatabase {
    import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class HotelDatabase {
    public static ArrayList<Guest> guests = new ArrayList<>();
    public static ArrayList<Room> rooms = new ArrayList<>();
    public static ArrayList<Reservation> reservations = new ArrayList<>();
    public static ArrayList<Invoice> invoices = new ArrayList<>();

    static {
        prePopulate();
    }

    private static void prePopulate() {
        Guest g1 = new Guest("jdoe", "pass123", LocalDate.of(1990, 5, 15), 500.0, "123 Maple St", Gender.MALE, "High floor");
        Guest g2 = new Guest("asmith", "secure456", LocalDate.of(1985, 10, 20), 1200.0, "456 Oak Ave", Gender.FEMALE, "Near elevator");
        guests.add(g1);
        guests.add(g2);

        RoomType suiteType = new RoomType(4, "Luxury Suite", 250.0);
        RoomType singleType = new RoomType(1, "Single Cozy", 85.0);
        
        Room r101 = new Room(101, suiteType);
        Room r202 = new Room(202, singleType);
        rooms.add(r101);
        rooms.add(r202);

        Reservation res1 = new Reservation(g1, r101, LocalDate.now(), LocalDate.now().plusDays(3), ReservationStatus.CONFIRMED);
        reservations.add(res1);

        Invoice inv1 = new Invoice(750.0); 
        invoices.add(inv1);
    }
}
}
