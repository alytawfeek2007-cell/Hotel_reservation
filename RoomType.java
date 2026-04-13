public class RoomType {
   private int capacity;
   private String description;
   private double pricePerNight;
    private boolean isOccupied = false;

    public RoomType(int capacity, String description, double pricePerNight, boolean isOccupied) {
        this.capacity = capacity;
        this.description = description;
        this.pricePerNight = pricePerNight;
       this.isOccupied = isOccupied;
    }
 public void bookRoom() throws RoomNotAvailableException {
     if (this.isOccupied) {
         throw new RoomNotAvailableException("Room is already occupied.");
     }
     
     this.isOccupied = true;
     System.out.println("Room has been successfully booked.");
 }

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

}
