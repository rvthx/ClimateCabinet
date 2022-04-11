package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

//This class is made for Tables in User Interface

public class tableClass {
    private ImageView image = new ImageView();
	private StringProperty slotColumn = new SimpleStringProperty();
	private StringProperty pruflingColumn = new SimpleStringProperty();
	private StringProperty auftragColumn = new SimpleStringProperty();
	private StringProperty OKNOKColumn = new SimpleStringProperty();
	
	public StringProperty slotColumnProperty() {
	    return slotColumn;
	}
	public StringProperty pruflingColumnProperty() {
	   return pruflingColumn;
	}
	public StringProperty auftragColumnProperty() {
		   return auftragColumn;
	}
    
	public StringProperty OKNOKColumnProperty() {
		   return OKNOKColumn;
	}
	
    public void setSlotColumn(StringProperty slotColumn) {
		this.slotColumn = slotColumn;
	}

	public void setPruflingColumn(StringProperty pruflingColumn) {
		this.pruflingColumn = pruflingColumn;
	}

	public void setAuftragColumn(StringProperty auftragColumn) {
		this.auftragColumn = auftragColumn;
	}
	
	public tableClass(String OKNOKColumn) {
    	this.OKNOKColumn = new SimpleStringProperty(OKNOKColumn);
    }
	
	public tableClass(String slotColumn, String pruflingColumn, String auftragColumn) {
    	this.slotColumn = new SimpleStringProperty(slotColumn);
    	this.pruflingColumn = new SimpleStringProperty(pruflingColumn);
    	this.auftragColumn = new SimpleStringProperty(auftragColumn);
    }
	
	public tableClass(String OKNOKColumn,String slotColumn, String pruflingColumn, String auftragColumn) {
		this.OKNOKColumn = new SimpleStringProperty(OKNOKColumn);
		this.slotColumn = new SimpleStringProperty(slotColumn);
    	this.pruflingColumn = new SimpleStringProperty(pruflingColumn);
    	this.auftragColumn = new SimpleStringProperty(auftragColumn);
    }
	
	public tableClass(ImageView image,String slotColumn, String pruflingColumn, String auftragColumn) {
		this.image = image;
		this.slotColumn = new SimpleStringProperty(slotColumn);
    	this.pruflingColumn = new SimpleStringProperty(pruflingColumn);
    	this.auftragColumn = new SimpleStringProperty(auftragColumn);
    }
    
	public StringProperty getSlotColumn() {
		return slotColumn;
	}

	public StringProperty getPruflingColumn() {
		return pruflingColumn;
	}

	public StringProperty getAuftragColumn() {
		return auftragColumn;
	}
	public StringProperty getOKNOKColumn() {
		return OKNOKColumn;
	}
	public void setOKNOKColumn(StringProperty oKNOKColumn) {
		OKNOKColumn = oKNOKColumn;
	}
	public ImageView getImage() {
		return image;
	}
	public void setImage(ImageView image) {
		this.image = image;
	}
	
}
