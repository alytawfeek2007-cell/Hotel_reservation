package hotel;
import java.time.LocalDate;
import java.util.List;

public class Admin extends Staff {

    public Admin(String username,String Password,LocalDate dateOfBirth, int workingHours){
        super(username, Password, dateOfBirth,Role.ADMIN, workingHours);
    }
     public void addRoom(List<Room> rooms, Room room) {
        rooms.add(room);
        System.out.println("Room " + room.getRoomNumber() + " added.");
    }

    public void removeRoom(List<Room> rooms, int roomNumber) {
        rooms.removeIf(r -> r.getRoomNumber().equals(String.valueOf(roomNumber)));
        System.out.println("Room " + roomNumber + " removed.");
    }

     public void updateRoom(List<Room> rooms, int roomNumber, Room updatedRoom) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomNumber().equals(String.valueOf(roomNumber))) {
                rooms.set(i, updatedRoom);
                System.out.println("Room " + roomNumber + " updated.");
                return;
            }
        }
        System.out.println("Room " + roomNumber + " not found.");
    }

     public void addAmenity(List<Amenity> amenities, Amenity amenity) {
        amenities.add(amenity);
        System.out.println("Amenity " + amenity.getName() + " added.");
    }

    public void removeAmenity(List<Amenity> amenities, String amenityName) {
        amenities.removeIf(a -> a.getName().equals(amenityName));
        System.out.println("Amenity " + amenityName + " removed.");
    }


    public void addRoomType(List<RoomType> roomTypes, RoomType roomType) {
        roomTypes.add(roomType);
        System.out.println("RoomType " + roomType.getTypeName() + " added.");
    }

    public void removeRoomType(List<RoomType> roomTypes, String typeName) {
        roomTypes.removeIf(rt -> rt.getTypeName().equals(typeName));
        System.out.println("RoomType " + typeName + " removed.");
    }

    public void updateRoomType(List<RoomType> roomTypes, String typeName, RoomType updatedType) {
        for (int i = 0; i < roomTypes.size(); i++) {
            if (roomTypes.get(i).getTypeName().equals(typeName)) {
                roomTypes.set(i, updatedType);
                System.out.println("RoomType " + typeName + " updated.");
                return;
            }
        }
        System.out.println("RoomType " + typeName + " not found.");
    }

    public void updateAmenity(List<Amenity> amenities, String amenityName, Amenity updatedAmenity) {
        for (int i = 0; i < amenities.size(); i++) {
            if (amenities.get(i).getName().equals(amenityName)) {
                amenities.set(i, updatedAmenity);
                System.out.println("Amenity " + amenityName + " updated.");
                return;
            }
        }
        System.out.println("Amenity " + amenityName + " not found.");
    }
}
