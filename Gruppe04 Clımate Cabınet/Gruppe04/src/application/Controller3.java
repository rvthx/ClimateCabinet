package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Controller3 implements Initializable {
	private Stage stage;
	private Scene scene;
	ControlCabinet c = new ControlCabinet();
	Alert alert = new Alert(AlertType.ERROR);
	Alert alert2 = new Alert(AlertType.INFORMATION);
	
	
	@FXML
	private AnchorPane userlogin = new AnchorPane();
	@FXML
	private AnchorPane usererstellung = new AnchorPane();
    @FXML
    private ImageView ImageView;
    @FXML
    private AnchorPane tablePingAnchor;
    @FXML
    private Label infLabel6;
    @FXML
    private TableView<tableClass> table = new TableView<tableClass>();
    @FXML
    private TableColumn<tableClass, String> OKNOKColumn;
    @FXML
    private TableColumn<tableClass, String> slotColumn;
    @FXML
    private TableColumn<tableClass, String> pruflingColumn;
    @FXML
    private TableColumn<tableClass, String> auftragColumn;
    @FXML
    private Button entfernButton;
    @FXML
    private TextField entfernField;
    @FXML
    private Button pingButton;
    @FXML
    private Pane pane;
    @FXML
    private Label infLabel12;
    @FXML
    private Label infLabel13;
    @FXML
    private Label infLabel14;
    @FXML
    private Label infLabel15;
    @FXML
    private Button goEndButton;
    @FXML
    private Label endLabel;
    @FXML
	private Button statusChange = new Button();
    @FXML
    private Button goHomeButton = new Button();
    @FXML
	private AnchorPane cabinetChose = new AnchorPane();
	@FXML
	private AnchorPane hauptMenu = new AnchorPane();
	@FXML
	private AnchorPane cabinetAdd = new AnchorPane();
	@FXML
    private Button ausloggenButton = new Button();
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
		slotColumn.setCellValueFactory(cellData -> cellData.getValue().slotColumnProperty());
		pruflingColumn.setCellValueFactory(cellData -> cellData.getValue().pruflingColumnProperty());
		auftragColumn.setCellValueFactory(cellData -> cellData.getValue().auftragColumnProperty());
		OKNOKColumn.setCellValueFactory(cellData -> cellData.getValue().OKNOKColumnProperty());
		
		for(int i=0; i< Controller2.list.size() ; i++) {
			Controller2.list.get(i).setOKNOKColumn(null);
		}
		
		table.setItems(Controller2.list);
		
		try {
			table.setOnMouseClicked(e -> {
    		events();
    	});
		}
		catch(java.lang.NullPointerException e) {
			alert.setContentText("Es gibt nichts zum Wählen.");
			alert.showAndWait();
		}
		
	}
    
    public void changeUserStatus(ActionEvent a) {
		//if user is active change it to inactive
		if(User.userStatus == "active") {
			String query = "UPDATE benutzer SET benutzer_Status = 'inactive' WHERE benutzer_Name ='"+User.userName+"'";
			try {
				DBConnection.connection.createStatement().execute(query);
				User.userStatus = "inactive";
				statusChange.setStyle("-fx-background-color:red");
				statusChange.setText("Inaktiv");
				alert2.setTitle("Informationsdialog");
				alert2.setHeaderText(null);
				alert2.setContentText("Sie sind jetzt 'inaktiv'");
				alert2.showAndWait();
			}catch(SQLException e) {
				System.out.println(e);
				return;
			}
		}
		
		//if user is inactive change it to active
		else {
			String query = "UPDATE benutzer SET benutzer_Status = 'active' WHERE benutzer_Name ='"+User.userName+"'";
			try {
				DBConnection.connection.createStatement().execute(query);
				User.userStatus = "active";
				statusChange.setStyle("-fx-background-color:green");
				statusChange.setText("Aktiv");
			}catch(SQLException e) {
				System.out.println(e);
				return;
			}
		}
	}

    //to remove the samples
    @FXML
    void pEntfern(ActionEvent event) {
    	tableClass selectedItem = table.getSelectionModel().getSelectedItem();
    	for(int i= 0; i<Controller2.list.size(); i++) {
    		if(Controller2.list.get(i).getSlotColumn().getValueSafe().compareTo(entfernField.getText()) == 0) {
    			Sample.removeSample(Controller2.list.get(i).getPruflingColumn().getValueSafe());
    			Controller2.list.remove(i);
    	        table.getItems().remove(selectedItem);
    		}
    	}
    }

    
    @FXML
    void pingTestStart(ActionEvent event) {

    	c.startPingConnection();
    	c.startSendingPing();
    	
    	for(int i= 0 ; i< Controller2.list.size() ; i++) {
    		String s1 = Controller2.list.get(i).getSlotColumn().getValueSafe();
    		int k = Integer.parseInt(s1);
    		if(Cabinet.samplePingStatus.get(k-1).compareTo("OK") == 0) {
    			tableClass temp = Controller2.list.get(i);
    			Controller2.list.remove(i);
    			tableClass tt = new tableClass("OK", temp.getSlotColumn().getValueSafe(), temp.getPruflingColumn().getValueSafe(),temp.getAuftragColumn().getValueSafe());
    			Controller2.list.add(i, tt);
    		}
    		else {
    			tableClass temp = Controller2.list.get(i);
    			Controller2.list.remove(i);
    			tableClass tt = new tableClass("NOK",temp.getSlotColumn().getValueSafe(), temp.getPruflingColumn().getValueSafe(),temp.getAuftragColumn().getValueSafe());
    			Controller2.list.add(i,tt);
    		}
    	}
    	for(int j=0 ; j < Controller2.list.size() ; j++) {
    		if(Controller2.list.get(j).getOKNOKColumn().getValueSafe() == "NOK") {
    			infLabel12.setText( "Bauteil " +  Controller2.list.get(j).getPruflingColumn().getValueSafe() + " in Slot " + Controller2.list.get(j).getSlotColumn().getValueSafe() + " ist nicht in Ordnung.");
    			pane.setVisible(true);
    			return;
    		}
    	}
    	infLabel12.setText("Alle Prüflinge sind in Ordnung.");
    	endLabel.setVisible(true);
    }
    
    public void events() {
    	entfernField.setText(table.getSelectionModel().getSelectedItem().getSlotColumn().getValueSafe());
    }
    
    //for finish the test
    @FXML
    void goEnd() {
    	for(int j=0 ; j < Controller2.list.size() ; j++) {
    		if(Controller2.list.get(j).getOKNOKColumn().getValueSafe() == "NOK") {
    			alert.setContentText("Es gibt Prüflinge, die nicht in Ordnung sind. Bitte löschen Sie sie, bevor Sie fortfahren!");
				alert.showAndWait();
				return;
    		}
    		
    		
    		Alert alert = new Alert(AlertType.CONFIRMATION, "Bist du sicher? Die Verbindung geht verloren", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
    		alert.showAndWait();

    		if (alert.getResult() == ButtonType.YES) {
    			c.stopSendingPing();
    			c.sendStopMessage();
    	
    	//tablePingAnchor.setVisible(true);
    			pane.setVisible(false);
    			pingButton.setVisible(false);
    			goEndButton.setVisible(false);
    			infLabel12.setVisible(false);
    			c.stopConnection();
    			Platform.exit();
    		}
    	}
    	
    }
    
    //to go to the main menu
    @FXML
	void goHauptmenu(ActionEvent event) throws IOException{
		Parent root = FXMLLoader.load(getClass().getResource("2.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		cabinetChose.setVisible(false);
		cabinetAdd.setVisible(false);
		hauptMenu.setVisible(true);
	}
    
    //to switch user/log out
    @FXML
	void logout(ActionEvent event) throws IOException{
    	User.logoutUser();
		Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		cabinetChose.setVisible(false);
		userlogin.setVisible(true);
		usererstellung.setVisible(false);
	}

}
