package fr.univartois.butinfo.lensymphony.notes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the NoteValue enum.
 * This test validates the duration calculation and the fromString mapping.
 */
class NoteValueTest {

    @Test
    void testDurationCalculation() {
        // 120 BPM = 500ms per beat (quarter note)
        // A whole note (4 beats) should be 2000ms.
        int tempo = 120;

        assertEquals(2000, NoteValue.WHOLE.duration(tempo), "Whole note duration incorrect.");
        assertEquals(500, NoteValue.QUARTER.duration(tempo), "Quarter note duration incorrect.");
        assertEquals(250, NoteValue.EIGHTH.duration(tempo), "Eighth note duration incorrect.");
    }

    @Test
    void testFromStringValidTypes() {
        assertEquals(NoteValue.WHOLE, NoteValue.fromString("whole"));
        assertEquals(NoteValue.HALF, NoteValue.fromString("half"));
        assertEquals(NoteValue.QUARTER, NoteValue.fromString("quarter"));
        assertEquals(NoteValue.SIXTEENTH, NoteValue.fromString("16th"));
    }

    @Test
    void testFromStringIsCaseInsensitive() {
        // This test validates the equalsIgnoreCase branch
        assertEquals(NoteValue.QUARTER, NoteValue.fromString("Quarter"));
        assertEquals(NoteValue.HALF, NoteValue.fromString("HALF"));
    }

    @Test
    void testFromStringInvalidType() {
        // This test validates the 'throw' branch
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            NoteValue.fromString("non_existent_type");
        });

        String expectedMessage = "No NoteValue with type non_existent_type";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Exception message is incorrect.");
    }
}