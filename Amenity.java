public class Amenity implements Manageable {
    private String name;
    private String description;
    private double price;

    public Amenity(String name, String description, double price) throws InvalidPaymentException {
    this.name = name;
    this.description = description;
    this.setPrice(price);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) throws InvalidPaymentException {
        if (price < 0) {
            throw new InvalidPaymentException("Price cannot be negative. Provided value: " + price);
        }
        this.price = price;
    }

    // --- Manageable interface methods ---
    @Override
    public void create() {
        System.out.println("Amenity created: " + name);
        // In a real app, you’d add this to HotelDatabase.amenities
    }

    @Override
    public void update() {
        System.out.println("Amenity updated: " + name);
        // In a real app, you’d update the record in HotelDatabase
    }

    @Override
    public void delete() {
        System.out.println("Amenity deleted: " + name);
        // In a real app, you’d remove this from HotelDatabase.amenities
    }
}
