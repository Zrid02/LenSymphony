package fr.univartois.butinfo.lensymphony.notes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class FermataTest {

    @Test
    void testFermataDuration() {
        // Create a pitched note
        NotePitch pitchC4 = NotePitch.of(PitchClass.C, 4);
        NoteValue quarter = NoteValue.QUARTER;
        Note pitchedNote = new PitchedNote(pitchC4, quarter);

        // Wrap it in a fermata
        FermataNote fermata = new FermataNote(pitchedNote);

        int tempo = 120;

        // Calculate expected duration with fermata (2 times the original duration)
        int originalDuration = pitchedNote.getDuration(tempo);
        int expectedDuration = originalDuration * 2;

        assertEquals(expectedDuration, fermata.getDuration(tempo), "The duration with fermata is incorrect.");
    }

    @Test
    void testFermataDuration2() {
        // Create a pitched note
        NotePitch pitchA4 = NotePitch.of(PitchClass.A, 4);
        NoteValue half = NoteValue.HALF;
        Note pitchedNote = new PitchedNote(pitchA4, half);

        // Wrap it in a fermata
        FermataNote fermata = new FermataNote(pitchedNote);

        int tempo = 90;

        // Calculate expected duration with fermata (2 times the original duration)
        int originalDuration = pitchedNote.getDuration(tempo);
        int expectedDuration = originalDuration * 2;

        assertEquals(expectedDuration, fermata.getDuration(tempo), "The duration with fermata is incorrect.");
    }

    @Test
    void testFermataOnDottedNote() {
        // Create a pitched note
        NotePitch pitchE4 = NotePitch.of(PitchClass.E, 4);
        NoteValue eighth = NoteValue.EIGHTH;
        Note pitchedNote = new PitchedNote(pitchE4, eighth);

        // Create a dotted note
        Note dottedNote = new DottedNote(pitchedNote);

        // Wrap it in a fermata
        FermataNote fermata = new FermataNote(dottedNote);

        int tempo = 100;

        // Calculate expected duration with fermata (2 times the original dotted duration)
        int originalDuration = dottedNote.getDuration(tempo);
        int expectedDuration = originalDuration * 2;

        assertEquals(expectedDuration, fermata.getDuration(tempo), "The duration of dotted note with fermata is incorrect.");
    }

    @Test
    void testFermataNullNote_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            new FermataNote(null);
        });
    }

}
