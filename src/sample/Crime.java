package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

// Crime class for populating data into the table
// Consists of neighbourhood, incident type, year
public class Crime {
    public SimpleStringProperty neighbourhood;
    public SimpleStringProperty incident;
    public SimpleIntegerProperty year;
    public SimpleIntegerProperty occurrences;

    // Constructor for Crime
    Crime(String neighbourhood, String incident, Integer year, Integer occurrences) {
        this.neighbourhood = new SimpleStringProperty(neighbourhood);
        this.incident = new SimpleStringProperty(incident);
        this.year = new SimpleIntegerProperty(year);
        this.occurrences = new SimpleIntegerProperty(occurrences);
    }

    // Getter and setter methods to get and set neighbourhood, incident, year
    public String getNeighbourhood() { return neighbourhood.get(); }
    public void setNeighbourhood(String neighbourhood) { this.neighbourhood = new SimpleStringProperty(neighbourhood); }

    public String getIncident() { return incident.get(); }
    public void setIncident(String incident) { this.incident = new SimpleStringProperty(incident); }

    public Integer getYear() { return year.get(); }
    public void setYear(Integer year) { this.year = new SimpleIntegerProperty(year); }

    public Integer getOccurrences() { return occurrences.get(); }
    public void setOccurrences(Integer occurrences) { this.occurrences = new SimpleIntegerProperty(occurrences); }

}
