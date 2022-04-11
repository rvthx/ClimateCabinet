package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Controller2 implements Initializable {
	private Stage stage;
	private Scene scene;
	ControlCabinet c = new ControlCabinet();
	Sample s = new Sample();
	Alert alert = new Alert(AlertType.ERROR);
	Alert alert2 = new Alert(AlertType.INFORMATION);
	float lastTemp = 0;	
	boolean flag = false;
	
	
	@FXML
	private AnchorPane userlogin = new AnchorPane();
	@FXML
	private AnchorPane usererstellung = new AnchorPane();
    @FXML
    private ImageView ImageView;
    @FXML
    private AnchorPane auftragAnchor;
    @FXML
    private TextField auftragNameField;
    @FXML
    private Button aAddButton;
    @FXML
    private Label infLabel31;
    @FXML
    private Label beginLabel;
    @FXML
    private Button goBackButton2;
    @FXML
    private Label infLabel4;
    @FXML
    private AnchorPane tableAnchor;
    @FXML
    private Label prufling1;
    @FXML
    private Label auftrag1;
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
    private AnchorPane setupAnchor;
    @FXML
    private Label infLabel;
    @FXML
    private TextField pruflingNummerField;
    @FXML
    private TextField auftragNummerField;
    @FXML
    private Button pruflingScene;
    @FXML
    private Button goAuftragButton;
    @FXML
    private Button beginnInitButton;
    @FXML
    private Button fertigButton;
    @FXML
    private TextField slotNummerField;
    @FXML
    private Button goBackButton3;
    @FXML
    private Button pretestButton;
    @FXML
    private Label infLabel6;
    @FXML
    private Button entfernButton;
    @FXML
    private TextField entfernField;
    @FXML
    private Button burninTestButton;
    @FXML
    private ImageView image2;
    @FXML
    private AnchorPane burinAnchor;
    @FXML
    private TextField zTempField;
    @FXML
    private TextField zZeitField;
    @FXML
    private TextField tRateField;
    @FXML
    private TextField fRateField;
    @FXML
    private Label infLabel8;
    @FXML
    private Label infLabel9;
    @FXML
    private ProgressBar pBar;
    @FXML
    private Button burnButton;
    @FXML
    private Label infLabel10;
    @FXML
    private Label infLabel11;
    @FXML
    private Button weiterButton;
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
	@FXML
	private Label sTempLabel = new Label();
	@FXML
	private Label sollTempLabel = new Label();
	@FXML
	private Label infLabel12 = new Label();
	@FXML
	private Button startBurnButton2 = new Button();
	
    final static ObservableList<tableClass> list = FXCollections.observableArrayList(
    		);
	
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		slotColumn.setCellValueFactory(cellData -> cellData.getValue().slotColumnProperty());
		pruflingColumn.setCellValueFactory(cellData -> cellData.getValue().pruflingColumnProperty());
		auftragColumn.setCellValueFactory(cellData -> cellData.getValue().auftragColumnProperty());
		OKNOKColumn.setCellValueFactory(cellData -> cellData.getValue().OKNOKColumnProperty());
		table.setItems(list);
		
		try {
			table.setOnMouseClicked(e -> {
    		events();
    	});
		}
		catch(java.lang.NullPointerException e) {
			alert.setContentText("Es gibt nichts zum Wählen.");
			alert.showAndWait();
		}
		burninTestButton.setVisible(false);
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
    
    //adding new orders
    @FXML
    void auftragAdd(ActionEvent event) {
    	if(auftragNameField.getText() == "") {
			alert.setContentText("Alle Felder müssen ausgefüllt werden!");
			alert.showAndWait();
		}
		else {
			int result = Order.addOrder(auftragNameField.getText());
			if(result == -1) {
				alert.setContentText("Es gibt schon einen Auftrag mit diesem Name.");
				alert.showAndWait();
			}
			else if(result == 0) {
				infLabel4.setText("Der Auftrag ist erfolgreich hinzugefügt.");
			}
		}
    }

    //this function takes from user the order, sample and slot number (to put the sample in)
    @FXML
    void beginnInitialisierung(ActionEvent event) {
    	int result2 = -5;
		if(flag == false) { //this flag helps us to call start message function only one  time.
			c.sendStartMessage();
			flag = true;
		}
		
		if(auftragNummerField.getText() == "" || pruflingNummerField.getText() == "" || slotNummerField.getText() == "") {
			alert.setContentText("Alle Felder müssen ausgefüllt werden!");
			alert.showAndWait();
		}
		
		else if(slotNummerField.getText().matches("[0-9]+")) {
			if(Integer.parseInt(slotNummerField.getText()) > 20 || Integer.parseInt(slotNummerField.getText()) < 0 ) {
				alert.setContentText("Falsche Slotnummer!");
				alert.showAndWait();
			}
			else {
				int result = Order.selectOrder(auftragNummerField.getText());
				if(result == -2) {
					alert.setContentText("Es gibt keinen Auftrag mit dieser Nummer.");
					alert.showAndWait();
				}
				else if(result == 0)
					result2 = Sample.selectSample(pruflingNummerField.getText());
					if(result2 == -3 ) {
						alert.setContentText("Sie haben die maximale Slotnummer erreicht.");
						alert.showAndWait();
					}
					else if(result2 == -2) {
						alert.setContentText("Es gibt keine Prüfling mit dieser Nummer.");
						alert.showAndWait();
					}
					else if(result2 == 0) {
						for(int n=0; n <list.size() ; n++) {
							if(list.get(n).getSlotColumn().getValueSafe().compareTo(slotNummerField.getText()) == 0 ) {
								alert.setContentText("Diese Slotnummer ist belegt.");
								alert.showAndWait();
								return;
							}
							else if(list.get(n).getPruflingColumn().getValueSafe().compareTo(pruflingNummerField.getText()) == 0) {
								alert.setContentText("Diese Prüfling befindet sich schon in einem Slot.");
								alert.showAndWait();
								return;
							}
						}
						c.initializeSample(Integer.parseInt(slotNummerField.getText()));
						tableClass t = new tableClass ("",slotNummerField.getText(), pruflingNummerField.getText(), auftragNummerField.getText());
						list.add(t);
						infLabel12.setText("Prüfling " + list.size() + " erfolgreich hinzugefügt");
					
					}	
				}
			}
		else {
			alert.setContentText("Slotnummer sollte nur aus Nummern bestehen.");
			alert.showAndWait();
		}
		infLabel6.setText("Alle " + list.size() + " Geräte sind erfasst.");
    }
    
    @FXML
    void pretestStart(ActionEvent event) {
    	String s = c.startVortest();
    	infLabel6.setText(s);
    	c.startPretest();
    	
    	for(int i= 0 ; i< list.size() ; i++) {
    		String s1 = list.get(i).getSlotColumn().getValueSafe();
    		int k = Integer.parseInt(s1);
    		if(Cabinet.sampleStatus.get(k-1).compareTo("NOK") == 0) {
    			tableClass temp = list.get(i);
    			list.remove(i);
    			tableClass tt = new tableClass("NOK", temp.getSlotColumn().getValueSafe(), temp.getPruflingColumn().getValueSafe(),temp.getAuftragColumn().getValueSafe());
    			list.add(i, tt);
    			infLabel6.setText("Fehlerhafte Prüflinge erkennt!");
    		}
    		else {
    			tableClass temp = list.get(i);
    			list.remove(i);
    			tableClass tt = new tableClass("OK",temp.getSlotColumn().getValueSafe(), temp.getPruflingColumn().getValueSafe(),temp.getAuftragColumn().getValueSafe());
    			list.add(i,tt);
    		}
    	}
    	for(int j=0 ; j < list.size() ; j++) {
    		if(list.get(j).getOKNOKColumn().getValueSafe() == "NOK") {
    			return;
    		}
    	}
    	infLabel6.setText("Alle Prüflinge sind OK!");
    	burninTestButton.setVisible(true);
    	pretestButton.setVisible(false);
    		
    }
    
    //to remove samples
    @FXML
    void pEntfern(ActionEvent event) {
    	tableClass selectedItem = table.getSelectionModel().getSelectedItem();
    	for(int i= 0; i<list.size(); i++) {
    		if(list.get(i).getSlotColumn().getValueSafe().compareTo(entfernField.getText()) == 0) {
    			Sample.removeSample(list.get(i).getPruflingColumn().getValueSafe());
    			list.remove(i);
    	        table.getItems().remove(selectedItem);
    		}
    	}
    }
    
    //this function is for taking the selected slot number and showing it in the textfield
    public void events() {
    	entfernField.setText(table.getSelectionModel().getSelectedItem().getSlotColumn().getValueSafe());
    }
    
    
    @FXML
    void burninTestStart(ActionEvent event) throws IOException {
    	//this part checks if there is a faulty sample, if it is, it does not go to the next page until the faulty sample removed.
    	for(int i = 0; i < list.size() ; i++) {
    		if(list.get(i).getOKNOKColumn().getValueSafe().compareTo("NOK") == 0) {
    			alert.setContentText("Sie müssen zuerst fehlerhafte Prüflinge entfernen.");
				alert.showAndWait();
				return;
    		}
    	}
    	burninTestButton.setVisible(false);
    	pretestButton.setVisible(true);
    	c.stopPretest();
    	setupAnchor.setVisible(false);
		auftragAnchor.setVisible(false);
		tableAnchor.setVisible(false);
		burinAnchor.setVisible(true);
		c.startBurninTest();
    	
		
    }
   
    @FXML
    void startBurn(ActionEvent event) throws InterruptedException {
    	c.setToleranceRate(tRateField.getText());
    	c.setFailureRate(fRateField.getText());
    	c.setTargetTime(zZeitField.getText());
    	c.setTargetTemperature(zTempField.getText());
    	String s = c.setTemperature().replaceAll("[^0-9]", "");
    	String s1 = s.substring(0, 2);
    	String s2 = s.substring(2, s.length()-1);
    	String temp = s1 + "." + s2;
    	infLabel9.setText("Zimmer Temperatur: " + temp + "°C");
    	sTempLabel.setText("Start Temperatur: " + temp);
    	sollTempLabel.setText("SOLL Temperatur: " + zTempField.getText() );
    	beginLabel.setVisible(true);
    	startBurnButton2.setVisible(true);
    }
    
     @FXML
     void startBurn2() {
    	ArrayList<Float> aL = new ArrayList<Float>();
     	aL = c.askTemperature();
     	if(aL == null) {
     		alert.setContentText("Etwas ist schief gelaufen. Bitte versuche es erneut.");
			alert.showAndWait();
			return;
     	}
     	final ArrayList<Float> it = aL;
     	for(int n= 0 ; n< aL.size() ; n++) {
     		it.set(n, aL.get(n));
     	}
     	//we used Thread here, because without the Thread the progress bar didn't get full step by step, it got full at once
     	new Thread(() -> {
     	    for (int i = 0; i < it.size(); i++) {
     	        try {
     	            Thread.sleep(600);
     	        } catch (InterruptedException e) {
     	            e.printStackTrace();
     	        }
     	        final double progress = it.get(i) / Integer.parseInt(zTempField.getText());
     	        pBar.setProgress(progress);
     	    }
     	}).start();
     	
     	lastTemp = it.get(it.size()-1);
     	infLabel10.setText("Burn-In Test war erfolgreich! Erreichte Temperatur: " + lastTemp );
     }
    
     //the other codes are for changing the page
    @FXML
    void goPing(ActionEvent event) throws IOException {
    	
    	try {
    		infLabel10.setText("IST Temperatur: " + lastTemp);
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Parent root = FXMLLoader.load(getClass().getResource("5.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
    }
 
    	
    @FXML
    void goNext(ActionEvent event) {
    	c.initializeSampleClose();
    	burinAnchor.setVisible(false);
		setupAnchor.setVisible(false);
		auftragAnchor.setVisible(false);
		tableAnchor.setVisible(true);
    }

    @FXML
    void switchScene5(ActionEvent event) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("3.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
    }

    @FXML
    void switchScene6(MouseEvent event) {

    }

    @FXML
    void switchScene7(ActionEvent event) {
    	setupAnchor.setVisible(true);
		auftragAnchor.setVisible(false);
		burinAnchor.setVisible(false);
    }

    @FXML
    void switchScene8(ActionEvent event) {
    	tableAnchor.setVisible(false);
		setupAnchor.setVisible(false);
		auftragAnchor.setVisible(true);
		burinAnchor.setVisible(false);
    }
    
    @FXML
    void switchScene9(ActionEvent event) {
    	tableAnchor.setVisible(false);
		setupAnchor.setVisible(true);
		auftragAnchor.setVisible(false);
		burinAnchor.setVisible(false);
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

