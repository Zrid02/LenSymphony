package fr.univartois.butinfo.lensymphony.notes;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Rest class.
 * This test covers all constructor, validation, and logic branches.
 */
class RestTest {

    private final int TEMPO = 120; // 120 BPM = 500ms per quarter note

    @Test
    void testConstructor() {
        Rest rest = new Rest(NoteValue.QUARTER);
        assertNotNull(rest, "Rest should be created.");

        // Test branch: Objects.requireNonNull(noteValue, ...)
        assertThrows(NullPointerException.class, () -> {
            new Rest(null);
        }, "Constructor should throw NullPointerException for null noteValue.");
    }

    @Test
    void testGetDurationValidation() {
        Rest rest = new Rest(NoteValue.QUARTER);

        // Test branch: if (tempo <= 0)
        assertThrows(IllegalArgumentException.class, () -> {
            rest.getDuration(0);
        }, "getDuration(0) should throw IllegalArgumentException.");

        assertThrows(IllegalArgumentException.class, () -> {
            rest.getDuration(-1);
        }, "getDuration(-1) should throw IllegalArgumentException.");
    }

    @Test
    void testDurationWithDots() {
        Rest rest = new Rest(NoteValue.QUARTER); // Base duration = 500ms at 120 BPM

        // Test base duration (dots = 0)
        assertEquals(500, rest.getDuration(TEMPO), "Base duration (0 dots) is incorrect.");

        // Test 1 dot
        rest.addDot(); // dots = 1
        assertEquals(750, rest.getDuration(TEMPO), "Duration with 1 dot is incorrect.");

        // Test 2 dots
        rest.addDot(); // dots = 2
        assertEquals(875, rest.getDuration(TEMPO), "Duration with 2 dots is incorrect.");
    }

    @Test
    void testAddDotsMethod() {
        Rest rest = new Rest(NoteValue.QUARTER);
        rest.addDots(3);
        assertEquals(938, rest.getDuration(TEMPO), "Duration with 3 dots (from addDots) is incorrect.");

        // TEST DE BRANCHE : if (n < 0)
        assertThrows(IllegalArgumentException.class, () -> {
            rest.addDots(-1);
        }, "addDots(-1) should throw IllegalArgumentException.");
    }

    @Test
    void testTieWith() {
        Rest rest = new Rest(NoteValue.QUARTER); // 500ms
        Note tiedRest = new FakeNote(0.0, 250); // Eighth note rest (250ms)

        rest.tieWith(tiedRest);

        // Test summation loop in getDuration()
        assertEquals(750, rest.getDuration(TEMPO), "Duration with one tied rest is incorrect.");
    }

    @Test
    void testTieWithValidation() {
        Rest rest = new Rest(NoteValue.QUARTER);

        // TEST DE BRANCHE : Objects.requireNonNull(other)
        assertThrows(NullPointerException.class, () -> {
            rest.tieWith(null);
        }, "tieWith(null) should throw NullPointerException.");

        // TEST DE BRANCHE : if (other.getFrequency() != 0.0)
        Note pitchedNote = new FakeNote(440.0, 250); // A pitched note
        assertThrows(IllegalArgumentException.class, () -> {
            rest.tieWith(pitchedNote);
        }, "tieWith(pitchedNote) should throw IllegalArgumentException.");
    }

    @Test
    void testGetTiedNotesUnmodifiable() {
        Rest rest = new Rest(NoteValue.QUARTER);
        rest.tieWith(new FakeNote(0.0, 100));

        List<Note> tiedNotes = rest.getTiedNotes();
        assertEquals(1, tiedNotes.size(), "getTiedNotes() should return the list.");

        // Test that the returned list is unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> {
            tiedNotes.add(new FakeNote(0.0, 50));
        }, "List from getTiedNotes() should be unmodifiable.");
    }
}