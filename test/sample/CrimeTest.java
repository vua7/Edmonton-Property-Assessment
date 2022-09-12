package sample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CrimeTest {
    private Crime c1;
    private Crime c2;

    @org.junit.jupiter.api.BeforeEach
    void setup() {
        c1 = new Crime("Mill Woods", "Assault", 2019, 2);
        c2 = new Crime("Windermere", "Break and Enter", 2020, 1);
    }

    @Test
    void getNeighbourhood() { System.out.println(c1.getNeighbourhood()); }

    @Test
    void setNeighbourhood() {
        c1.setNeighbourhood("Windermere");
        assertEquals(c2.getNeighbourhood(), c1.getNeighbourhood());
    }

    @Test
    void getIncident() { System.out.println(c1.getIncident()); }

    @Test
    void setIncident() {
        c1.setIncident("Break and Enter");
        assertEquals(c2.getIncident(), c1.getIncident());
    }

    @Test
    void getYear() { System.out.println(c1.getYear()); }

    @Test
    void setYear() {
        c1.setYear(2020);
        assertEquals(c2.getYear(), c1.getYear());
    }

    @Test
    void getOccurrences() { System.out.println(c1.getOccurrences()); }

    @Test
    void setOccurrences() {
        c1.setOccurrences(1);
        assertEquals(c2.getOccurrences(), c1.getOccurrences());
    }
}