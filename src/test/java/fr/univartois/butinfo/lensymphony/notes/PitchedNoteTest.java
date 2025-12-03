package fr.univartois.butinfo.lensymphony.notes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PitchedNote class.
 * This test covers constructor validation and all branches of equals().
 */
class PitchedNoteTest {

    private static NotePitch pitchC4;
    private static NotePitch pitchA4;
    private static NoteValue valueQuarter;
    private static NoteValue valueHalf;

    @BeforeAll
    static void setUp() {
        // We must call .of() to populate the cache in NotePitch
        pitchC4 = NotePitch.of(PitchClass.C, 4);
        pitchA4 = NotePitch.of(PitchClass.A, 4);
        valueQuarter = NoteValue.QUARTER;
        valueHalf = NoteValue.HALF;
    }

    @Test
    void testConstructorNullChecks() {
        // Test branch: if (pitch == null)
        assertThrows(NullPointerException.class, () -> {
            new PitchedNote(null, valueQuarter);
        }, "Constructor should throw NullPointerException for null pitch.");

        // Test branch: if (value == null)
        assertThrows(NullPointerException.class, () -> {
            new PitchedNote(pitchC4, null);
        }, "Constructor should throw NullPointerException for null value.");
    }

    @Test
    void testGetters() {
        PitchedNote note = new PitchedNote(pitchA4, valueQuarter);
        assertEquals(440.0, note.getFrequency(), "getFrequency() error.");
        assertEquals(500, note.getDuration(120), "getDuration() error.");
        assertSame(pitchA4, note.pitch(), "pitch() getter error.");
        assertSame(valueQuarter, note.value(), "value() getter error.");
    }

    @Test
    void testEqualsAndHashCode() {
        PitchedNote note1 = new PitchedNote(pitchC4, valueQuarter);
        PitchedNote note2 = new PitchedNote(pitchC4, valueQuarter); // Identical
        PitchedNote note3_diffPitch = new PitchedNote(pitchA4, valueQuarter);
        PitchedNote note4_diffValue = new PitchedNote(pitchC4, valueHalf);

        // Test branch: if (this == o)
        assertEquals(note1, note1, "A note must equal itself.");

        // Test logical equality
        assertEquals(note1, note2, "Two notes with same pitch and value should be equal.");
        assertEquals(note1.hashCode(), note2.hashCode(), "Hashcodes for equal objects must be equal.");

        // Test non-equality
        assertNotEquals(note1, note3_diffPitch, "Notes with different pitch should not be equal.");
        assertNotEquals(note1, note4_diffValue, "Notes with different value should not be equal.");

        // Test branch: if (!(o instanceof PitchedNote))
        assertNotEquals(note1, "A String", "Note should not be equal to a different type.");
    }
}