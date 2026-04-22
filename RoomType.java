public class RoomType {
   private int capacity;
   private String typeName;
   private double pricePerNight;
    

    public RoomType(String typeName, int capacity, double pricePerNight) {
        setTypeName(typeName);
        setCapacity(capacity);
        setPricePerNight(pricePerNight);
    }

 

   public int getCapacity() {
    return capacity;
   }

   public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0.");
        }
        this.capacity = capacity;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        if (typeName == null || typeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Room type name cannot be empty.");
        }
        this.typeName = typeName.trim();
    }

    public double getPricePerNight() {
        return pricePerNight;
    }


 public void setPricePerNight(double pricePerNight) {
        if (pricePerNight < 0) {
            throw new IllegalArgumentException("Price per night cannot be negative.");
        }
        this.pricePerNight = pricePerNight;
    }

 @Override
    public String toString() {
        return "RoomType{name='" + typeName + "', capacity=" + capacity + ", pricePerNight=" + pricePerNight + "}";
    }
}

