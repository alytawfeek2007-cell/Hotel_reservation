package hotel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class LoginScreen {

    private final Scene scene;

    public LoginScreen() {
        scene = buildScene();
    }

    public Scene getScene() { return scene; }

    private Scene buildScene() {

        // ── Left decorative panel ────────────────────────────────
        VBox leftPanel = new VBox(16);
        leftPanel.setPrefWidth(380);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setPadding(new Insets(60, 50, 60, 50));
        leftPanel.setStyle("-fx-background-color:#1a2744;");

        Label hotelIcon = new Label("🏨");
        hotelIcon.setStyle("-fx-font-size:64px;");

        Label hotelName = new Label("LUXE STAY");
        hotelName.setStyle("-fx-text-fill:white;" +
            "-fx-font-size:28px;-fx-font-weight:bold;");

        Label tagline = new Label("Hotel & Suites");
        tagline.setStyle("-fx-text-fill:#c8a04a;-fx-font-size:14px;");

        Separator sep = new Separator();
        sep.setMaxWidth(200);

        Label quote = new Label("\"Where comfort meets luxury.\"");
        quote.setStyle("-fx-text-fill:rgba(255,255,255,0.7);" +
            "-fx-font-size:13px;");
        quote.setWrapText(true);
        quote.setTextAlignment(TextAlignment.CENTER);

        leftPanel.getChildren().addAll(
            hotelIcon, hotelName, tagline, sep, quote);

        // ── Right login form ─────────────────────────────────────
        VBox rightPanel = new VBox();
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setStyle("-fx-background-color:#f4f6fb;");

        VBox formCard = new VBox(16);
        formCard.setPadding(new Insets(40));
        formCard.setMaxWidth(360);
        formCard.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:16;" +
            "-fx-effect:dropshadow(gaussian," +
            "rgba(0,0,0,0.1),20,0,0,4);");

        Label welcome = new Label("Welcome back");
        welcome.setStyle("-fx-font-size:22px;" +
            "-fx-font-weight:bold;-fx-text-fill:#1a2744;");

        Label sub = new Label("Sign in to manage your reservations");
        sub.setStyle("-fx-font-size:13px;-fx-text-fill:#7a8599;");

        // Role toggle
        ToggleGroup roleGroup = new ToggleGroup();
        ToggleButton guestBtn = new ToggleButton("Guest");
        ToggleButton staffBtn = new ToggleButton("Staff");
        guestBtn.setToggleGroup(roleGroup);
        staffBtn.setToggleGroup(roleGroup);
        guestBtn.setSelected(true);
        styleToggle(guestBtn, true);
        styleToggle(staffBtn, false);
        guestBtn.selectedProperty().addListener(
            (o, ov, nv) -> styleToggle(guestBtn, nv));
        staffBtn.selectedProperty().addListener(
            (o, ov, nv) -> styleToggle(staffBtn, nv));
        HBox roleRow = new HBox(8, guestBtn, staffBtn);

        // Input fields
        TextField usernameField = styledField("Enter your username");
        PasswordField passwordField = styledPassField(
            "Enter your password");

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill:#e74c3c;-fx-font-size:12px;");
        errorLabel.setWrapText(true);

        Button loginBtn = new Button("Sign In");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setPrefHeight(44);
        styleLoginButton(loginBtn);

        // Login logic
        loginBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                return;
            }

            if (guestBtn.isSelected()) {
                Guest found = null;
                for (Guest g : HotelDatabase.getGuests()) {
                    if (g.login(username, password)) {
                        found = g;
                        break;
                    }
                }
                if (found != null) {
                    SceneManager.setCurrentGuest(found);
                    SceneManager.showGuestDashboard();
                } else {
                    errorLabel.setText(
                        "Invalid username or password.");
                }
            } else {
                // Staff login
                if (username.equals("admin") 
                        && password.equals("Admin@123!")) {
                    Admin admin = new Admin("admin", "Admin@123!",
                        java.time.LocalDate.of(1985, 1, 1), 40);
                    SceneManager.setCurrentStaff(admin);
                    SceneManager.showAdminDashboard();
                } else {
                    errorLabel.setText(
                        "Invalid staff credentials.");
                }
            }
        });

        // Register link
        Hyperlink registerLink = new Hyperlink(
            "Don't have an account? Register here");
        registerLink.setStyle(
            "-fx-text-fill:#c8a04a;-fx-font-size:12px;" +
            "-fx-border-color:transparent;");
        registerLink.setOnAction(
            e -> SceneManager.showRegister());

        Label demoHint = new Label(
    "Demo: Guest ali/Ali#2025!  |  Staff: admin/Admin@123!");
        demoHint.setStyle(
            "-fx-font-size:10px;-fx-text-fill:#7a8599;");

        formCard.getChildren().addAll(
            welcome, sub, new Separator(),
            new Label("Sign in as"), roleRow,
            new Label("Username"), usernameField,
            new Label("Password"), passwordField,
            errorLabel, loginBtn,
            registerLink, demoHint);

        rightPanel.getChildren().add(formCard);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        HBox root = new HBox(leftPanel, rightPanel);
        return new Scene(root, 900, 650);
    }

    // ─── Helper methods ──────────────────────────────────────────
    private void styleToggle(ToggleButton btn, boolean active) {
        if (active) {
            btn.setStyle(
                "-fx-background-color:#1a2744;" +
                "-fx-text-fill:white;-fx-font-weight:bold;" +
                "-fx-padding:7 22;-fx-background-radius:8;" +
                "-fx-cursor:hand;");
        } else {
            btn.setStyle(
                "-fx-background-color:#eef0f6;" +
                "-fx-text-fill:#7a8599;" +
                "-fx-padding:7 22;-fx-background-radius:8;" +
                "-fx-cursor:hand;");
        }
    }

    private void styleLoginButton(Button btn) {
        btn.setStyle(
            "-fx-background-color:#c8a04a;" +
            "-fx-text-fill:#1a2744;-fx-font-weight:bold;" +
            "-fx-background-radius:8;-fx-cursor:hand;" +
            "-fx-font-size:14px;");
    }

    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle(
            "-fx-background-color:#f4f6fb;" +
            "-fx-border-color:#dde3f0;-fx-border-radius:8;" +
            "-fx-background-radius:8;-fx-padding:10;" +
            "-fx-font-size:13px;");
        return tf;
    }

    private PasswordField styledPassField(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setStyle(
            "-fx-background-color:#f4f6fb;" +
            "-fx-border-color:#dde3f0;-fx-border-radius:8;" +
            "-fx-background-radius:8;-fx-padding:10;" +
            "-fx-font-size:13px;");
        return pf;
    }
}
