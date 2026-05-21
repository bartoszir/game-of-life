package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    public void testSetIsAlive() {
        Cell cell = new Cell(5, 5);
        assertFalse(cell.isAlive());
        cell.setIsAlive(true);
        assertTrue(cell.isAlive());
    }

    @Test
    public void testChangeState() {
        Cell cell = new Cell(5, 5);
        assertFalse(cell.isAlive());
        cell.changeState(3);
        assertTrue(cell.isAlive());
    }


}