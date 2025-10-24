package fr.univartois.butinfo.lensymphony.notes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the DottedNote decorator.
 */
class DottedNoteTest {

    @Test
    void testDottedNoteDuration() {
        Note baseNote = new FakeNote(440.0, 1000);
        Note dottedNote = new DottedNote(baseNote);

        // Expected: 1000 * 1.5 = 1500
        assertEquals(1500, dottedNote.getDuration(120), "DottedNote duration should be 1.5 times base.");
    }

    @Test
    void testDecoratorFrequencyPassthrough() {
        // This test validates the NoteDecorator's getFrequency()
        Note baseNote = new FakeNote(261.63, 1000);
        Note dottedNote = new DottedNote(baseNote);

        assertEquals(261.63, dottedNote.getFrequency(), "Decorator should not change frequency.");
    }
}