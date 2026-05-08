package hotel;

import java.util.List;
import java.util.stream.Collectors;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ReservationScreen {

    private final Scene scene;
    private VBox reservationList;
    private ToggleButton allBtn, activeBtn,
                         cancelledBtn, completedBtn;

    public ReservationScreen() {
        scene = buildScene();
    }

    public Scene getScene() { return scene; }

    private Scene buildScene() {
        Guest guest = SceneManager.getCurrentGuest();

        // ── Sidebar ──────────────────────────────────────────────
        VBox sidebar = buildSidebar();

        // ── Topbar ───────────────────────────────────────────────
        HBox topbar = buildTopbar(guest.getUsername());

        // ── Content ──────────────────────────────────────────────
        VBox content = new VBox(20);
        content.setPadding(new Insets(24));
        content.setStyle("-fx-background-color:#f4f6fb;");

        // Filter tabs
        ToggleGroup group = new ToggleGroup();
        allBtn       = filterTab("All",       group, true);
        activeBtn    = filterTab("Active",    group, false);
        cancelledBtn = filterTab("Cancelled", group, false);
        completedBtn = filterTab("Completed", group, false);

        HBox tabs = new HBox(8,
            allBtn, activeBtn,
            cancelledBtn, completedBtn);

        // New reservation button
        Button newResBtn = goldButton("+ New Reservation");
        newResBtn.setOnAction(
            e -> SceneManager.showRoomBrowsing());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox headerRow = new HBox(
            12, tabs, spacer, newResBtn);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        // Refresh list when tab changes
        group.selectedToggleProperty().addListener(
            (obs, ov, nv) -> {
            if (nv != null)
                refreshList(guest,
                    ((ToggleButton) nv).getText());
        });

        // Reservation list container
        reservationList = new VBox(12);
        refreshList(guest, "All");

        ScrollPane scroll = new ScrollPane(reservationList);
        scroll.setFitToWidth(true);
        scroll.setStyle(
            "-fx-background:transparent;" +
            "-fx-background-color:transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        content.getChildren().addAll(headerRow, scroll);

        VBox mainArea = new VBox(topbar, content);
        VBox.setVgrow(content, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return new Scene(root, 1100, 720);
    }

    // ── Filter tab button ─────────────────────────────────────────
    private ToggleButton filterTab(String text,
            ToggleGroup group, boolean selected) {
        ToggleButton btn = new ToggleButton(text);
        btn.setToggleGroup(group);
        btn.setSelected(selected);
        String active =
            "-fx-background-color:#1a2744;" +
            "-fx-text-fill:white;-fx-font-weight:bold;" +
            "-fx-padding:8 20;-fx-background-radius:20;" +
            "-fx-cursor:hand;-fx-font-size:12px;";
        String inactive =
            "-fx-background-color:white;" +
            "-fx-text-fill:#7a8599;" +
            "-fx-padding:8 20;-fx-background-radius:20;" +
            "-fx-cursor:hand;-fx-font-size:12px;" +
            "-fx-border-color:#dde3f0;" +
            "-fx-border-radius:20;";
        btn.setStyle(selected ? active : inactive);
        btn.selectedProperty().addListener(
            (o, ov, nv) ->
                btn.setStyle(nv ? active : inactive));
        return btn;
    }

    // ── Refresh reservation list ──────────────────────────────────
    private void refreshList(Guest guest, String filter) {
        reservationList.getChildren().clear();

        List<Reservation> filtered =
            HotelDatabase.getReservations()
            .stream()
            .filter(r -> r.getGuest().getUsername()
                .equals(guest.getUsername()))
            .filter(r -> switch (filter) {
                case "Active" ->
                    r.getStatus() == ReservationStatus.CONFIRMED
                    || r.getStatus() == ReservationStatus.PENDING;
                case "Cancelled" ->
                    r.getStatus() == ReservationStatus.CANCELLED;
                case "Completed" ->
                    r.getStatus() == ReservationStatus.COMPLETED;
                default -> true;
            })
            .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            Label empty = new Label(
                "No reservations found.");
            empty.setStyle(
                "-fx-text-fill:#7a8599;" +
                "-fx-font-size:14px;-fx-padding:40;");
            reservationList.getChildren().add(empty);
            return;
        }

        for (Reservation res : filtered) {
            reservationList.getChildren().add(
                buildReservationCard(res, guest));
        }
    }

    // ── Single reservation card ───────────────────────────────────
    private VBox buildReservationCard(
            Reservation res, Guest guest) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(18));
        card.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:12;" +
            "-fx-effect:dropshadow(gaussian," +
            "rgba(0,0,0,0.07),8,0,0,2);");

        // Header row — ID + status badge
        Label resId = new Label(res.getReservationId());
        resId.setStyle(
            "-fx-font-size:14px;-fx-font-weight:bold;" +
            "-fx-text-fill:#1a2744;");

        String statusColor = switch (res.getStatus()) {
            case CONFIRMED -> "#27ae60";
            case PENDING   -> "#c8a04a";
            case CANCELLED -> "#e74c3c";
            case COMPLETED -> "#7a8599";
        };
        Label statusBadge = new Label(
            res.getStatus().toString());
        statusBadge.setStyle(
            "-fx-background-color:" + statusColor + ";" +
            "-fx-text-fill:white;" +
            "-fx-padding:3 12;-fx-background-radius:12;" +
            "-fx-font-size:11px;-fx-font-weight:bold;");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        HBox headerRow = new HBox(8, resId, sp, statusBadge);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        // Details grid
        GridPane grid = new GridPane();
        grid.setHgap(32);
        grid.setVgap(8);
        addDetail(grid, 0, 0, "Room",
            "Room " + res.getRoom().getRoomNumber() +
            " — " + res.getRoom().getRoomType()
                .getTypeName());
        addDetail(grid, 1, 0, "Check-In",
            res.getCheckInDate().toString());
        addDetail(grid, 0, 1, "Check-Out",
            res.getCheckOutDate().toString());
        addDetail(grid, 1, 1, "Nights",
            String.valueOf(res.getNumberOfNights()));
        addDetail(grid, 0, 2, "Total",
            String.format("$%.2f",
                res.calculateTotalCost()));
        if (res.getInvoice() != null) {
            addDetail(grid, 1, 2, "Paid",
                String.format("$%.2f",
                    res.getInvoice().getPaidAmount()));
        }

        // Action buttons
        HBox actions = new HBox(10);

        if (res.getStatus() == ReservationStatus.CONFIRMED) {
            Button checkoutBtn =
                goldButton("💳  Checkout & Pay");
            checkoutBtn.setOnAction(
                e -> SceneManager.showCheckout(res));
            actions.getChildren().add(checkoutBtn);
        }

        if (res.getStatus() == ReservationStatus.CONFIRMED
                || res.getStatus()
                    == ReservationStatus.PENDING) {
            Button cancelBtn = dangerButton("✖  Cancel");
            cancelBtn.setOnAction(e -> {
                Alert confirm = new Alert(
                    Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Cancel Reservation");
                confirm.setHeaderText(null);
                confirm.setContentText(
                    "Cancel " + res.getReservationId()
                    + "?");
                confirm.showAndWait().ifPresent(result -> {
                    if (result == ButtonType.OK) {
                        try {
                            guest.cancelReservation(res);
                            refreshList(guest,
                                getCurrentFilter());
                        } catch (Exception ex) {
                            showError(ex.getMessage());
                        }
                    }
                });
            });
            actions.getChildren().add(cancelBtn);
        }

        if (actions.getChildren().isEmpty()) {
            Label noAction = new Label("No actions");
            noAction.setStyle(
                "-fx-text-fill:#7a8599;" +
                "-fx-font-size:12px;");
            actions.getChildren().add(noAction);
        }

        card.getChildren().addAll(
            headerRow, new Separator(),
            grid, new Separator(), actions);
        return card;
    }

    // ── Grid detail helper ────────────────────────────────────────
    private void addDetail(GridPane grid,
            int col, int row,
            String key, String value) {
        VBox cell = new VBox(2);
        Label k = new Label(key);
        k.setStyle(
            "-fx-font-size:11px;-fx-text-fill:#7a8599;");
        Label v = new Label(value);
        v.setStyle(
            "-fx-font-size:13px;-fx-font-weight:bold;" +
            "-fx-text-fill:#1a2744;");
        cell.getChildren().addAll(k, v);
        grid.add(cell, col, row);
    }

    // ── Get current filter tab name ───────────────────────────────
    private String getCurrentFilter() {
        if (activeBtn.isSelected())    return "Active";
        if (cancelledBtn.isSelected()) return "Cancelled";
        if (completedBtn.isSelected()) return "Completed";
        return "All";
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
            {"🏠", "Dashboard"},
            {"🛏", "Browse Rooms"},
            {"📋", "My Reservations"},
            {"💳", "Checkout"}
        };
        for (String[] item : items) {
            boolean active =
                item[1].equals("My Reservations");
            HBox btn = sidebarBtn(
                item[0], item[1], active);
            btn.setOnMouseClicked(e -> {
                switch (item[1]) {
                    case "Dashboard" ->
                        SceneManager.showGuestDashboard();
                    case "Browse Rooms" ->
                        SceneManager.showRoomBrowsing();
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
                            () -> showError(
                                "No confirmed reservation.")
                        );
                    }
                }
            });
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
        Label title = new Label("My Reservations");
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
            "-fx-padding:9 20;-fx-background-radius:8;" +
            "-fx-cursor:hand;");
        return btn;
    }

    private Button dangerButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color:#e74c3c;" +
            "-fx-text-fill:white;-fx-font-weight:bold;" +
            "-fx-padding:9 18;-fx-background-radius:8;" +
            "-fx-cursor:hand;");
        return btn;
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}