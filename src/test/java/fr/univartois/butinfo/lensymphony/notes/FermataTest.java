package fr.univartois.butinfo.lensymphony.notes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the FermataNote decorator.
 */
class FermataTest {

    @Test
    void testFermataNoteDuration() {
        Note baseNote = new FakeNote(440.0, 1000);
        Note fermataNote = new FermataNote(baseNote);

        // Expected: 1000 * 2 = 2000
        assertEquals(2000, fermataNote.getDuration(120), "FermataNote duration should be 2 times base.");
    }

    @Test
    void testDecoratorFrequencyPassthrough() {
        // This test validates the NoteDecorator's getFrequency()
        Note baseNote = new FakeNote(329.63, 1000);
        Note fermataNote = new FermataNote(baseNote);

        assertEquals(329.63, fermataNote.getFrequency(), "Decorator should not change frequency.");
    }
}