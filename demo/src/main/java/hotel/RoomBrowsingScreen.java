package hotel;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class RoomBrowsingScreen {

    private final Scene scene;
    private FlowPane roomGrid;

    public RoomBrowsingScreen() {
        scene = buildScene();
    }

    public Scene getScene() { return scene; }

    private Scene buildScene() {
        Guest guest = SceneManager.getCurrentGuest();

        // ── Sidebar ──────────────────────────────────────────────
        VBox sidebar = buildSidebar();

        // ── Topbar ───────────────────────────────────────────────
        HBox topbar = buildTopbar(guest.getUsername());

        // ── Filter panel ─────────────────────────────────────────
        VBox filterPanel = buildFilterPanel();

        // ── Room grid ────────────────────────────────────────────
        roomGrid = new FlowPane(16, 16);
        roomGrid.setPadding(new Insets(20));

        ScrollPane gridScroll = new ScrollPane(roomGrid);
        gridScroll.setFitToWidth(true);
        gridScroll.setStyle(
            "-fx-background:transparent;" +
            "-fx-background-color:#f4f6fb;");

        // Load all rooms at start
        loadRooms(null, Double.MAX_VALUE);

        HBox bodyArea = new HBox(filterPanel, gridScroll);
        HBox.setHgrow(gridScroll, Priority.ALWAYS);

        VBox mainArea = new VBox(topbar, bodyArea);
        VBox.setVgrow(bodyArea, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return new Scene(root, 1100, 720);
    }

    // ── Filter panel ─────────────────────────────────────────────
    private VBox buildFilterPanel() {
        VBox panel = new VBox(16);
        panel.setPrefWidth(220);
        panel.setMinWidth(220);
        panel.setPadding(new Insets(20, 16, 20, 16));
        panel.setStyle(
            "-fx-background-color:white;" +
            "-fx-effect:dropshadow(gaussian," +
            "rgba(0,0,0,0.06),6,0,0,0);");

        Label title = new Label("Filters");
        title.setStyle("-fx-font-size:15px;" +
            "-fx-font-weight:bold;-fx-text-fill:#1a2744;");

        // Room type filter
        Label typeLabel = new Label("Room Type");
        typeLabel.setStyle(
            "-fx-font-size:12px;-fx-font-weight:bold;" +
            "-fx-text-fill:#1a2744;");
        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.getItems().add("All Types");
        for (Room r : HotelDatabase.getRooms()) {
            String t = r.getRoomType().getTypeName();
            if (!typeFilter.getItems().contains(t))
                typeFilter.getItems().add(t);
        }
        typeFilter.setValue("All Types");
        typeFilter.setMaxWidth(Double.MAX_VALUE);

        // Price slider
        Label priceLabel = new Label("Max Price / Night");
        priceLabel.setStyle(
            "-fx-font-size:12px;-fx-font-weight:bold;" +
            "-fx-text-fill:#1a2744;");
        Label priceValue = new Label("No limit");
        priceValue.setStyle(
            "-fx-text-fill:#c8a04a;-fx-font-weight:bold;");
        Slider priceSlider = new Slider(0, 1000, 1000);
        priceSlider.setShowTickLabels(true);
        priceSlider.setMajorTickUnit(250);
        priceSlider.valueProperty().addListener((o, ov, nv) -> {
            double val = nv.doubleValue();
            priceValue.setText(val >= 1000
                ? "No limit"
                : String.format("$%.0f", val));
        });

        // Apply button
        Button applyBtn = goldButton("Apply Filters");
        applyBtn.setMaxWidth(Double.MAX_VALUE);
        applyBtn.setOnAction(e -> {
            String type = typeFilter.getValue()
                .equals("All Types") ? null
                : typeFilter.getValue();
            double max = priceSlider.getValue() >= 1000
                ? Double.MAX_VALUE
                : priceSlider.getValue();
            loadRooms(type, max);
        });

        // Reset button
        Button resetBtn = outlineButton("Reset");
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setOnAction(e -> {
            typeFilter.setValue("All Types");
            priceSlider.setValue(1000);
            loadRooms(null, Double.MAX_VALUE);
        });

        // Refresh button 
        Button refreshBtn = new Button("🔄 Refresh Rooms");
        refreshBtn.setMaxWidth(Double.MAX_VALUE);
        refreshBtn.setStyle(
            "-fx-background-color:#1a2744;" +
            "-fx-text-fill:white;-fx-font-weight:bold;" +
            "-fx-padding:10 22;-fx-background-radius:8;" +
            "-fx-cursor:hand;");
        refreshBtn.setOnAction(
            e -> loadRooms(null, Double.MAX_VALUE));

    // Add to panel children:
    panel.getChildren().addAll(
        title, new Separator(),
        typeLabel, typeFilter,
        priceLabel, priceValue, priceSlider,
        new Separator(),
        applyBtn, resetBtn, refreshBtn); // ← add refreshBtn here

        
        return panel;
    }

    // ── Load and display rooms ────────────────────────────────────
   
    private void loadRooms(String typeFilter, double maxPrice) {
        roomGrid.getChildren().clear();

        // This now reads the LIVE list directly
        List<Room> rooms = HotelDatabase.getRooms()
            .stream()
            .filter(r -> typeFilter == null
                || r.getRoomType().getTypeName()
                    .equals(typeFilter))
            .filter(r -> r.getTotalPricePerNight() <= maxPrice)
            .collect(Collectors.toList());

        if (rooms.isEmpty()) {
            Label none = new Label(
                "No rooms match your filters.");
            none.setStyle(
                "-fx-text-fill:#7a8599;" +
                "-fx-font-size:14px;-fx-padding:40;");
            roomGrid.getChildren().add(none);
            return;
        }

        for (Room room : rooms) {
            roomGrid.getChildren().add(buildRoomCard(room));
        }
    }

    // ── Room card ─────────────────────────────────────────────────
    private VBox buildRoomCard(Room room) {
        VBox card = new VBox(10);
        card.setPrefWidth(260);
        card.setPadding(new Insets(18));
        card.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:12;" +
            "-fx-effect:dropshadow(gaussian," +
            "rgba(0,0,0,0.08),10,0,0,2);");

        // Room emoji based on type
        String emoji = switch (
                room.getRoomType().getTypeName()
                    .toLowerCase()) {
            case "suite"  -> "🛏✨";
            case "double" -> "🛏🛏";
            default       -> "🛏";
        };
        Label roomEmoji = new Label(emoji);
        roomEmoji.setStyle("-fx-font-size:30px;");

        Label roomNum = new Label(
            "Room " + room.getRoomNumber());
        roomNum.setStyle(
            "-fx-font-size:15px;-fx-font-weight:bold;" +
            "-fx-text-fill:#1a2744;");

        Label typeLabel = new Label(
            room.getRoomType().getTypeName() +
            "  •  Capacity: " +
            room.getRoomType().getCapacity());
        typeLabel.setStyle(
            "-fx-font-size:12px;-fx-text-fill:#7a8599;");

        Label price = new Label(String.format(
            "$%.2f / night",
            room.getTotalPricePerNight()));
        price.setStyle(
            "-fx-font-size:17px;-fx-font-weight:bold;" +
            "-fx-text-fill:#c8a04a;");

        String availText = room.isAvailable()
            ? "✅ Available" : "❌ Occupied";
        String availColor = room.isAvailable()
            ? "#27ae60" : "#e74c3c";
        Label availLabel = new Label(availText);
        availLabel.setStyle(
            "-fx-text-fill:" + availColor + ";" +
            "-fx-font-size:12px;-fx-font-weight:bold;");

        // Amenities list
        VBox amenBox = new VBox(2);
        if (!room.getAmenities().isEmpty()) {
            Label amenHeader = new Label("Amenities:");
            amenHeader.setStyle(
                "-fx-font-size:11px;-fx-font-weight:bold;" +
                "-fx-text-fill:#7a8599;");
            amenBox.getChildren().add(amenHeader);
            for (Amenity a : room.getAmenities()) {
                Label al = new Label(
                    "  •  " + a.getName() +
                    " (+$" + String.format("%.0f",
                        a.getPrice()) + ")");
                al.setStyle(
                    "-fx-font-size:11px;" +
                    "-fx-text-fill:#7a8599;");
                amenBox.getChildren().add(al);
            }
        }

        // Book button
        Button bookBtn = goldButton("Book Now");
        bookBtn.setMaxWidth(Double.MAX_VALUE);
        bookBtn.setDisable(!room.isAvailable());

        bookBtn.setOnAction(
            e -> showBookingDialog(room));

        card.getChildren().addAll(
            roomEmoji, roomNum, typeLabel,
            new Separator(), price, availLabel,
            amenBox, bookBtn);
        return card;
    }

    // ── Booking dialog ────────────────────────────────────────────
    private void showBookingDialog(Room room) {
        Guest guest = SceneManager.getCurrentGuest();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Book Room " + room.getRoomNumber());
        dialog.setHeaderText(null);

        VBox content = new VBox(12);
        content.setPadding(new Insets(20));
        content.setPrefWidth(340);

        Label title = new Label("Select Your Dates");
        title.setStyle(
            "-fx-font-size:15px;-fx-font-weight:bold;" +
            "-fx-text-fill:#1a2744;");

        Label roomInfo = new Label(
            "Room " + room.getRoomNumber() + "  •  " +
            room.getRoomType().getTypeName() + "  •  $" +
            String.format("%.2f",
                room.getTotalPricePerNight()) + "/night");
        roomInfo.setStyle(
            "-fx-font-size:12px;-fx-text-fill:#7a8599;");

        DatePicker checkIn = new DatePicker(LocalDate.now());
        checkIn.setPromptText("Check-in");
        checkIn.setMaxWidth(Double.MAX_VALUE);

        DatePicker checkOut = new DatePicker(
            LocalDate.now().plusDays(1));
        checkOut.setPromptText("Check-out");
        checkOut.setMaxWidth(Double.MAX_VALUE);

        Label totalLabel = new Label();
        totalLabel.setStyle(
            "-fx-font-size:14px;-fx-font-weight:bold;" +
            "-fx-text-fill:#c8a04a;");

        // Update total whenever dates change
        Runnable updateTotal = () -> {
            if (checkIn.getValue() != null
                    && checkOut.getValue() != null
                    && checkIn.getValue()
                        .isBefore(checkOut.getValue())) {
                long nights = java.time.temporal.ChronoUnit
                    .DAYS.between(
                        checkIn.getValue(),
                        checkOut.getValue());
                double total =
                    room.getTotalPricePerNight() * nights;
                totalLabel.setText(String.format(
                    "Total: $%.2f  (%d nights)",
                    total, nights));
            } else {
                totalLabel.setText("Select valid dates");
            }
        };
        checkIn.valueProperty().addListener(
            (o, ov, nv) -> updateTotal.run());
        checkOut.valueProperty().addListener(
            (o, ov, nv) -> updateTotal.run());
        updateTotal.run();

        Label errorLabel = new Label("");
        errorLabel.setStyle(
            "-fx-text-fill:#e74c3c;-fx-font-size:12px;");
        errorLabel.setWrapText(true);

        content.getChildren().addAll(
            title, roomInfo, new Separator(),
            new Label("Check-in Date:"), checkIn,
            new Label("Check-out Date:"), checkOut,
            totalLabel, errorLabel);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes()
            .addAll(ButtonType.OK, ButtonType.CANCEL);

        Button okBtn = (Button) dialog.getDialogPane()
            .lookupButton(ButtonType.OK);
        okBtn.setText("Confirm Booking");

        // Validate before confirming
        okBtn.addEventFilter(
                javafx.event.ActionEvent.ACTION, ev -> {
            errorLabel.setText("");
            if (checkIn.getValue() == null
                    || checkOut.getValue() == null) {
                errorLabel.setText(
                    "Please select both dates.");
                ev.consume(); return;
            }
            if (!checkIn.getValue()
                    .isBefore(checkOut.getValue())) {
                errorLabel.setText(
                    "Check-in must be before check-out.");
                ev.consume(); return;
            }
            if (checkIn.getValue()
                    .isBefore(LocalDate.now())) {
                errorLabel.setText(
                    "Check-in cannot be in the past.");
                ev.consume(); return;
            }
            try {
                Reservation res = new Reservation(
                    guest, room,
                    checkIn.getValue(),
                    checkOut.getValue());
                guest.makeReservation(res);
                HotelDatabase.addReservation(res);

                // Show success
                Alert alert = new Alert(
                    Alert.AlertType.INFORMATION);
                alert.setTitle("Booking Confirmed!");
                alert.setHeaderText(null);
                alert.setContentText(
                    "Room " + room.getRoomNumber() +
                    " booked!\nCheck-in: " +
                    checkIn.getValue() +
                    "\nCheck-out: " +
                    checkOut.getValue());
                alert.showAndWait();

                loadRooms(null, Double.MAX_VALUE);
            } catch (Exception ex) {
                errorLabel.setText(ex.getMessage());
                ev.consume();
            }
        });

        dialog.showAndWait();
    }

    // ── Sidebar (same pattern as GuestDashboard) ──────────────────
    private VBox buildSidebar() {
        VBox sidebar = new VBox(4);
        sidebar.setPrefWidth(210);
        sidebar.setStyle("-fx-background-color:#1a2744;");
        sidebar.setPadding(new Insets(0, 0, 20, 0));

        Label brand = new Label("🏨  LUXE STAY");
        brand.setStyle(
            "-fx-text-fill:white;-fx-font-size:16px;" +
            "-fx-font-weight:bold;" +
            "-fx-padding:24 16 24 16;");
        sidebar.getChildren().add(brand);
        sidebar.getChildren().add(new Separator());

        String[][] items = {
            {"🏠", "Dashboard"},
            {"🛏", "Browse Rooms"},
            {"📋", "My Reservations"},
            {"💳", "Checkout"}
        };

        for (String[] item : items) {
            boolean active =
                item[1].equals("Browse Rooms");
            HBox btn = sidebarButton(
                item[0], item[1], active);
            btn.setOnMouseClicked(e -> {
                switch (item[1]) {
                    case "Dashboard" ->
                        SceneManager.showGuestDashboard();
                    case "My Reservations" ->
                        SceneManager
                            .showReservationManagement();
                    case "Checkout" -> {
                        var res = HotelDatabase
                            .getReservations().stream()
                            .filter(r -> r.getGuest()
                                .getUsername().equals(
                                SceneManager
                                    .getCurrentGuest()
                                    .getUsername()))
                            .filter(r -> r.getStatus()
                                == ReservationStatus.CONFIRMED)
                            .findFirst();
                        res.ifPresentOrElse(
                            SceneManager::showCheckout,
                            () -> showAlert(
                                "No confirmed reservation.")
                        );
                    }
                }
            });
            sidebar.getChildren().add(btn);
        }
        return sidebar;
    }

    private HBox sidebarButton(String emoji,
                               String label, boolean active) {
        Label icon = new Label(emoji);
        icon.setStyle("-fx-font-size:15px;");
        Label lbl = new Label(label);
        lbl.setStyle(active
            ? "-fx-text-fill:#1a2744;" +
              "-fx-font-weight:bold;-fx-font-size:13px;"
            : "-fx-text-fill:rgba(255,255,255,0.85);" +
              "-fx-font-size:13px;");
        HBox btn = new HBox(10, icon, lbl);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(11, 16, 11, 20));
        btn.setStyle(active
            ? "-fx-background-color:#c8a04a;" +
              "-fx-background-radius:0 24 24 0;" +
              "-fx-cursor:hand;"
            : "-fx-background-color:transparent;" +
              "-fx-cursor:hand;");
        if (!active) {
            btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color:" +
                "rgba(255,255,255,0.1);" +
                "-fx-background-radius:0 24 24 0;" +
                "-fx-cursor:hand;"));
            btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-cursor:hand;"));
        }
        return btn;
    }

    // ── Topbar ────────────────────────────────────────────────────
    private HBox buildTopbar(String username) {
        Label title = new Label("Browse Rooms");
        title.setStyle(
            "-fx-font-size:18px;-fx-font-weight:bold;" +
            "-fx-text-fill:#1a2744;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label user = new Label("👤  " + username);
        user.setStyle(
            "-fx-background-color:#1a2744;" +
            "-fx-text-fill:white;-fx-padding:6 14;" +
            "-fx-background-radius:20;-fx-font-size:12px;");
        Button logout = new Button("Logout");
        logout.setStyle(
            "-fx-background-color:#e74c3c;" +
            "-fx-text-fill:white;-fx-padding:6 14;" +
            "-fx-background-radius:8;-fx-cursor:hand;");
        logout.setOnAction(e -> SceneManager.logout());
        HBox topbar = new HBox(
            12, title, spacer, user, logout);
        topbar.setAlignment(Pos.CENTER_LEFT);
        topbar.setPadding(new Insets(16, 24, 16, 24));
        topbar.setStyle(
            "-fx-background-color:white;" +
            "-fx-effect:dropshadow(gaussian," +
            "rgba(0,0,0,0.07),6,0,0,2);");
        return topbar;
    }

    // ── Button helpers ────────────────────────────────────────────
    private Button goldButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color:#c8a04a;" +
            "-fx-text-fill:#1a2744;-fx-font-weight:bold;" +
            "-fx-padding:10 22;-fx-background-radius:8;" +
            "-fx-cursor:hand;");
        return btn;
    }

    private Button outlineButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color:transparent;" +
            "-fx-text-fill:#7a8599;" +
            "-fx-padding:10 22;-fx-background-radius:8;" +
            "-fx-border-color:#dde3f0;" +
            "-fx-border-radius:8;-fx-cursor:hand;");
        return btn;
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
