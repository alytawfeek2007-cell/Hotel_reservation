public class RoomType implements Bookable {
    private int capacity;
    private String description;
    private double pricePerNight;
    private boolean isOccupied = false;
    private String typeName; 

    public RoomType(int capacity, String description, double pricePerNight, boolean isOccupied) {
        this.capacity = capacity;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.isOccupied = isOccupied;
    }

    // Existing method
    public void bookRoom() throws RoomNotAvailableException {
        if (this.isOccupied) {
            throw new RoomNotAvailableException("Room is already occupied.");
        }
        this.isOccupied = true;
        System.out.println("Room has been successfully booked.");
    }

    // --- Bookable interface methods ---
    @Override
    public void reserve(Reservation r) throws RoomNotAvailableException {
        if (isOccupied) {
            throw new RoomNotAvailableException("Room type is already occupied.");
        }
        this.isOccupied = true;
        r.confirm(); // mark reservation as confirmed
        System.out.println("Reservation confirmed for room type: " + description);
    }

    @Override
    public void cancel(String id) {
        this.isOccupied = false;
        System.out.println("Reservation " + id + " cancelled. Room type is now available.");
    }

    @Override
    public boolean checkAvailability() {
        return !isOccupied;
    }

    // Getters and setters
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }
    public void setPricePerNight(double pricePerNight) throws InvalidPaymentException {
        if (pricePerNight < 0) {
            throw new InvalidPaymentException("Price per night cannot be negative. Attempted: " + pricePerNight);
        }
        this.pricePerNight = pricePerNight;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String name) {
        this.typeName = name;
    }
}
