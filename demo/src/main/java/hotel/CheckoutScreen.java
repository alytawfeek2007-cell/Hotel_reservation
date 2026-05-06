package hotel;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CheckoutScreen {

    private final Scene scene;
    private final Reservation reservation;

    public CheckoutScreen(Reservation reservation) {
        this.reservation = reservation;
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
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle(
            "-fx-background:transparent;" +
            "-fx-background-color:#f4f6fb;");

        VBox content = new VBox(20);
        content.setPadding(new Insets(28));
        content.setMaxWidth(680);
        content.setStyle(
            "-fx-background-color:#f4f6fb;");

        // ── Invoice summary ───────────────────────────────────────
        Invoice invoice = reservation.getInvoice();
        double total     = reservation.calculateTotalCost();
        double paid      = invoice != null
            ? invoice.getPaidAmount() : 0;
        double remaining = total - paid;

        VBox invoiceCard = buildCard("Invoice Summary");

        String[][] rows = {
            {"Reservation ID",
                reservation.getReservationId()},
            {"Room",
                "Room " + reservation.getRoom()
                    .getRoomNumber() + " — " +
                reservation.getRoom().getRoomType()
                    .getTypeName()},
            {"Check-In",
                reservation.getCheckInDate().toString()},
            {"Check-Out",
                reservation.getCheckOutDate().toString()},
            {"Nights",
                String.valueOf(
                    reservation.getNumberOfNights())},
            {"Price / Night",
                String.format("$%.2f",
                    reservation.getRoom()
                        .getTotalPricePerNight())},
            {"Total Amount",
                String.format("$%.2f", total)},
            {"Already Paid",
                String.format("$%.2f", paid)},
        };
        for (String[] row : rows) {
            invoiceCard.getChildren().add(
                detailRow(row[0], row[1]));
        }

        invoiceCard.getChildren().add(new Separator());
        Label remainingLbl = new Label(
            String.format("Remaining: $%.2f", remaining));
        remainingLbl.setStyle(
            "-fx-font-size:20px;-fx-font-weight:bold;" +
            "-fx-text-fill:#c8a04a;");
        invoiceCard.getChildren().add(remainingLbl);

        // ── Payment method ────────────────────────────────────────
        VBox paymentCard = buildCard("Select Payment Method");

        ToggleGroup methodGroup = new ToggleGroup();
        ToggleButton cashBtn =
            paymentBtn("💵  Cash",
                PaymentMethod.CASH, methodGroup);
        ToggleButton cardBtn =
            paymentBtn("💳  Credit Card",
                PaymentMethod.CREDIT_CARD, methodGroup);
        ToggleButton onlineBtn =
            paymentBtn("🌐  Online",
                PaymentMethod.ONLINE, methodGroup);
        cashBtn.setSelected(true);

        HBox methodsRow = new HBox(
            12, cashBtn, cardBtn, onlineBtn);

        Label balanceLbl = new Label(String.format(
            "Your balance: $%.2f", guest.getBalance()));
        balanceLbl.setStyle(
            "-fx-font-size:13px;-fx-text-fill:#7a8599;");

        Label warningLbl = new Label("");
        warningLbl.setStyle(
            "-fx-text-fill:#e74c3c;-fx-font-size:12px;");
        warningLbl.setWrapText(true);

        if (guest.getBalance() < remaining) {
            warningLbl.setText(String.format(
                "Your balance ($%.2f) is not enough " +
                "to cover $%.2f.",
                guest.getBalance(), remaining));
        }

        paymentCard.getChildren().addAll(
            methodsRow, new Separator(),
            balanceLbl, warningLbl);

        // ── Confirm button ────────────────────────────────────────
        Button confirmBtn = new Button(
            "Confirm Payment & Checkout");
        confirmBtn.setPrefHeight(48);
        confirmBtn.setMaxWidth(Double.MAX_VALUE);
        confirmBtn.setStyle(
            "-fx-background-color:#c8a04a;" +
            "-fx-text-fill:#1a2744;-fx-font-weight:bold;" +
            "-fx-background-radius:8;-fx-cursor:hand;" +
            "-fx-font-size:15px;");
        confirmBtn.setDisable(
            guest.getBalance() < remaining
            || remaining <= 0);

        Button backBtn = new Button(
            "← Back to Reservations");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setStyle(
            "-fx-background-color:transparent;" +
            "-fx-text-fill:#7a8599;" +
            "-fx-padding:10 20;-fx-background-radius:8;" +
            "-fx-border-color:#dde3f0;" +
            "-fx-border-radius:8;-fx-cursor:hand;");
        backBtn.setOnAction(
            e -> SceneManager
                .showReservationManagement());

        confirmBtn.setOnAction(e -> {
            ToggleButton selected =
                (ToggleButton) methodGroup
                    .getSelectedToggle();
            if (selected == null) {
                showError(
                    "Please select a payment method.");
                return;
            }
            PaymentMethod method =
                (PaymentMethod) selected.getUserData();

            Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Payment");
            confirm.setHeaderText(null);
            confirm.setContentText(String.format(
                "Pay $%.2f via %s?\n" +
                "This cannot be undone.",
                remaining, method));
            confirm.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    try {
                        guest.checkoutAndPay(
                            reservation, method);
                        reservation.complete();
                        Alert success = new Alert(
                            Alert.AlertType.INFORMATION);
                        success.setTitle(
                            "Checkout Complete!");
                        success.setHeaderText(null);
                        success.setContentText(
                            String.format(
                                "Payment of $%.2f " +
                                "processed!\n" +
                                "Room %s released.\n" +
                                "Balance: $%.2f",
                                remaining,
                                reservation.getRoom()
                                    .getRoomNumber(),
                                guest.getBalance()));
                        success.showAndWait();
                        SceneManager
                            .showGuestDashboard();
                    } catch (Exception ex) {
                        showError(ex.getMessage());
                    }
                }
            });
        });

        content.getChildren().addAll(
            invoiceCard, paymentCard,
            confirmBtn, backBtn);

        VBox centered = new VBox(content);
        centered.setAlignment(Pos.TOP_CENTER);
        centered.setPadding(
            new Insets(0, 20, 0, 20));
        scroll.setContent(centered);

        VBox mainArea = new VBox(topbar, scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return new Scene(root, 1100, 720);
    }

    // ── Invoice detail row ────────────────────────────────────────
    private HBox detailRow(String key, String value) {
        Label k = new Label(key);
        k.setStyle(
            "-fx-font-size:13px;-fx-text-fill:#7a8599;" +
            "-fx-min-width:160;");
        Label v = new Label(value);
        v.setStyle(
            "-fx-font-size:13px;-fx-text-fill:#1a2744;");
        HBox row = new HBox(20, k, v);
        row.setPadding(new Insets(4, 0, 4, 0));
        return row;
    }

    // ── Payment method toggle button ──────────────────────────────
    private ToggleButton paymentBtn(String label,
            PaymentMethod method, ToggleGroup group) {
        ToggleButton btn = new ToggleButton(label);
        btn.setToggleGroup(group);
        btn.setUserData(method);
        String active =
            "-fx-background-color:#1a2744;" +
            "-fx-text-fill:white;-fx-font-weight:bold;" +
            "-fx-padding:12 24;-fx-background-radius:10;" +
            "-fx-cursor:hand;-fx-font-size:13px;";
        String inactive =
            "-fx-background-color:#eef0f6;" +
            "-fx-text-fill:#1a2744;" +
            "-fx-padding:12 24;-fx-background-radius:10;" +
            "-fx-cursor:hand;-fx-font-size:13px;";
        btn.setStyle(inactive);
        btn.selectedProperty().addListener(
            (o, ov, nv) ->
                btn.setStyle(nv ? active : inactive));
        return btn;
    }

    // ── Card builder ──────────────────────────────────────────────
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
        card.getChildren().addAll(heading, new Separator());
        return card;
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
                item[1].equals("Checkout");
            HBox btn = sidebarBtn(
                item[0], item[1], active);
            btn.setOnMouseClicked(e -> {
                switch (item[1]) {
                    case "Dashboard" ->
                        SceneManager.showGuestDashboard();
                    case "Browse Rooms" ->
                        SceneManager.showRoomBrowsing();
                    case "My Reservations" ->
                        SceneManager
                            .showReservationManagement();
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
        Label title = new Label("Checkout & Payment");
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

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}