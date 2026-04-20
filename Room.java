
import java.util.ArrayList;

public class Room {
    private RoomType type;
    private ArrayList<Amenity> amenities=new ArrayList<>();
    private String roomNumber;
    private boolean isAvailable = true;

    public Room(String roomNumber, RoomType type) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.amenities = new ArrayList<>();
    }

    public RoomType getType() {
        return type;
    }   
    public void setType(RoomType type) {
        this.type = type;
    }
    public ArrayList<Amenity> getAmenities() {
        return amenities;
    }
    public void setAmenities(ArrayList<Amenity> amenities) {
        this.amenities = amenities;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
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

}
