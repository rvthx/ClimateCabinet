package application;


import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;


public class Controller implements Initializable{
	private Stage stage;
	private Scene scene;
	boolean flag = false;
	Alert alert = new Alert(AlertType.ERROR);
	Alert alert2 = new Alert(AlertType.INFORMATION);
	ControlCabinet c = new ControlCabinet();
	
	@FXML
	private Button loginButton = new Button();
	@FXML
	private Button anmeldenButton = new Button();
	@FXML
	private Button switchButton = new Button();
	@FXML
	private TextField nameField = new TextField();
	@FXML
	private PasswordField passwortField = new PasswordField();
	@FXML
	private TextField anmeldenName = new TextField();
	@FXML
	private PasswordField anmeldenPasswort = new PasswordField();
	@FXML
	private AnchorPane userlogin = new AnchorPane();
	@FXML
	private AnchorPane usererstellung = new AnchorPane();
	@FXML
	private AnchorPane cabinetChose = new AnchorPane();
	@FXML
	private AnchorPane hauptMenu = new AnchorPane();
	@FXML
	private AnchorPane cabinetAdd = new AnchorPane();
	@FXML
	private CheckBox status = new CheckBox();
	@FXML
	private RadioButton adminR = new RadioButton();
	@FXML
	private RadioButton testerR = new RadioButton();
	@FXML
	private Button statusChange = new Button();
	@FXML
	private ChoiceBox<String> cabinetBox = new ChoiceBox<String>();
	@FXML
	private Button cabinetButton = new Button();
	@FXML
	private ChoiceBox<String> cabinetBox2 = new ChoiceBox<String>();
	@FXML
	private Button schrankAddButton = new Button();
	@FXML
	private Button schrankHinzButton = new Button();
	@FXML
	private TextField schrankIDField = new TextField();
	@FXML
	private Button choseButton = new Button();
	@FXML
	private Label infLabel = new Label();
	@FXML
	private Label infLabel2 = new Label();
	@FXML
	private Label infLabel3 = new Label();
	@FXML
	private Button testBetriebButton = new Button();
	@FXML
	private Button finishButton = new Button();
	@FXML
	private Label infLabel5 = new Label();
	@FXML
	private Button startButton = new Button();
	@FXML
	private Button pChangeStatusButton = new Button();
	@FXML
	private Button pAddButton = new Button();
	@FXML
	private Button pRemoveButton = new Button();
	@FXML
	private Button pUpdateButton = new Button();
	@FXML
	private TextField IDField = new TextField();
	@FXML
	private RadioButton pAktive = new RadioButton();
	@FXML
	private RadioButton pInactive = new RadioButton();
	@FXML
	private TextField altIDField = new TextField();
	@FXML
	private TextField neuIDField = new TextField();
	@FXML
	private Button pruflingScene = new Button();
	@FXML
	private AnchorPane setupAnchor = new AnchorPane();
	@FXML
	private AnchorPane pruflingAnchor = new AnchorPane();
	@FXML
	private Button goBackButton = new Button();
	@FXML
	private AnchorPane auftragAnchor = new AnchorPane();
	@FXML
	private Button goBackButton2 = new Button();
	@FXML
	private Button aAddButton = new Button();
	@FXML
	private TextField auftragNummerField = new TextField();
	@FXML
	private TextField pruflingNummerField = new TextField();
	@FXML
	private Button beginnInitButton = new Button();;
	@FXML
	private Button fertigButton = new Button();
    @FXML
    private AnchorPane tableAnchor;
    @FXML
    private TableView<tableClass> table = new TableView<tableClass>();
    @FXML
    private TableColumn<tableClass, String> slotColumn;
    @FXML
    private TableColumn<tableClass, String> pruflingColumn;
    @FXML
    private TableColumn<tableClass, String> auftragColumn;
    @FXML
	private TextField slotNummerField = new TextField();
    @FXML 
    private Label slot1 = new Label();
    @FXML 
    private Label prufling1 = new Label();
    @FXML 
    private Label auftrag1 = new Label();
    @FXML
    private Button goHomeButton = new Button();
    @FXML
    private Button ausloggenButton = new Button();
    @FXML
	private ListView<String> myListView;
			
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		String control = "SELECT schrank_Seriennummer FROM schrank";
		try {
			ResultSet rs = DBConnection.connection.createStatement().executeQuery(control);
			while (rs.next()) { 
			    Cabinet.cabinet.add(rs.getString(1));
			}
			cabinetBox.getItems().addAll(Cabinet.cabinet);
			cabinetBox2.getItems().addAll(Cabinet.cabinet);
			cabinetBox2.setOnAction(this::getCabinet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getCabinet(ActionEvent event) {
		String myCabinet = cabinetBox2.getValue();
		schrankIDField.setText(myCabinet);
	}
	
	//to select the cabinet
	public void cabinetAndUser(ActionEvent Event) {
		if(cabinetBox.getValue() == null) {
			alert.setContentText("Kein Schrank ist gewählt.");
			alert.showAndWait();
			return;
		}
		else {
			String myCabinet = cabinetBox.getValue();
			User.selectCabinet(myCabinet);
			infLabel2.setVisible(true);
			initialize(null, null);
		}
			
	}
	
	
	//userlogin
	public void login(ActionEvent event) throws IOException {
		if( nameField.getText() == "" || passwortField.getText() == "") {
			alert.setContentText("Alle * enthaltenden Felder müssen ausgefüllt werden!");
			alert.showAndWait();
		}
		 
		else {
			int result = User.loginUser(nameField.getText(), passwortField.getText());
			if(result == -1) {
				alert.setContentText("Es gibt keinen Benutzer mit diesem Namen!");
				alert.showAndWait();
			}
			if(result == -3) {
				alert.setContentText("Es gibt derzeit einen Benutzer, der schon sich angemeldet!");
				alert.showAndWait();
			}
			if(result == -4) {
				alert.setContentText("Ihr Passwort ist falsch.");
				alert.showAndWait();
			}
		
			if(result == 0) {
				User.userStatus = "active";
				cabinetChose.setVisible(true);
				cabinetAdd.setVisible(false);
				if(User.userStatus == "active") {
					statusChange.setStyle("-fx-background-color:green");
					statusChange.setText("Active");
					}
				Parent root = FXMLLoader.load(getClass().getResource("2.fxml"));
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			}
		}
	}
	
	//new user
	public void addUser(ActionEvent a) {
		
		if(anmeldenName.getText() == "" || anmeldenPasswort.getText() == "" || User.userRole == "" || User.userStatus == "") {
			alert.setContentText("Alle * enthaltenden Felder müssen ausgefüllt werden!");
			alert.showAndWait();
		}
		
		if(status.isSelected() == true)
			User.userStatus = "Active";
		else
			User.userStatus = "Inactive";
		
		if(testerR.isSelected() == true)
			User.userRole = "TESTER";
		else if(adminR.isSelected() == true)
			User.userRole = "ADMIN";
		
		int result = User.addUser(anmeldenName.getText(), anmeldenPasswort.getText(), User.userRole, User.userStatus);
		if(result == -1) {
			alert.setContentText("Es gibt schon einen Benutzer mit diesem Namen!");
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
	
	//new cabinet add
	public void cabinetAdd(ActionEvent a) {
		int result = Cabinet.addCabinet(schrankIDField.getText());
		if(result == -1) {
			alert.setContentText("Es gibt schon einen Schrank mit diesem Namen!");
			alert.showAndWait();
		}
		if(result == 0) {
			infLabel.setVisible(true);
		}
	}
	
	//go to the other page to start the test
	public void testStart(ActionEvent a) throws IOException {
		if(infLabel2.isVisible() == true) {
			Parent root = FXMLLoader.load(getClass().getResource("4.fxml"));
			stage = (Stage)((Node)a.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
		else {
			alert.setContentText("Bitte wählen Sie zuerst einen Klimaschrank.");
			alert.showAndWait();
		}
	}
	
	//new sample add
	public void pruflingAdd(ActionEvent a) {
		if(IDField.getText() == "" || pAktive.isSelected() == false) {
			if (pInactive.isSelected() == false) {
				alert.setContentText("Alle Felder müssen ausgefüllt werden!");
				alert.showAndWait();
			}
		}
		
		else {
			if(pAktive.isSelected() == true) 
				Sample.sampleStatus = "Active";
			else if( pInactive.isSelected() == true) 
				Sample.sampleStatus = "Inactive";	
		
			int result = Sample.addSample(IDField.getText(), Sample.sampleStatus);
			if(result == -1) {
				alert.setContentText("Es gibt schon eine Prüfling mit diesem ID.");
				alert.showAndWait();
			}
			if(result == 0) {
				infLabel3.setText("Die Prüfling ist erfolgreich hinzugefügt.");
			}
		}
	}
	
	//sample remove
	public void pruflingRemove(ActionEvent a) {
		int result = Sample.removeSample(IDField.getText());
		if(result == 0) {
			infLabel3.setText("Die Prüfling ist erfolgreich gelöscht.");
		}
		else if(result == -2) {
			infLabel3.setText("Es gibt keine Prüfling mit diesem ID.");
		}
	}
	
	//sampleID change
	public void pruflingIDChange(ActionEvent a) {
		if(altIDField.getText() == "" || neuIDField.getText() == "" ) {
			alert.setContentText("Alle Felder müssen ausgefüllt werden!");
			alert.showAndWait();
		}
		else {
			int result = Sample.updateSampleID(altIDField.getText(), neuIDField.getText());
			if(result == -1) {
				alert.setContentText("Es gibt bereits eine Prüfling für diese ID.");
				alert.showAndWait();
			}
		}
	}
	
	//sample status change
	public void pruflingStatusChange(ActionEvent a) {
		int result = -5;
		if(pAktive.isSelected() == true){
			if(User.userStatus == "Active") {
				alert.setContentText("Diese Prüfling ist schon aktiv.");
				alert.showAndWait();
			}
			else 
				result = Sample.changeSampleStatus(IDField.getText(), "Aktive");
		}
		if(pInactive.isSelected() == true) {
			if(User.userStatus == "Inactive") {
				alert.setContentText("Diese Prüfling ist schon inaktiv.");
				alert.showAndWait();
			}
			else
				result = Sample.changeSampleStatus(IDField.getText(), "Inaktive");
		}
		if(result == 0) {
			infLabel3.setText("Die Status von der Prüfling ist erfolgreich geändert.");
		}
		
	}
	
	//to go to other pages
	public void goTestBetrieb() {
		cabinetChose.setVisible(true);
		cabinetAdd.setVisible(true);
		hauptMenu.setVisible(false);
	}
	
	public void switchScene() {
		userlogin.setVisible(false);
		usererstellung.setVisible(true);
	}
	
	public void switchScene2() {
		userlogin.setVisible(true);
		usererstellung.setVisible(false);
	}
	
	public void switchScene3() {
		cabinetChose.setVisible(false);
		cabinetAdd.setVisible(true);
		hauptMenu.setVisible(false);
	}
	
	public void switchScene4() {
		cabinetChose.setVisible(true);
		hauptMenu.setVisible(false);
	}
	
	public void finish() {
		User.logoutUser();
		c.sendStopMessage();
		c.stopConnection();
		Platform.exit();
	}
	

	public void switchScene6(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("4.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
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
