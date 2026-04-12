
import java.util.ArrayList;

public class Room {
    private RoomType type;
    private ArrayList<Amenity> amenities=new ArrayList<>();
    
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

}
