package fr.univartois.butinfo.lensymphony.notes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TiedsTest {

    @Test
    void testTiedNotesDurationAndFrequency() {
        // Create two notes to be tied
        NotePitch pitchC4 = NotePitch.of(PitchClass.C, 4); //
        NoteValue quarter = NoteValue.QUARTER;
        NoteValue half = NoteValue.HALF;

        Note note1 = new PitchedNote(pitchC4, quarter);
        // Use the same pitch for the second note because tied notes must have the same frequency
        Note note2 = new PitchedNote(pitchC4, half);

        // Create the tied notes
        TiedNotes tied = new TiedNotes(List.of(note1, note2));

        int tempo = 120;

        // Calculate  duration
        int expectedDuration = note1.getDuration(tempo) + note2.getDuration(tempo);

        // Frequency should be the same as the first note
        double expectedFrequency = note1.getFrequency();

        assertEquals(expectedDuration, tied.getDuration(tempo), "The duration of tied notes is incorrect.");
        assertEquals(expectedFrequency, tied.getFrequency(), "The frequency of tied notes is incorrect.");
    }
}