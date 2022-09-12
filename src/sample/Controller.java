package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import org.json.JSONArray;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.*;

public class Controller {
    // Data structures for PropertyAssessment
    private static ObservableList<PropertyAssessment> assessmentsList = FXCollections.observableArrayList(); // Array to store entries from Property Assessment API
    private static ObservableList<Crime> crimesList = FXCollections.observableArrayList(); // Array to store entries from Crimes Reported API
    ObservableList<String> assessmentClassList = FXCollections.observableArrayList("Residential", "Non Residential", "Other Residential"); // Assessment Class Drop-down Options
    ObservableList<PropertyAssessment> dataList = FXCollections.observableArrayList(); // Array of Properties to display
    List<Integer> assessedValues = new ArrayList<Integer>(); // Calculating statistics
    private static HttpURLConnection connection;
    int totalOccurrences = 0; // Counter for crime occurrences

    // FXML elements
    @FXML private TextField accountNumberField;
    @FXML private TextField addressField;
    @FXML private TextField neighbourhoodField;
    @FXML private ComboBox assessmentClassBox;
    @FXML private Button search;
    @FXML private Button reset;
    @FXML private Button compare;
    @FXML private Button visualize;
    @FXML private TableView table;
    @FXML private TextArea statistics;
    @FXML private TableColumn accountColumn;
    @FXML private TableColumn addressColumn;
    @FXML private TableColumn assessedValueColumn;
    @FXML private TableColumn assessmentClassColumn;
    @FXML private TableColumn neighbourhoodColumn;
    @FXML private TableColumn latitudeColumn;
    @FXML private TableColumn longitudeColumn;
    @FXML private BarChart<String, Double> barchart;
    @FXML private PieChart piechart;

    public void initialize() throws IOException {
        // Property Assessment API Pull
        System.out.println("--- Starting HTTP Operations ---");
        URL propertyUrl = new URL("https://data.edmonton.ca/resource/q7d6-ambg.json?$limit=100000"); // Change to desired limit, or remove limit to pull every entry
        URLConnection propertyRequest = propertyUrl.openConnection();
        propertyRequest.connect();

        BufferedReader propertyReader;
        String propertyLine;
        StringBuffer propertyContent = new StringBuffer();

        System.out.println("Attempting Property API pull");

        try {
            // Attempt to open URL connection and if HTTP status is in 200s, parse the data and close the connection, otherwise throw exceptions
            connection = (HttpURLConnection) propertyUrl.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            int propertyStatus = connection.getResponseCode();
            if (propertyStatus > 299) {
                propertyReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((propertyLine = propertyReader.readLine()) != null) {
                    propertyContent.append(propertyLine);
                }
                propertyReader.close();
            } else {
                propertyReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((propertyLine = propertyReader.readLine()) != null) {
                    propertyContent.append(propertyLine);
                }
                propertyReader.close();
            }
            parseProperty(propertyContent.toString());
        } catch (MalformedURLException e) {
            throw e;
        } catch (IOException ie) {
            throw ie;
        } finally {
            connection.disconnect();
        }
        System.out.println("Property API pull successful");

        // Crimes Reported API Pull
        URL crimeURL = new URL("https://dashboard.edmonton.ca/resource/xthe-mnvi.json?$where=occurrence_reported_year=2019"); // Change to desired year, or remove query to display every entry
        URLConnection crimeRequest = crimeURL.openConnection();
        crimeRequest.connect();

        BufferedReader crimeReader;
        String crimeLine;
        StringBuffer crimeContent = new StringBuffer();

        System.out.println("Attempting Crime API pull");

        try {
            // Attempt to open URL connection and if HTTP status is in 200s, parse the data and close the connection, otherwise throw exceptions
            connection = (HttpURLConnection) crimeURL.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            int crimeStatus = connection.getResponseCode();
            if (crimeStatus > 299) {
                crimeReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((crimeLine = crimeReader.readLine()) != null) {
                    crimeContent.append(crimeLine);
                }
                crimeReader.close();
            } else {
                crimeReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((crimeLine = crimeReader.readLine()) != null) {
                    crimeContent.append(crimeLine);
                }
                crimeReader.close();
            }
            parseCrime(crimeContent.toString());
        } catch (MalformedURLException e) {
            throw e;
        } catch (IOException ie) {
            throw ie;
        } finally {
            connection.disconnect();
        }
        System.out.println("Crime API pull successful");
        System.out.println("--- Ending HTTP Operations ---");

        // Initialize drop-down for Assessment Class and populate the table with no search criteria
        initializeAssessmentClass();
        onActionSearch();
    }

