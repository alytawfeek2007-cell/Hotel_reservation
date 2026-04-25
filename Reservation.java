import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {

    private final String reservationId;
    private static int idCounter = 1;

   
    private final Guest guest;
    private final Room  room;

  
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

 
    private ReservationStatus status;

    
    private Invoice invoice;

    private static final double DEPOSIT_RATE = 0.30;


    public Reservation(Guest guest, Room room,
                       LocalDate checkInDate, LocalDate checkOutDate) {

        if (guest == null) throw new IllegalArgumentException("Guest cannot be null.");
        if (room  == null) throw new IllegalArgumentException("Room cannot be null.");
        validateDates(checkInDate, checkOutDate);

        this.reservationId = "RES-" + String.format("%04d", idCounter++);
        this.guest         = guest;
        this.room          = room;
        this.checkInDate   = checkInDate;
        this.checkOutDate  = checkOutDate;
        this.status        = ReservationStatus.PENDING;
    }

    public String            getReservationId() { return reservationId; }
    public Guest             getGuest()         { return guest; }
    public Room              getRoom()          { return room; }
    public LocalDate         getCheckInDate()   { return checkInDate; }
    public LocalDate         getCheckOutDate()  { return checkOutDate; }
    public ReservationStatus getStatus()        { return status; }
    public Invoice           getInvoice()       { return invoice; }

       public void setCheckInDate(LocalDate checkInDate) {
        requireStatus(ReservationStatus.PENDING, "change check-in date");
        if (checkInDate == null)
            throw new IllegalArgumentException("Check-in date cannot be null.");
        if (!checkInDate.isBefore(this.checkOutDate))
            throw new IllegalArgumentException("Check-in must be before check-out.");
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        requireStatus(ReservationStatus.PENDING, "change check-out date");
        if (checkOutDate == null)
            throw new IllegalArgumentException("Check-out date cannot be null.");
        if (!this.checkInDate.isBefore(checkOutDate))
            throw new IllegalArgumentException("Check-out must be after check-in.");
        this.checkOutDate = checkOutDate;
    }

    public double getDepositAmount() {
        return calculateTotalCost() * DEPOSIT_RATE;
    }

    public double getRemainingAfterDeposit() {
        return calculateTotalCost() - getDepositAmount();
    }




    public void confirm() {
            requireStatus(ReservationStatus.PENDING, "confirm");
            try {
                room.bookRoom();
            } catch (RoomNotAvailableException e) {
                throw new IllegalStateException("Cannot confirm: " + e.getMessage(), e);
            }
            this.status  = ReservationStatus.CONFIRMED;
            this.invoice = new Invoice(calculateTotalCost());
            System.out.println(reservationId + " confirmed. Invoice: $" + invoice.getTotalAmount());
        }

     public void cancel() {
        if (status == ReservationStatus.COMPLETED || status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException(
                "Cannot cancel a reservation that is already " + status + ".");
        }
        if (status == ReservationStatus.CONFIRMED) {
            room.setAvailable(true);
        }
        this.status = ReservationStatus.CANCELLED;
        System.out.println(reservationId + " cancelled.");
    }   

    public void complete() {
            requireStatus(ReservationStatus.CONFIRMED, "complete");
            if (invoice != null && !invoice.isFullyPaid()) {
                throw new IllegalStateException(
                    "Cannot complete reservation: invoice is not fully paid.");
            }
            room.setAvailable(true);
            this.status = ReservationStatus.COMPLETED;
            System.out.println(reservationId + " completed.");
        }




     public void reschedule(LocalDate newCheckIn, LocalDate newCheckOut) {
        requireStatus(ReservationStatus.PENDING, "reschedule");
        validateDates(newCheckIn, newCheckOut);
        this.checkInDate  = newCheckIn;
        this.checkOutDate = newCheckOut;
        System.out.println(reservationId + " rescheduled to " + newCheckIn + " → " + newCheckOut);
    }

    public long getNumberOfNights() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public double calculateTotalCost() {
        return room.getTotalPricePerNight() * getNumberOfNights();
    }

    public void payDeposit(PaymentMethod method) {
        requireStatus(ReservationStatus.CONFIRMED, "pay deposit");
        if (invoice == null)
            throw new IllegalStateException("No invoice found.");
        if (invoice.getPaidAmount() > 0)
            throw new IllegalStateException("Deposit already paid.");

        double deposit = getDepositAmount();
        invoice.pay(deposit, method);
        System.out.println("Deposit of $" + deposit + " paid. Remaining: $" + invoice.getRemainingAmount());
    }



    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn  == null) throw new IllegalArgumentException("Check-in date cannot be null.");
        if (checkOut == null) throw new IllegalArgumentException("Check-out date cannot be null.");
        if (!checkIn.isBefore(checkOut))
            throw new IllegalArgumentException(
                "Check-in (" + checkIn + ") must be before check-out (" + checkOut + ").");
    }

     private void requireStatus(ReservationStatus required, String operation) {
        if (status != required)
            throw new IllegalStateException(
                "Cannot " + operation + " a reservation with status " + status +
                ". Required status: " + required + ".");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        return reservationId.equals(((Reservation) o).reservationId);
    }
    }
