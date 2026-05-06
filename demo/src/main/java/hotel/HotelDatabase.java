package hotel;

import java.time.LocalDate;
import java.util.ArrayList;

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
        // ── Guests ───────────────────────────────────────────
        Guest g1 = new Guest("menna", "Menna@2025",
            LocalDate.of(2003, 3, 15), 5000.0,
            "10 Nile St, Cairo", Guest.Gender.FEMALE,
            "Quiet room");
        Guest g2 = new Guest("ali", "Ali#2025!",
            LocalDate.of(2003, 7, 22), 4000.0,
            "25 Tahrir Sq, Cairo", Guest.Gender.MALE,
            "High floor");
        Guest g3 = new Guest("jasmine", "Jas$mine9",
            LocalDate.of(2003, 1, 10), 3500.0,
            "7 Garden City, Cairo", Guest.Gender.FEMALE,
            "Sea view");
        Guest g4 = new Guest("sama", "Sama@321!",
            LocalDate.of(2003, 5, 18), 3000.0,
            "14 Maadi, Cairo", Guest.Gender.FEMALE,
            "Near elevator");
        Guest g5 = new Guest("aliTawfik", "ATawfik#9",
            LocalDate.of(2002, 11, 5), 6000.0,
            "33 Heliopolis, Cairo", Guest.Gender.MALE,
            "Top floor");
        Guest g6 = new Guest("jasmineAhmed", "JAhmed@7!",
            LocalDate.of(2003, 8, 25), 2500.0,
            "5 Dokki, Giza", Guest.Gender.FEMALE,
            "Balcony view");
        Guest g7 = new Guest("samaAyman", "SAyman#4!",
            LocalDate.of(2002, 6, 30), 2000.0,
            "19 6th October, Giza", Guest.Gender.FEMALE,
            "Ground floor");
        Guest g8 = new Guest("mennaKhaled", "MKhaled@8",
            LocalDate.of(2003, 2, 14), 4500.0,
            "8 Nasr City, Cairo", Guest.Gender.FEMALE,
            "Quiet room");
        Guest g9 = new Guest("ENG", "Eng!2025#",
            LocalDate.of(2001, 9, 1), 7000.0,
            "Ain Shams University", Guest.Gender.MALE,
            "Suite only");
        Guest g10 = new Guest("MAZEN", "Mazen@99!",
            LocalDate.of(2002, 4, 20), 5500.0,
            "12 New Cairo", Guest.Gender.MALE,
            "Penthouse");

        addGuest(g1); addGuest(g2); addGuest(g3);
        addGuest(g4); addGuest(g5); addGuest(g6);
        addGuest(g7); addGuest(g8); addGuest(g9);
        addGuest(g10);

        // ── Room Types ───────────────────────────────────────
        RoomType economy   = new RoomType(1, "Economy",    50.0);
        RoomType standard  = new RoomType(1, "Standard",   80.0);
        RoomType deluxe    = new RoomType(2, "Deluxe",    150.0);
        RoomType suite     = new RoomType(2, "Suite",     250.0);
        RoomType family    = new RoomType(4, "Family",    200.0);
        RoomType penthouse = new RoomType(4, "Penthouse", 500.0);

        // ── Rooms ────────────────────────────────────────────
        Room r101 = new Room("101", economy);
        Room r102 = new Room("102", economy);
        Room r103 = new Room("103", economy);
        Room r201 = new Room("201", standard);
        Room r202 = new Room("202", standard);
        Room r203 = new Room("203", standard);
        Room r204 = new Room("204", standard);
        Room r301 = new Room("301", deluxe);
        Room r302 = new Room("302", deluxe);
        Room r303 = new Room("303", deluxe);
        Room r401 = new Room("401", suite);
        Room r402 = new Room("402", suite);
        Room r501 = new Room("501", family);
        Room r502 = new Room("502", family);
        Room r601 = new Room("601", penthouse);
        Room r602 = new Room("602", penthouse);

        addRoom(r101); addRoom(r102); addRoom(r103);
        addRoom(r201); addRoom(r202);
        addRoom(r203); addRoom(r204);
        addRoom(r301); addRoom(r302); addRoom(r303);
        addRoom(r401); addRoom(r402);
        addRoom(r501); addRoom(r502);
        addRoom(r601); addRoom(r602);

        // ── Amenities ────────────────────────────────────────
        try {
            r301.addAmenity(new Amenity("WiFi",
                "High speed internet", 10.0));
            r301.addAmenity(new Amenity("TV",
                "Smart TV", 5.0));
            r302.addAmenity(new Amenity("WiFi",
                "High speed internet", 10.0));
            r302.addAmenity(new Amenity("Mini-bar",
                "Stocked mini-bar", 20.0));
            r401.addAmenity(new Amenity("WiFi",
                "High speed internet", 10.0));
            r401.addAmenity(new Amenity("TV",
                "Smart TV", 5.0));
            r401.addAmenity(new Amenity("Mini-bar",
                "Stocked mini-bar", 20.0));
            r401.addAmenity(new Amenity("Jacuzzi",
                "Private jacuzzi", 50.0));
            r601.addAmenity(new Amenity("WiFi",
                "High speed internet", 10.0));
            r601.addAmenity(new Amenity("TV",
                "85 inch Smart TV", 15.0));
            r601.addAmenity(new Amenity("Mini-bar",
                "Premium mini-bar", 30.0));
            r601.addAmenity(new Amenity("Jacuzzi",
                "Private jacuzzi", 50.0));
            r601.addAmenity(new Amenity("Butler",
                "Personal butler service", 100.0));
        } catch (InvalidPaymentException e) {
            System.out.println("Amenity error: "
                + e.getMessage());
        }

        // ── Sample reservations ───────────────────────────────
        Reservation res1 = new Reservation(g1, r201,
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(4));
        g1.makeReservation(res1);
        addReservation(res1);

        Reservation res2 = new Reservation(g5, r401,
            LocalDate.now().plusDays(2),
            LocalDate.now().plusDays(6));
        g5.makeReservation(res2);
        addReservation(res2);

    } catch (Exception e) {
        System.out.println(
            "Pre-population error: " + e.getMessage());
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
