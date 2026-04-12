public class RoomType {
   private int capacity;
   private String description;
   private double pricePerNight;

    public RoomType(int capacity, String description, double pricePerNight) {
        this.capacity = capacity;
        this.description = description;
        this.pricePerNight = pricePerNight;
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

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

}
