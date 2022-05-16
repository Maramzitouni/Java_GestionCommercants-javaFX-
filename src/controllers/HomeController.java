/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;



import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.ConnectionUtil;

import javax.swing.table.DefaultTableModel;

/**
 * FXML Controller class
 *
 * @author oXCToo
 */
public class HomeController implements Initializable {
    DefaultTableModel model=new DefaultTableModel();


    @FXML
    private TextField txtId;
    @FXML
    private TextField txtFirstname;
    @FXML
    private TextField txtLastname;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtSearch;
    @FXML
    private TextField txtDOB;

    @FXML
    Label lblStatus;

    @FXML
    TableView tblData;
    @FXML
    private Button btnHome;
    /**
     * Initializes the controller class.
     */
    PreparedStatement preparedStatement;
    Connection connection;

    public HomeController() {
        connection = (Connection) ConnectionUtil.conDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        fetColumnList();
        fetRowList();

    }
    @FXML
    private void HandleUpdate1() {
        getData();
    }
    @FXML
    private void HandleEvents(MouseEvent event) {
        //check if not empty
        if (txtEmail.getText().isEmpty() || txtFirstname.getText().isEmpty() || txtLastname.getText().isEmpty() || txtDOB.getText().equals(null)) {
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Enter all details");
        } else {
            saveData();
        }

    }
    @FXML
    private void HandleEvents1(MouseEvent event) {
        //check if not empty
        if (txtEmail.getText().isEmpty() || txtFirstname.getText().isEmpty() || txtLastname.getText().isEmpty() || txtDOB.getText().isEmpty() | txtId.getText().isEmpty() ) {
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Enter all details");
        } else {
            UpdateRow();
        }

    }

    @FXML
    private void HandleEvents3(KeyEvent event) {
        //check if not empty
        if (txtSearch.getText().isEmpty()) {
            fetRowList();
        } else {
            Search();
        }

    }
    @FXML
    private String deleteROW(MouseEvent event) {
        List<Object> prod = new ArrayList<>();
       Object tab;
            prod= (List<Object>) tblData.getSelectionModel().getSelectedItem();

        try {
            String st = "DELETE FROM points WHERE id = ?";
            preparedStatement = (PreparedStatement) connection.prepareStatement(st);
            preparedStatement.setString(1, (String) prod.get(0));

            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Utilisateur supprimé");

            fetRowList();
            //clear fields
            clearFields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }


}
    @FXML
    private String validateROW(MouseEvent event) {
        List<Object> prod = new ArrayList<>();
        Object tab;
        prod= (List<Object>) tblData.getSelectionModel().getSelectedItem();
        String points= (String) prod.get(2);
        String montant = String.valueOf(Integer.parseInt(points)*0.2);
        try {
            String st = "UPDATE points SET points='0',montant=? WHERE id= ?";
            preparedStatement = (PreparedStatement) connection.prepareStatement(st);
            preparedStatement.setString(1, montant);
            preparedStatement.setString(2, (String) prod.get(0));
            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Conversion faite");

            fetRowList();
            //clear fields
            clearFields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }


    }

    private void clearFields() {
        txtFirstname.clear();
        txtLastname.clear();
        txtEmail.clear();
        txtDOB.clear();
        txtId.clear();

    }

    private String saveData() {

        try {
            String st = "INSERT INTO points ( nom, points, status, montant,id) VALUES (?,?,?,?,?)";
            preparedStatement =  connection.prepareStatement(st);
            preparedStatement.setString(1, txtFirstname.getText());
            preparedStatement.setString(2, txtLastname.getText());
            preparedStatement.setString(3, txtEmail.getText());
            preparedStatement.setString(4, txtDOB.getText());
            preparedStatement.setString(5, txtId.getText());
            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Utilisateur ajouté");

            fetRowList();
            //clear fields
            clearFields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }

    private String UpdateRow() {

        try {
            String st = "UPDATE points SET nom = ? , points = ? , status = ? , montant = ?, id=? WHERE id = ?";
            preparedStatement = (PreparedStatement) connection.prepareStatement(st);
            preparedStatement.setString(1, txtFirstname.getText());
            preparedStatement.setString(2, txtLastname.getText());
            preparedStatement.setString(3, txtEmail.getText());
            preparedStatement.setString(4, txtDOB.getText());
            preparedStatement.setString(5, txtId.getText());
            preparedStatement.setString(6, txtId.getText());
            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Modification prise en compte");

            fetRowList();
            //clear fields
            clearFields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }
    private String Search() {
        try {
            String st = "SELECT * FROM points WHERE nom ='"+txtSearch.getText()+"'";

            if(txtSearch.getText().length()>0)
            fetRowList(st);
            if(txtSearch.getText().isEmpty()) fetRowList();
            //clear fields
            clearFields();
            return "Success";

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }

    private String convertData() {

        try {
            String points=txtLastname.getText();
            double montant = Integer.parseInt(points)*0.2;
            String st = "UPDATE points SET points = 0 ,montant = ? WHERE nom = ?";
            preparedStatement = (PreparedStatement) connection.prepareStatement(st);
            preparedStatement.setString(1, String.valueOf(montant));
            preparedStatement.setString(2, txtFirstname.getText());



            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Modification prise en compte");

            fetRowList();
            //clear fields
            clearFields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }
    private ObservableList<ObservableList> data;
    String SQL = "SELECT * from points";

    //only fetch columns
    private void fetColumnList() {

        try {
            ResultSet rs = connection.createStatement().executeQuery(SQL);

            //SQL FOR SELECTING ALL OF CUSTOMER
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tblData.getColumns().removeAll(col);
                tblData.getColumns().addAll(col);

                System.out.println("Column [" + i + "] ");

            }

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());

        }
    }

    //fetches rows and data from the list
    private void fetRowList() {

        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = connection.createStatement().executeQuery(SQL);

            while (rs.next()) {
                //Iterate Row
                ObservableList row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);



            }

            tblData.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void fetRowList(String SQL) {

        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = connection.createStatement().executeQuery(SQL);

            while (rs.next()) {
                //Iterate Row
                ObservableList row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);



            }

            tblData.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void HandleEventsHome(MouseEvent event) {



        if (event.getSource() == btnHome) {
            //login here


            try {

                //add you loading or delays - ;-)
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/Display.fxml")));
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

        }


    }
    public void getData(){
        List<Object> prod;

        prod= (List<Object>) tblData.getSelectionModel().getSelectedItem();

        txtId.setText(String.valueOf(prod.get(0)));
        txtFirstname.setText(String.valueOf(prod.get(1)));
        txtLastname.setText(String.valueOf(prod.get(2)));
        txtEmail.setText(String.valueOf(prod.get(3)));
        txtDOB.setText(String.valueOf(prod.get(4)));


    }

}