    // JSON Parser for PropertyAssessment
    public static String parseProperty (String responseBody){
        JSONArray assessments = new JSONArray(responseBody);
        String suite = "", streetName = "", garage = "", neighbourhood = "", ward = "", assessmentClass = "", address = "";
        int accountNumber = 0, houseNumber = 0, neighbourhoodID, assessedValue = 0;
        double latitude = 0, longitude = 0;
        int aLength = assessments.length();

        for (int i = 0; i < aLength; i++) {
            // Create a new JSON object for every entry from the API and assign each JSON value to local variables
            JSONObject assessment = assessments.getJSONObject(i);
            if (assessment.has("account_number")) { accountNumber = assessment.getInt("account_number"); }
            if (assessment.has("suite")) { suite = assessment.getString("suite"); }
            if (assessment.has("house_number")) { houseNumber = assessment.getInt("house_number"); }
            if (assessment.has("street_name")) { streetName = assessment.getString("street_name"); }
            if (assessment.has("garage")) { garage = assessment.getString("garage"); }
            if (assessment.has("neighbourhood_id")) { neighbourhoodID = assessment.getInt("neighbourhood_id"); }
            if (assessment.has("neighbourhood")) { neighbourhood = assessment.getString("neighbourhood"); }
            if (assessment.has("ward")) { ward = assessment.getString("ward"); }
            if (assessment.has("assessed_value")) { assessedValue = assessment.getInt("assessed_value"); }
            if (assessment.has("latitude")) { latitude = assessment.getDouble("latitude"); }
            if (assessment.has("longitude")) { longitude = assessment.getDouble("longitude");  }
            if (assessment.has("mill_class_1")) { assessmentClass = assessment.getString("mill_class_1"); }

            // Address concatenation
            if (suite != "") { address = address + suite + " "; }
            if (houseNumber > 0) { address = address + houseNumber + " "; }
            if (streetName != "") { address = address + streetName; }

            // Create a new Property Assessment object for every entry and add to assessmentsList and clear the values stored in the local variables
            assessmentsList.add(new PropertyAssessment(accountNumber, address, assessedValue, assessmentClass, neighbourhood, latitude, longitude));
            suite = ""; streetName = ""; garage = ""; neighbourhood = ""; ward = ""; assessmentClass = ""; address = "";
            accountNumber = 0; houseNumber = 0; neighbourhoodID = 0; assessedValue = 0;
            latitude = 0; longitude = 0;
        }
        return null;
    }

    // Parse JSON objects from crime API
    public static String parseCrime(String responseBody) {
        JSONArray crimes = new JSONArray(responseBody);
        String neighbourhood = "", incident = "";
        int year = 0, occurrences = 0;
        int cLength = crimes.length();

        for (int i = 0; i < cLength; i++) {
            // Create a new JSON object for every entry from the API and assign each JSON value to local variables
            JSONObject crime = crimes.getJSONObject(i);
            if (crime.has("neighbourhood_description_occurrence")) { neighbourhood = crime.getString("neighbourhood_description_occurrence"); }
            if (crime.has("occurrence_violation_type_group_incident")) { incident = crime.getString("occurrence_violation_type_group_incident"); }
            if (crime.has("occurrence_reported_year")) { year = crime.getInt("occurrence_reported_year"); }
            if (crime.has("occurrences")) { occurrences = crime.getInt("occurrences"); }

            // Create a new Crime object for every entry and add to crimesList
            crimesList.add(new Crime(neighbourhood, incident, year, occurrences));
        }
        return null;
    }

