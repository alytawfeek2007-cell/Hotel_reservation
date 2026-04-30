package hotel;
public interface Bookable {
    void reserve(Reservation r) throws RoomNotAvailableException;   
    void cancel(String id) throws RoomNotAvailableException;        
    boolean checkAvailability() throws RoomNotAvailableException;   
}
