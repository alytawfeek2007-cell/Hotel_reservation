
        import java.time.LocalDate;
        import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("=================================================");
        System.out.println("       HOTEL RESERVATION SYSTEM - TEST           ");
        System.out.println("=================================================\n");

        // ─── 1. DATABASE LOADS ────────────────────────────────────
        System.out.println("--- 1. Database Pre-population ---");
        List<Guest> guests = HotelDatabase.getGuests();
        List<Room>  rooms  = HotelDatabase.getRooms();
        System.out.println("Guests loaded: " + guests.size());
        System.out.println("Rooms loaded:  " + rooms.size());
        System.out.println();

        // ─── 2. GUEST LOGIN ───────────────────────────────────────
        System.out.println("--- 2. Guest Login ---");
        Guest g1 = guests.get(0);
        Guest g2 = guests.get(1);
        g1.login("jdoe", "pass123");          // should succeed
        g1.login("jdoe", "wrongpass");        // should fail
        g2.login("asmith", "secure456");      // should succeed
        System.out.println();

        // ─── 3. GUEST REGISTER ────────────────────────────────────
        System.out.println("--- 3. Guest Register ---");
        g1.register();
        System.out.println();

        // ─── 4. VIEW AVAILABLE ROOMS ──────────────────────────────
        System.out.println("--- 4. View Available Rooms ---");
        g1.viewAvailableRooms(rooms);
        System.out.println();

        // ─── 5. MAKE RESERVATION ──────────────────────────────────
        System.out.println("--- 5. Make Reservation ---");
        Room r202 = rooms.get(1);
        Reservation res2 = new Reservation(g2, r202,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5));
        g2.makeReservation(res2);
        System.out.println();

        // ─── 6. VIEW RESERVATIONS ─────────────────────────────────
        System.out.println("--- 6. View Reservations ---");
        g1.viewReservations();
        g2.viewReservations();
        System.out.println();

        // ─── 7. PAY INVOICE ───────────────────────────────────────
        System.out.println("--- 7. Pay Invoice ---");
        Invoice inv = res2.getInvoice();
        System.out.println("Invoice: " + inv);
        inv.pay(100.0, PaymentMethod.CASH);
        inv.pay(100.0, PaymentMethod.CREDIT_CARD);
        inv.pay(inv.getRemainingAmount(), PaymentMethod.ONLINE);
        System.out.println("Fully paid: " + inv.isFullyPaid());
        System.out.println();

        // ─── 8. COMPLETE RESERVATION ──────────────────────────────
        System.out.println("--- 8. Complete Reservation ---");
        res2.complete();
        System.out.println();

        // ─── 9. CANCEL RESERVATION ────────────────────────────────
        System.out.println("--- 9. Cancel Reservation ---");
        Reservation res3 = new Reservation(g2, r202,
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(15));
        g2.makeReservation(res3);
        g2.cancelReservation(res3);
        System.out.println();

        // ─── 10. RESCHEDULE RESERVATION ──────────────────────────
        System.out.println("--- 10. Reschedule Reservation ---");
        Reservation res4 = new Reservation(g2, r202,
                LocalDate.now().plusDays(20),
                LocalDate.now().plusDays(23));
        res4.reschedule(LocalDate.now().plusDays(22),
                        LocalDate.now().plusDays(25));
        System.out.println();

        // ─── 11. GUEST CHECKOUT AND PAY ──────────────────────────
        System.out.println("--- 11. Guest Checkout and Pay ---");
        g1.checkoutAndPay(200.0);  // should succeed
        g1.checkoutAndPay(99999.0); // should fail - insufficient balance
        System.out.println();

        // ─── 12. STAFF - VIEW ALL ─────────────────────────────────
        System.out.println("--- 12. Staff View All ---");
        Admin admin = new Admin("admin1", "adminpass", 
                                LocalDate.of(1980, 1, 1), 40);
        admin.viewAllGuests(HotelDatabase.getGuests());
        admin.viewAllRooms(HotelDatabase.getRooms());
        admin.viewAllReservations(HotelDatabase.getReservations());
        System.out.println();

        // ─── 13. ADMIN CRUD ON ROOMS ─────────────────────────────
        System.out.println("--- 13. Admin CRUD on Rooms ---");
        RoomType deluxeType = new RoomType(3, "Deluxe", 180.0);
        Room r303 = new Room("303", deluxeType);
        List<Room> roomList = HotelDatabase.getRooms();
        admin.addRoom(roomList, r303);
        admin.updateRoom(roomList, 303, new Room("303", deluxeType));
        admin.removeRoom(roomList, 303);
        System.out.println();

        // ─── 14. ADMIN CRUD ON ROOM TYPES ────────────────────────
        System.out.println("--- 14. Admin CRUD on RoomTypes ---");
        List<RoomType> roomTypes = new java.util.ArrayList<>();
        roomTypes.add(deluxeType);
        admin.updateRoomType(roomTypes, "Deluxe", new RoomType(4, "Deluxe Plus", 200.0));
        admin.removeRoomType(roomTypes, "Deluxe Plus");
        System.out.println();

        // ─── 15. ADMIN CRUD ON AMENITIES ─────────────────────────
        System.out.println("--- 15. Admin CRUD on Amenities ---");
        try {
            Amenity wifi = new Amenity("WiFi", "High speed internet", 10.0);
            Amenity tv   = new Amenity("TV", "Smart TV", 5.0);
            List<Amenity> amenityList = new java.util.ArrayList<>();
            admin.addAmenity(amenityList, wifi);
            admin.addAmenity(amenityList, tv);
            admin.updateAmenity(amenityList, "WiFi", 
                                new Amenity("WiFi Pro", "Ultra fast internet", 15.0));
            admin.removeAmenity(amenityList, "TV");
        } catch (InvalidPaymentException e) {
            System.out.println("Amenity error: " + e.getMessage());
        }
        System.out.println();

        // ─── 16. RECEPTIONIST CHECK-IN / CHECK-OUT ───────────────
        System.out.println("--- 16. Receptionist Check-in / Check-out ---");
        try {
            Receptionist receptionist = new Receptionist("rec1", "recpass123",
                    LocalDate.of(1995, 6, 15), 35);
            Room r202fresh = new Room("202", new RoomType(2, "Single Cozy", 85.0));
            receptionist.checkIn(g2, r202fresh,
                    LocalDate.now().plusDays(30),
                    LocalDate.now().plusDays(33));
            receptionist.checkOut(g2, r202fresh, 
                    LocalDate.now().plusDays(33));
        } catch (RoomNotAvailableException e) {
            System.out.println("Check-in error: " + e.getMessage());
        }
        System.out.println();

        // ─── 17. ROOM BOOKABLE INTERFACE ─────────────────────────
        System.out.println("--- 17. Room Bookable Interface ---");
        try {
            Room testRoom = new Room("999", new RoomType(2, "Test", 50.0));
            System.out.println("Available: " + testRoom.checkAvailability());
            Reservation testRes = new Reservation(g1, testRoom,
                    LocalDate.now().plusDays(5),
                    LocalDate.now().plusDays(7));
            testRoom.reserve(testRes);
            System.out.println("Available after reserve: " + testRoom.checkAvailability());
            testRoom.cancel(testRes.getReservationId());
            System.out.println("Available after cancel: " + testRoom.checkAvailability());
        } catch (RoomNotAvailableException e) {
            System.out.println("Room error: " + e.getMessage());
        }
        System.out.println();

        // ─── 18. GUEST TOSTRING ───────────────────────────────────
        System.out.println("--- 18. toString Tests ---");
        System.out.println(g1);
        System.out.println(g2);
        System.out.println(rooms.get(0));
        System.out.println(res2);
        System.out.println(inv);

        System.out.println("\n=================================================");
        System.out.println("              ALL TESTS COMPLETE                 ");
        System.out.println("=================================================");
    }
}
