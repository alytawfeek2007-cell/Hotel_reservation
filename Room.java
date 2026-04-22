
import java.util.ArrayList;

public class Room {
    private RoomType type;
    private ArrayList<Amenity> amenities=new ArrayList<>();
    private String roomNumber;
    private boolean isAvailable = true;

     public Room(String roomNumber, RoomType roomType) {
        setRoomNumber(roomNumber);
        setRoomType(roomType);
        this.amenities = new ArrayList<>();
        this.isAvailable = true;
    }

    public RoomType getRoomType() {
        return type;
    }   
    public void setRoomType(RoomType roomType) {
        if (roomType == null) {
            throw new IllegalArgumentException("Room type cannot be null.");
        }
        this.type = roomType;
    }

    public ArrayList<Amenity> getAmenities() {

        return amenities;
    }

    public void setAmenities(ArrayList<Amenity> amenities) {
         if (amenities == null) {
            throw new IllegalArgumentException("Amenities list cannot be null.");
        }
        this.amenities = amenities;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

   public void setRoomNumber(String roomNumber) {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Room number cannot be empty.");
        }
        this.roomNumber = roomNumber.trim();
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void bookRoom() throws RoomNotAvailableException {
        if (!isAvailable) {
            throw new RoomNotAvailableException("Room " + roomNumber + " is already occupied.");
        }
        this.isAvailable = false;
        System.out.println("Room " + roomNumber + " has been successfully booked.");
    }

    public void addAmenity(Amenity amenity) {
        if (amenity == null) {
            throw new IllegalArgumentException("Amenity cannot be null.");
        }
        amenities.add(amenity);
    }
    
    public boolean removeAmenityByName(String amenityName) {
        if (amenityName == null || amenityName.trim().isEmpty()) return false;
        return amenities.removeIf(a -> a.getName().equalsIgnoreCase(amenityName.trim()));
    }

    public double getTotalPricePerNight() {
        double total = type.getPricePerNight();
        for (Amenity amenity : amenities) {
            total += amenity.getExtraPricePerNight();
        }
        return total;
    }
    

     @Override
    public String toString() {
        return "Room{roomNumber='" + roomNumber + "', roomType=" + getRoomType()+
                ", available=" + isAvailable + ", totalPricePerNight=" + getTotalPricePerNight() + "}";
    }
}


