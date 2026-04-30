package hotel;

import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class RegisterScreen {

    private final Scene scene;

    public RegisterScreen() {
        scene = buildScene();
    }

    public Scene getScene() { return scene; }

    private Scene buildScene() {

        // ── Left panel ───────────────────────────────────────────
        VBox leftPanel = new VBox(14);
        leftPanel.setPrefWidth(320);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setPadding(new Insets(60, 40, 60, 40));
        leftPanel.setStyle("-fx-background-color:#1a2744;");

        Label icon = new Label("🏨");
        icon.setStyle("-fx-font-size:56px;");
        Label name = new Label("LUXE STAY");
        name.setStyle("-fx-text-fill:white;-fx-font-size:22px;" +
            "-fx-font-weight:bold;");
        Label sub = new Label("Create your account");
        sub.setStyle("-fx-text-fill:#c8a04a;-fx-font-size:13px;");

        leftPanel.getChildren().addAll(icon, name, sub);

        // ── Right form ───────────────────────────────────────────
        VBox rightPanel = new VBox();
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setStyle("-fx-background-color:#f4f6fb;");

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle(
            "-fx-background:transparent;" +
            "-fx-background-color:transparent;");

        VBox formCard = new VBox(12);
        formCard.setPadding(new Insets(32, 40, 32, 40));
        formCard.setMaxWidth(420);
        formCard.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:16;" +
            "-fx-effect:dropshadow(gaussian," +
            "rgba(0,0,0,0.1),20,0,0,4);");

        Label title = new Label("Create Account");
        title.setStyle("-fx-font-size:20px;" +
            "-fx-font-weight:bold;-fx-text-fill:#1a2744;");

        // Form fields
        TextField usernameField = styledField("Choose a username");
        PasswordField passwordField = styledPassField(
            "At least 6 characters");
        PasswordField confirmField = styledPassField(
            "Repeat your password");
        TextField addressField = styledField("Your address");
        TextField balanceField = styledField(
            "Starting balance (e.g. 500)");

        DatePicker dobPicker = new DatePicker();
        dobPicker.setPromptText("Date of birth");
        dobPicker.setMaxWidth(Double.MAX_VALUE);

        // Gender toggle
        ToggleGroup genderGroup = new ToggleGroup();
        ToggleButton maleBtn = new ToggleButton("Male");
        ToggleButton femaleBtn = new ToggleButton("Female");
        maleBtn.setToggleGroup(genderGroup);
        femaleBtn.setToggleGroup(genderGroup);
        maleBtn.setSelected(true);
        styleToggle(maleBtn, true);
        styleToggle(femaleBtn, false);
        maleBtn.selectedProperty().addListener(
            (o, ov, nv) -> styleToggle(maleBtn, nv));
        femaleBtn.selectedProperty().addListener(
            (o, ov, nv) -> styleToggle(femaleBtn, nv));
        HBox genderRow = new HBox(8, maleBtn, femaleBtn);

        TextField prefField = styledField(
            "Room preference (optional)");

        Label errorLabel = new Label("");
        errorLabel.setStyle(
            "-fx-text-fill:#e74c3c;-fx-font-size:12px;");
        errorLabel.setWrapText(true);

        Button registerBtn = new Button("Create Account");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setPrefHeight(44);
        registerBtn.setStyle(
            "-fx-background-color:#c8a04a;" +
            "-fx-text-fill:#1a2744;-fx-font-weight:bold;" +
            "-fx-background-radius:8;-fx-cursor:hand;" +
            "-fx-font-size:14px;");

        // Register logic
        registerBtn.setOnAction(e -> {
            errorLabel.setText("");
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String confirm  = confirmField.getText().trim();
            String address  = addressField.getText().trim();
            String balStr   = balanceField.getText().trim();
            LocalDate dob   = dobPicker.getValue();

            // Validation
            if (username.isEmpty() || password.isEmpty() 
                    || address.isEmpty()) {
                errorLabel.setText(
                    "Please fill in all required fields.");
                return;
            }
            if (!password.equals(confirm)) {
                errorLabel.setText("Passwords do not match.");
                return;
            }
            if (password.length() < 6) {
                errorLabel.setText(
                    "Password must be at least 6 characters.");
                return;
            }
            if (dob == null) {
                errorLabel.setText(
                    "Please select your date of birth.");
                return;
            }

            // Check duplicate username
            for (Guest g : HotelDatabase.getGuests()) {
                if (g.getUsername().equalsIgnoreCase(username)) {
                    errorLabel.setText(
                        "Username already taken.");
                    return;
                }
            }

            double balance = 0;
            try {
                balance = Double.parseDouble(balStr);
                if (balance < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                errorLabel.setText(
                    "Enter a valid positive balance.");
                return;
            }

            Guest.Gender gender = maleBtn.isSelected()
                ? Guest.Gender.MALE : Guest.Gender.FEMALE;
            String pref = prefField.getText().trim();

            try {
                Guest newGuest = pref.isEmpty()
                    ? new Guest(username, password, dob,
                                balance, address, gender)
                    : new Guest(username, password, dob,
                                balance, address, gender, pref);
                HotelDatabase.addGuest(newGuest);

                // Show success alert
                Alert alert = new Alert(
                    Alert.AlertType.INFORMATION);
                alert.setTitle("Account Created");
                alert.setHeaderText(null);
                alert.setContentText(
                    "Welcome, " + username + 
                    "! You can now log in.");
                alert.showAndWait();
                SceneManager.showLogin();

            } catch (Exception ex) {
                errorLabel.setText(ex.getMessage());
            }
        });

        Hyperlink backLink = new Hyperlink("← Back to Login");
        backLink.setStyle(
            "-fx-text-fill:#c8a04a;-fx-font-size:12px;" +
            "-fx-border-color:transparent;");
        backLink.setOnAction(e -> SceneManager.showLogin());

        formCard.getChildren().addAll(
            title, new Separator(),
            lbl("Username"),        usernameField,
            lbl("Password"),        passwordField,
            lbl("Confirm Password"),confirmField,
            lbl("Date of Birth"),   dobPicker,
            lbl("Balance ($)"),     balanceField,
            lbl("Address"),         addressField,
            lbl("Gender"),          genderRow,
            lbl("Room Preference"), prefField,
            errorLabel, registerBtn, backLink);

        VBox wrapper = new VBox(formCard);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(30, 20, 30, 20));
        scroll.setContent(wrapper);
        rightPanel.getChildren().add(scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        HBox root = new HBox(leftPanel, rightPanel);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);
        return new Scene(root, 900, 650);
    }

    // ─── Helper methods ──────────────────────────────────────────
    private Label lbl(String text) {
        Label l = new Label(text);
        l.setStyle(
            "-fx-font-size:12px;-fx-font-weight:bold;" +
            "-fx-text-fill:#1a2744;");
        return l;
    }

    private void styleToggle(ToggleButton btn, boolean active) {
        btn.setStyle(active
            ? "-fx-background-color:#1a2744;-fx-text-fill:white;" +
              "-fx-padding:8 20;-fx-background-radius:8;" +
              "-fx-cursor:hand;"
            : "-fx-background-color:#eef0f6;" +
              "-fx-text-fill:#7a8599;" +
              "-fx-padding:8 20;-fx-background-radius:8;" +
              "-fx-cursor:hand;");
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
