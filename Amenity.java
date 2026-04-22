public class Amenity {
   private String name;
   private String description;
   private double extraPricePerNight;

    public Amenity(String name, String description, double extraPricePerNight) {
        setName(name);
        setDescription(description);
        setExtraPricePerNight(extraPricePerNight);
    }

   public String getName() {
    return name;
   }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Amenity name cannot be empty.");
        }
        this.name = name.trim();
    }


   public String getDescription() {
    return description;
   }
   public void setDescription(String description) {
        this.description = (description == null) ? "" : description.trim();
    }


   public double getExtraPricePerNight() {
    return extraPricePerNight;
   }

  public void setExtraPricePerNight(double extraPricePerNight) {
        if (extraPricePerNight < 0) {
            throw new IllegalArgumentException("Amenity extra price cannot be negative.");
        }
        this.extraPricePerNight = extraPricePerNight;
    }

    

    @Override
    public String toString() {
        return "Amenity{name='" + name + "', extraPricePerNight=" + extraPricePerNight + "}";
    }
}