    // Initialize values for assessment class (residential, non residential, other residential)
    @FXML private void initializeAssessmentClass() { assessmentClassBox.getItems().addAll(assessmentClassList); }

    // Clear the values from each input field and the table when the reset button is clicked
    @FXML public void onActionReset() {
        accountNumberField.setText("");
        addressField.setText("");
        neighbourhoodField.setText("");
        assessmentClassBox.getItems().clear();
        assessmentClassBox.getItems().addAll(assessmentClassList);
        statistics.setText("");
        table.getItems().clear();
        barchart.getData().clear();
        piechart.getData().clear();
    }

    // Retrieve the Property Assessments data, search for input parameters, calculate and display statistics
    @FXML public void onActionSearch() throws FileNotFoundException {
        // Clear the data structures and input fields
        statistics.setText("");
        table.getItems().clear();
        dataList.clear();
        assessedValues.clear();
        totalOccurrences = 0;

        for (int i = 0; i < assessmentsList.size(); i++) {
            // Iterate through assessmentsList and create a new Property Assessment object for each entry
            PropertyAssessment newProperty = new PropertyAssessment(assessmentsList.get(i).getAccount(),
                    assessmentsList.get(i).getAddress(),
                    assessmentsList.get(i).getAssessedValue(),
                    assessmentsList.get(i).getAssessmentClass(),
                    assessmentsList.get(i).getNeighbourhood(),
                    assessmentsList.get(i).getLatitude(),
                    assessmentsList.get(i).getLongitude());

            // Compare values in text fields to the property assessment. If a match is found, continue to compare against the next text field.
            // Continue is used to check against the other text fields to prevent overlapping data.
            if (accountNumberField.getText() != "") {
                if (Integer.valueOf(accountNumberField.getText()) == newProperty.getAccount()) { }
                else { continue; }
            }

            if (addressField.getText() != "") {
                if (addressField.getText().equalsIgnoreCase(newProperty.getAddress())) { }
                else { continue; }
            }

            if (neighbourhoodField.getText() != "") {
                if (neighbourhoodField.getText().equalsIgnoreCase(newProperty.getNeighbourhood())) { }
                else { continue; }
            }

            if (assessmentClassBox.getValue() != null) {
                if (newProperty.getAssessmentClass().equalsIgnoreCase(String.valueOf(assessmentClassBox.getValue()))) { }
                else { continue; }
            }

            // If an entry matches any of the search criteria, add the property to dataList, and add its value to assessedValues
            dataList.add(newProperty);
            assessedValues.add(newProperty.getAssessedValue());
        }

        // Iterate through the crimesList and if the neighbourhood text field matches an entry in crimesList, keep track of the number of occurrences
        for (int i = 0; i < crimesList.size(); i++) {
            if (neighbourhoodField.getText() != "") {
                if (neighbourhoodField.getText().equalsIgnoreCase(crimesList.get(i).getNeighbourhood())) { }
                else { continue; }
            }
                totalOccurrences += crimesList.get(i).getOccurrences();
        }

        // Populate table from values of Property Assessment observable list and calculate statistics to be displayed in text area
        accountColumn.setCellValueFactory(new PropertyValueFactory<PropertyAssessment, Integer>("account"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<PropertyAssessment, String>("address"));
        assessedValueColumn.setCellValueFactory(new PropertyValueFactory<PropertyAssessment, Integer>("assessedValue"));
        assessmentClassColumn.setCellValueFactory(new PropertyValueFactory<PropertyAssessment, String>("assessmentClass"));
        neighbourhoodColumn.setCellValueFactory(new PropertyValueFactory<PropertyAssessment, String>("neighbourhood"));
        latitudeColumn.setCellValueFactory(new PropertyValueFactory<PropertyAssessment, Double>("latitude"));
        longitudeColumn.setCellValueFactory(new PropertyValueFactory<PropertyAssessment, Double>("longitude"));
        table.setItems(dataList);

        calculate();
    }

    // Calculate the statistical data for the assessed values added into list of Property Assessments
    public void calculate() {
        int n = dataList.size();
        int min, max, range, mean, sd, median;

        // Calculate min, max, range
        Collections.sort(assessedValues);
        min = Collections.min(assessedValues);
        max = Collections.max(assessedValues);
        range = max - min;

        // Calculate mean
        double sum = 0;
        for (Integer value : assessedValues) {
            sum += value;
        }
        mean = (int) Math.round(sum / n);

        // Calculate standard deviation
        double sdSum = 0;
        for (Integer value : assessedValues) {
            sdSum += Math.pow(value - mean, 2);
        }
        sd = (int) Math.round(Math.sqrt(sdSum / n));

        // Calculate median
        int half = n / 2;
        if (n % 2 == 0) {
            median = (assessedValues.get(half) + assessedValues.get(half-1)) / 2;
        } else {
            median = (assessedValues.get(half));
        }

        // Display formatted statistics in the text area
        statistics.setText("Statistics of Assessed Values: \n\n" +
                "Number of properties: " + NumberFormat.getIntegerInstance().format(n) + "\n" +
                "Min: $" + NumberFormat.getIntegerInstance().format(min)+ "\n" +
                "Max: $" + NumberFormat.getIntegerInstance().format(max) + "\n" +
                "Range: $" + NumberFormat.getIntegerInstance().format(range) + "\n" +
                "Mean: $" + NumberFormat.getIntegerInstance().format(mean) + "\n" +
                "Median: $" + NumberFormat.getIntegerInstance().format(median) + "\n" +
                "Standard deviation: $" + NumberFormat.getIntegerInstance().format(sd) + "\n" +
                "Total crime occurrences: " + totalOccurrences);
    }

    // Create bar chart to compare number of properties and crime occurrences in specific neighbourhoods
    public void compare() {
        String neighbourhood = neighbourhoodField.getText();
        Integer total = 0;
        Integer crimes = 0;
        Integer n = assessedValues.size();

        for (int i = 0; i < assessedValues.size(); i++) {
            total += assessedValues.get(i);
        }

        for (int i = 0; i < crimesList.size(); i++) {
            if (neighbourhood.equalsIgnoreCase(crimesList.get(i).getNeighbourhood())) {
                crimes += crimesList.get(i).getOccurrences();
            }
        }

        // Create new series for X and Y values and populate the bar chart
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        series1.getData().add(new XYChart.Data(neighbourhood, n));
        series2.getData().add(new XYChart.Data(neighbourhood, crimes));
        barchart.getData().addAll(series1);
        barchart.getData().addAll(series2);

    }

    // Create pie chart to visualize each neighbourhoods' amount of crime compared to total
    public void visualize() {
        // Data structures for the list of neighbourhoods and the number of crime occurrences reported
        List<String> neighbourhoods = new ArrayList<>();
        List<Integer> allOccurrences = new ArrayList<Integer>();
        String outputString = "";

        // Iterate through crimesList and add each neighbourhood to the list of neighbourhoods
        for (int i = 0; i < crimesList.size(); i++) {
            if (neighbourhoods.contains(crimesList.get(i).getNeighbourhood()) ) { }
            else {neighbourhoods.add(crimesList.get(i).getNeighbourhood());}
        }

        // Iterate through the list of neighbourhoods and count the number of occurrences in each neighbourhood
        for (int j = 0; j < neighbourhoods.size(); j++) {
            int someOccurrences = 0;
            for (int k = 0; k < crimesList.size(); k++) {
                if (neighbourhoods.get(j).equalsIgnoreCase(crimesList.get(k).getNeighbourhood())) {
                    someOccurrences += crimesList.get(k).getOccurrences();
                }
            }
            allOccurrences.add(j, someOccurrences);
        }

        // Clear the text area and create data for the pie chart
        statistics.clear();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (int x = 0; x < neighbourhoods.size(); x++) {
            PieChart.Data newData = new PieChart.Data(neighbourhoods.get(x), allOccurrences.get(x));
            pieChartData.add(newData);
            outputString = outputString + neighbourhoods.get(x) + " = " + allOccurrences.get(x) + "\n";
        }

        // Populate the text area with list of neighbourhoods and number of crime occurrences and populate the pie chart with data
        statistics.setText(outputString);
        piechart.setData(pieChartData);

    }
}