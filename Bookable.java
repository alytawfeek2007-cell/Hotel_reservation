public interface Bookable {
    void reserve(Reservation r);   
    void cancel(String id);        
    boolean checkAvailability();   
}
