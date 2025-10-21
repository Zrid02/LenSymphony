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

    @Test
    void testTiedNotesEmptyList() {
        TiedNotes tied = new TiedNotes(List.of());

        int tempo = 120;

        assertEquals(0, tied.getDuration(tempo), "The duration of tied notes with an empty list should be 0.");
        assertEquals(0.0, tied.getFrequency(), "The frequency of tied notes with an empty list should be 0.");
    }

    @Test
    void testTiedNotesSingleNote() {
        NotePitch pitchD4 = NotePitch.of(PitchClass.D, 4);
        NoteValue whole = NoteValue.WHOLE;

        Note note = new PitchedNote(pitchD4, whole);

        TiedNotes tied = new TiedNotes(List.of(note));

        int tempo = 100;

        assertEquals(note.getDuration(tempo), tied.getDuration(tempo), "The duration of tied notes with a single note is incorrect.");
        assertEquals(note.getFrequency(), tied.getFrequency(), "The frequency of tied notes with a single note is incorrect.");
    }

    @Test
    void testTiedNotesDurationAndFrequency2() {
        // Create two notes to be tied
        NotePitch pitchE4 = NotePitch.of(PitchClass.E, 4); //
        NoteValue eighth = NoteValue.EIGHTH;
        NoteValue quarter = NoteValue.QUARTER;

        Note note1 = new PitchedNote(pitchE4, eighth);
        // Use the same pitch for the second note because tied notes must have the same frequency
        Note note2 = new PitchedNote(pitchE4, quarter);

        // Create the tied notes
        TiedNotes tied = new TiedNotes(List.of(note1, note2));

        int tempo = 140;

        // Calculate  duration
        int expectedDuration = note1.getDuration(tempo) + note2.getDuration(tempo);

        // Frequency should be the same as the first note
        double expectedFrequency = note1.getFrequency();

        assertEquals(expectedDuration, tied.getDuration(tempo), "The duration of tied notes is incorrect.");
        assertEquals(expectedFrequency, tied.getFrequency(), "The frequency of tied notes is incorrect.");
    }

}