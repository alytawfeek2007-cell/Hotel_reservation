package hotel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AdminDashboardScreen {

    private final Scene scene;
    private VBox roomListBox;

    public AdminDashboardScreen() {
        scene = buildScene();
    }

    public Scene getScene() { return scene; }

    private Scene buildScene() {
        Staff staff = SceneManager.getCurrentStaff();

        // ── Sidebar ──────────────────────────────────────────────
        VBox sidebar = buildSidebar();

        // ── Topbar ───────────────────────────────────────────────
        HBox topbar = buildTopbar(
            staff.getUsername() + " (Admin)");

        // ── Tab pane ─────────────────────────────────────────────
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(
            TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab overviewTab = new Tab(
            "Overview", buildOverviewPane());
        Tab roomsTab = new Tab(
            "Rooms", buildRoomsPane());
        Tab guestsTab = new Tab(
            "Guests", buildGuestsPane());
        Tab reservationsTab = new Tab(
            "Reservations", buildReservationsPane());

        tabPane.getTabs().addAll(
            overviewTab, roomsTab,
            guestsTab, reservationsTab);

        VBox mainArea = new VBox(topbar, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return new Scene(root, 1100, 720);
    }

    // ── Overview tab ──────────────────────────────────────────────
    private ScrollPane buildOverviewPane() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(24));
        content.setStyle(
            "-fx-background-color:#f4f6fb;");

        Label heading = new Label("System Overview");
        heading.setStyle(
            "-fx-font-size:20px;-fx-font-weight:bold;" +
            "-fx-text-fill:#1a2744;");

        long totalRooms = HotelDatabase.getRooms().size();
        long available  = HotelDatabase.getRooms()
            .stream().filter(Room::isAvailable).count();
        long totalGuests =
            HotelDatabase.getGuests().size();
        long totalRes =
            HotelDatabase.getReservations().size();
        double revenue = HotelDatabase.getReservations()
            .stream()
            .filter(r -> r.getInvoice() != null)
            .mapToDouble(r ->
                r.getInvoice().getPaidAmount())
            .sum();

        HBox stats = new HBox(16,
            statCard("🛏",
                String.valueOf(totalRooms),
                "Total Rooms", "#1a2744"),
            statCard("✅",
                String.valueOf(available),
                "Available", "#27ae60"),
            statCard("👥",
                String.valueOf(totalGuests),
                "Guests", "#8e44ad"),
            statCard("📋",
                String.valueOf(totalRes),
                "Reservations", "#c8a04a")
        );

        VBox revenueCard = buildCard("Revenue");
        Label revLbl = new Label(
            String.format("$%.2f collected", revenue));
        revLbl.setStyle(
            "-fx-font-size:24px;-fx-font-weight:bold;" +
            "-fx-text-fill:#c8a04a;");
        revenueCard.getChildren().add(revLbl);

        content.getChildren().addAll(
            heading, stats, revenueCard);

        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle(
            "-fx-background:transparent;" +
            "-fx-background-color:#f4f6fb;");
        return sp;
    }

    // ── Rooms tab ─────────────────────────────────────────────────
    private ScrollPane buildRoomsPane() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(24));
        content.setStyle(
            "-fx-background-color:#f4f6fb;");

        // Add room form
        VBox addCard = buildCard("Add New Room");
        TextField numField  =
            styledField("Room number (e.g. 301)");
        TextField typeField =
            styledField("Room type (e.g. Suite)");
        TextField capField  =
            styledField("Capacity");
        TextField priceField =
            styledField("Price per night");
        Label errLbl = new Label("");
        errLbl.setStyle(
            "-fx-text-fill:#e74c3c;-fx-font-size:12px;");

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(10);
        grid.add(lbl("Room Number:"), 0, 0);
        grid.add(numField, 1, 0);
        grid.add(lbl("Type:"), 0, 1);
        grid.add(typeField, 1, 1);
        grid.add(lbl("Capacity:"), 0, 2);
        grid.add(capField, 1, 2);
        grid.add(lbl("Price/Night:"), 0, 3);
        grid.add(priceField, 1, 3);

        Button addBtn = goldButton("Add Room");
        addBtn.setOnAction(e -> {
            errLbl.setText("");
            try {
                String num = numField.getText().trim();
                String type = typeField.getText().trim();
                int cap = Integer.parseInt(
                    capField.getText().trim());
                double price = Double.parseDouble(
                    priceField.getText().trim());
                if (num.isEmpty() || type.isEmpty())
                    throw new IllegalArgumentException(
                        "Fill in all fields.");
                RoomType rt = new RoomType(
                    cap, type, price);
                Room room = new Room(num, rt);
                HotelDatabase.addRoom(room);
                numField.clear(); typeField.clear();
                capField.clear(); priceField.clear();
                showSuccess("Room " + num + " added!");
                refreshRoomList(roomListBox);
            } catch (NumberFormatException ex) {
                errLbl.setText(
                    "Capacity and price must be numbers.");
            } catch (Exception ex) {
                errLbl.setText(ex.getMessage());
            }
        });

        addCard.getChildren().addAll(
            grid, errLbl, addBtn);

        // Room list
        VBox listCard = buildCard("All Rooms");
        roomListBox = new VBox(8);
        refreshRoomList(roomListBox);
        listCard.getChildren().add(roomListBox);

        content.getChildren().addAll(addCard, listCard);

        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle(
            "-fx-background:transparent;" +
            "-fx-background-color:#f4f6fb;");
        return sp;
    }

    private void refreshRoomList(VBox box) {
        box.getChildren().clear();
        for (Room room : HotelDatabase.getRooms()) {
            HBox row = new HBox(16);
            row.setPadding(new Insets(10));
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle(
                "-fx-background-color:#f9fafc;" +
                "-fx-background-radius:8;");

            Label num = new Label(
                "Room " + room.getRoomNumber());
            num.setStyle(
                "-fx-font-weight:bold;" +
                "-fx-font-size:13px;-fx-min-width:90;");
            Label type = new Label(
                room.getRoomType().getTypeName());
            type.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#7a8599;-fx-min-width:90;");
            Label price = new Label(String.format(
                "$%.2f/night",
                room.getTotalPricePerNight()));
            price.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#c8a04a;" +
                "-fx-font-weight:bold;-fx-min-width:100;");
            Label avail = new Label(
                room.isAvailable()
                ? "✅ Available" : "❌ Occupied");
            avail.setStyle(
                "-fx-font-size:12px;-fx-min-width:100;");

            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);

            Button removeBtn = dangerButton("Remove");
            removeBtn.setOnAction(ev -> {
                HotelDatabase.getRooms().remove(room);
                refreshRoomList(box);
            });

            row.getChildren().addAll(
                num, type, price, avail, sp, removeBtn);
            box.getChildren().add(row);
        }
        if (box.getChildren().isEmpty()) {
            Label none = new Label("No rooms.");
            none.setStyle("-fx-text-fill:#7a8599;");
            box.getChildren().add(none);
        }
    }

    // ── Guests tab ────────────────────────────────────────────────
    private ScrollPane buildGuestsPane() {
        VBox content = new VBox(16);
        content.setPadding(new Insets(24));
        content.setStyle(
            "-fx-background-color:#f4f6fb;");

        VBox card = buildCard("All Registered Guests");

        for (Guest g : HotelDatabase.getGuests()) {
            VBox gCard = new VBox(6);
            gCard.setPadding(new Insets(12));
            gCard.setStyle(
                "-fx-background-color:#f9fafc;" +
                "-fx-background-radius:8;");

            Label uname = new Label(
                "👤  " + g.getUsername());
            uname.setStyle(
                "-fx-font-weight:bold;" +
                "-fx-font-size:14px;" +
                "-fx-text-fill:#1a2744;");

            HBox details = new HBox(24,
                miniDetail("Balance",
                    String.format("$%.2f",
                        g.getBalance())),
                miniDetail("Gender",
                    g.getGender() != null
                    ? g.getGender().toString() : "N/A"),
                miniDetail("Address",
                    g.getAddress() != null
                    ? g.getAddress() : "N/A")
            );
            gCard.getChildren().addAll(uname, details);
            card.getChildren().add(gCard);
        }

        if (HotelDatabase.getGuests().isEmpty()) {
            card.getChildren().add(
                new Label("No guests registered."));
        }

        content.getChildren().add(card);
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle(
            "-fx-background:transparent;" +
            "-fx-background-color:#f4f6fb;");
        return sp;
    }

    // ── Reservations tab ──────────────────────────────────────────
    private ScrollPane buildReservationsPane() {
        VBox content = new VBox(16);
        content.setPadding(new Insets(24));
        content.setStyle(
            "-fx-background-color:#f4f6fb;");

        VBox card = buildCard("All Reservations");

        for (Reservation r :
                HotelDatabase.getReservations()) {
            HBox row = new HBox(16);
            row.setPadding(new Insets(10));
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle(
                "-fx-background-color:#f9fafc;" +
                "-fx-background-radius:8;");

            Label id = new Label(r.getReservationId());
            id.setStyle(
                "-fx-font-weight:bold;" +
                "-fx-min-width:110;-fx-font-size:13px;");
            Label guest = new Label(
                "👤 " + r.getGuest().getUsername());
            guest.setStyle(
                "-fx-font-size:12px;-fx-min-width:110;" +
                "-fx-text-fill:#7a8599;");
            Label room = new Label(
                "🛏 Room " + r.getRoom().getRoomNumber());
            room.setStyle(
                "-fx-font-size:12px;-fx-min-width:100;");
            Label dates = new Label(
                r.getCheckInDate() + " → " +
                r.getCheckOutDate());
            dates.setStyle(
                "-fx-font-size:12px;-fx-min-width:180;" +
                "-fx-text-fill:#7a8599;");

            String color = switch (r.getStatus()) {
                case CONFIRMED -> "#27ae60";
                case PENDING   -> "#c8a04a";
                case CANCELLED -> "#e74c3c";
                case COMPLETED -> "#7a8599";
            };
            Label status = new Label(
                r.getStatus().toString());
            status.setStyle(
                "-fx-font-size:11px;-fx-font-weight:bold;" +
                "-fx-text-fill:" + color + ";");

            row.getChildren().addAll(
                id, guest, room, dates, status);
            card.getChildren().add(row);
        }

        if (HotelDatabase.getReservations().isEmpty()) {
            card.getChildren().add(
                new Label("No reservations found."));
        }

        content.getChildren().add(card);
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle(
            "-fx-background:transparent;" +
            "-fx-background-color:#f4f6fb;");
        return sp;
    }

    // ── Sidebar ───────────────────────────────────────────────────
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
        sidebar.getChildren().addAll(
            brand, new Separator());

        String[][] items = {
            {"📊", "Overview"},
            {"🛏", "Manage Rooms"},
            {"👥", "Guests"},
            {"📋", "Reservations"}
        };
        for (String[] item : items) {
            HBox btn = sidebarBtn(
                item[0], item[1],
                item[1].equals("Overview"));
            sidebar.getChildren().add(btn);
        }
        return sidebar;
    }

    private HBox sidebarBtn(String emoji,
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
        return btn;
    }

    // ── Topbar ────────────────────────────────────────────────────
    private HBox buildTopbar(String username) {
        Label title = new Label("Admin Dashboard");
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

    // ── Helpers ───────────────────────────────────────────────────
    private VBox buildCard(String title) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(18));
        card.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:12;" +
            "-fx-effect:dropshadow(gaussian," +
            "rgba(0,0,0,0.07),8,0,0,2);");
        Label heading = new Label(title);
        heading.setStyle(
            "-fx-font-size:15px;-fx-font-weight:bold;" +
            "-fx-text-fill:#1a2744;");
        card.getChildren().addAll(
            heading, new Separator());
        return card;
    }

    private VBox statCard(String emoji, String value,
                          String label, String color) {
        Label e = new Label(emoji);
        e.setStyle("-fx-font-size:26px;");
        Label v = new Label(value);
        v.setStyle(
            "-fx-font-size:20px;-fx-font-weight:bold;" +
            "-fx-text-fill:" + color + ";");
        Label l = new Label(label);
        l.setStyle(
            "-fx-font-size:12px;-fx-text-fill:#7a8599;");
        VBox card = new VBox(4, e, v, l);
        card.setPadding(new Insets(18, 22, 18, 22));
        card.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:12;" +
            "-fx-effect:dropshadow(gaussian," +
            "rgba(0,0,0,0.07),8,0,0,2);");
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private HBox miniDetail(String key, String value) {
        Label k = new Label(key + ": ");
        k.setStyle(
            "-fx-font-size:11px;-fx-text-fill:#7a8599;");
        Label v = new Label(value);
        v.setStyle(
            "-fx-font-size:12px;-fx-font-weight:bold;" +
            "-fx-text-fill:#1a2744;");
        return new HBox(2, k, v);
    }

    private Label lbl(String text) {
        Label l = new Label(text);
        l.setStyle(
            "-fx-font-size:12px;-fx-font-weight:bold;" +
            "-fx-text-fill:#1a2744;");
        return l;
    }

    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle(
            "-fx-background-color:#f4f6fb;" +
            "-fx-border-color:#dde3f0;" +
            "-fx-border-radius:8;" +
            "-fx-background-radius:8;" +
            "-fx-padding:10;-fx-font-size:13px;");
        return tf;
    }

    private Button goldButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color:#c8a04a;" +
            "-fx-text-fill:#1a2744;-fx-font-weight:bold;" +
            "-fx-padding:10 22;-fx-background-radius:8;" +
            "-fx-cursor:hand;");
        return btn;
    }

    private Button dangerButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color:#e74c3c;" +
            "-fx-text-fill:white;-fx-font-weight:bold;" +
            "-fx-padding:8 16;-fx-background-radius:8;" +
            "-fx-cursor:hand;");
        return btn;
    }

    private void showSuccess(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Success");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
