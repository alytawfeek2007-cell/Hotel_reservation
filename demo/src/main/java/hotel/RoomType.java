package hotel;
public class RoomType  {
    private int capacity;
    
    private double pricePerNight;
    private String typeName; 

    public RoomType(int capacity,  String typeName, double pricePerNight) {
        this.capacity = capacity;
        
        this.pricePerNight = pricePerNight;
       
        this.typeName=typeName;
    }

    // Existing method
   

   

    // Getters and setters
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
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

    @Override
    public String toString() {
        return "RoomType{name='" + typeName + "', capacity=" + capacity +
                ", pricePerNight=" + pricePerNight + "}";
    }
}
