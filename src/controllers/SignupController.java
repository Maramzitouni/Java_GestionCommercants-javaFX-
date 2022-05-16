package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.ConnectionUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignupController implements Initializable {
    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtEntreprise;
    @FXML
    private Button btnSignup;

    /// --
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;




    @FXML
    public void handleButtonAction7(MouseEvent event) {

        if (event.getSource() == btnSignup) {
            //login here
            if (logIn().equals("Success")) {
                try {

                    //add you loading or delays - ;-)
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    //stage.setMaximized(true);
                    stage.close();
                    Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/OnBoard.fxml"))));
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }

            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO
        if (con == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Erreur serveur");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Serveur connecté");
        }
    }

    public SignupController() {
        con = ConnectionUtil.conDB();
    }

    //we gonna use string to check for status
    private String logIn() {
        String status = "Success";
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String username = txtUsername.getText();
        String entreprise = txtEntreprise.getText();
        if(email.isEmpty() || password.isEmpty() || username.isEmpty()  || entreprise.isEmpty()) {
            setLblError(Color.TOMATO, "Champs vides");
            status = "Erreur";
        } else {
            //query
            String sql = "INSERT INTO comm (email,password,username,entreprise) VALUES (?,?,?,?)";
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, username);
                preparedStatement.setString(4, entreprise);
                preparedStatement.executeUpdate();


            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                status = "Exception";
            }
        }

        return status;
    }

    private void setLblError(Color color, String text) {

        lblErrors.setTextFill(color);
        lblErrors.setText(text);
        System.out.println(text);
    }
}


