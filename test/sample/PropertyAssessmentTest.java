package sample;

import static org.junit.jupiter.api.Assertions.*;

class PropertyAssessmentTest {
    private PropertyAssessment p1;
    private PropertyAssessment p2;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        p1 = new PropertyAssessment(8206575, "", 8500, "Non Residential", "STRATHCONA", 53.5267031284497, -113.4872564509870);
        p2 = new PropertyAssessment(7875495, "4016 120 STREET NW", 520000, "Residential", "ASPEN GARDENS", 53.4771560788491, -113.541589215624);
    }

    @org.junit.jupiter.api.Test
    void getAccount() {
        System.out.println(p1.getAccount());
    }

    @org.junit.jupiter.api.Test
    void setAccount() {
        p1.setAccount(7875495);
        assertEquals(p2.getAccount(), p1.getAccount());
    }

    @org.junit.jupiter.api.Test
    void getAddress() {
        System.out.println(p2.getAddress());
    }

    @org.junit.jupiter.api.Test
    void setAddress() {
        p1.setAddress("4016 120 STREET NW");
        assertEquals(p2.getAddress(), p1.getAddress());
    }

    @org.junit.jupiter.api.Test
    void getAssessedValue() {
        System.out.println(p1.getAssessedValue());
    }

    @org.junit.jupiter.api.Test
    void setAssessedValue() {
        p1.setAssessedValue(520000);
        assertEquals(p2.getAssessedValue(), p1.getAssessedValue());
    }

    @org.junit.jupiter.api.Test
    void getAssessmentClass() {
        System.out.println(p1.getAssessmentClass());
    }

    @org.junit.jupiter.api.Test
    void setAssessmentClass() {
        p1.setAssessmentClass("Residential");
        assertEquals(p2.getAssessmentClass(), p1.getAssessmentClass());
    }

    @org.junit.jupiter.api.Test
    void getNeighbourhood() {
        System.out.println(p1.getNeighbourhood());
    }

    @org.junit.jupiter.api.Test
    void setNeighbourhood() {
        p1.setNeighbourhood("ASPEN GARDENS");
        assertEquals(p1.getNeighbourhood(), p2.getNeighbourhood());
    }

    @org.junit.jupiter.api.Test
    void getLatitude() {
        System.out.println(p1.getLatitude());
    }

    @org.junit.jupiter.api.Test
    void setLatitude() {
        p1.setLatitude(53.4771560788491);
        assertEquals(p2.getLatitude(), p1.getLatitude());
    }

    @org.junit.jupiter.api.Test
    void getLongitude() {
        System.out.println(p1.getLongitude());
    }

    @org.junit.jupiter.api.Test
    void setLongitude() {
        p1.setLongitude(-113.541589215624);
        assertEquals(p2.getLongitude(), p1.getLongitude());
    }
}