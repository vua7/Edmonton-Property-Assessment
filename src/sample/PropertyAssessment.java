package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

// PropertyAssessment class for populating data into the table
// Consists of account, address, assessed value, assessment class, neighbourhood, latitude, longitude
public class PropertyAssessment {
    public SimpleIntegerProperty accountNumber;
    public SimpleStringProperty address;
    public SimpleIntegerProperty assessedValue;
    public SimpleStringProperty assessmentClass;
    public SimpleStringProperty neighbourhood;
    public SimpleDoubleProperty latitude;
    public SimpleDoubleProperty longitude;

    // Constructor for PropertyAssessment
    PropertyAssessment(Integer account, String address, Integer assessedValue, String assessmentClass, String neighbourhood, Double latitude, Double longitude) {
        this.accountNumber = new SimpleIntegerProperty(account);
        this.address = new SimpleStringProperty(address);
        this.assessedValue = new SimpleIntegerProperty(assessedValue);
        this.assessmentClass = new SimpleStringProperty(assessmentClass);
        this.neighbourhood = new SimpleStringProperty(neighbourhood);
        this.latitude = new SimpleDoubleProperty(latitude);
        this.longitude = new SimpleDoubleProperty(longitude);
    }

    // Getter and setter methods to get and set account, address, assessed value, assessment class, neighbourhood, latitude and longitude
    public int getAccount() { return accountNumber.get(); }
    public void setAccount(Integer account) { this.accountNumber = new SimpleIntegerProperty(account); }

    public String getAddress() { return address.get(); }
    public void setAddress(String address) { this.address = new SimpleStringProperty(address); }

    public Integer getAssessedValue() { return assessedValue.get(); }
    public void setAssessedValue(Integer assessedValue) { this.assessedValue = new SimpleIntegerProperty(assessedValue); }

    public String getAssessmentClass() { return assessmentClass.get(); }
    public void setAssessmentClass(String assessmentClass) { this.assessmentClass = new SimpleStringProperty(assessmentClass); }

    public String getNeighbourhood() { return neighbourhood.get(); }
    public void setNeighbourhood(String neighbourhood) { this.neighbourhood = new SimpleStringProperty(neighbourhood); }

    public Double getLatitude() { return latitude.get(); }
    public void setLatitude(Double latitude) { this.latitude = new SimpleDoubleProperty(latitude); }

    public Double getLongitude() { return longitude.get(); }
    public void setLongitude(Double longitude) { this.longitude = new SimpleDoubleProperty(longitude); }

}