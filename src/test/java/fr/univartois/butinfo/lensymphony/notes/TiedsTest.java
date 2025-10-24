package fr.univartois.butinfo.lensymphony.notes;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the TiedNotes class.
 */
class TiedsTest {

    @Test
    void testEmptyList() {
        TiedNotes notes = new TiedNotes(List.of());

        // Test branch: if (notes.isEmpty())
        assertEquals(0.0, notes.getFrequency(), "Frequency of empty tied notes should be 0.0.");
        assertEquals(0, notes.getDuration(120), "Duration of empty tied notes should be 0.");
    }

    @Test
    void testGetFrequency() {
        Note note1 = new FakeNote(440.0, 500); // A4
        Note note2 = new FakeNote(261.63, 500); // C4

        TiedNotes notes = new TiedNotes(List.of(note1, note2));

        // Test branch: return notes.getFirst().getFrequency()
        assertEquals(440.0, notes.getFrequency(), "Frequency should be that of the first note.");
    }

    @Test
    void testGetDuration() {
        Note note1 = new FakeNote(440.0, 500); // Quarter
        Note note2 = new FakeNote(440.0, 250); // Eighth

        TiedNotes notes = new TiedNotes(List.of(note1, note2));

        // Test the summation loop
        assertEquals(750, notes.getDuration(120), "Duration should be the sum of all tied notes.");
    }
}