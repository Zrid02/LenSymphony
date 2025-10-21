package fr.univartois.butinfo.lensymphony.notes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PitchedNoteTest {

    @Test
    void createPitchedNote() {
        Note note = new PitchedNote(NotePitch.of(PitchClass.A, 4), NoteValue.QUARTER);

        assertNotNull(note);
    }

    @Test
    void getFrequency() {
        Note note = new PitchedNote(NotePitch.of(PitchClass.A, 4), NoteValue.QUARTER);

        assertEquals(440.0, note.getFrequency(), 0.01);
    }

    @Test
    void getDuration() {
        Note note = new PitchedNote(NotePitch.of(PitchClass.A, 4), NoteValue.QUARTER);

        assertEquals(500, note.getDuration(120));
    }

    @Test
    void nullPitch_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            new PitchedNote(null, NoteValue.QUARTER);
        });
    }

    @Test
    void nullValue_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            new PitchedNote(NotePitch.of(PitchClass.A, 4), null);
        });
    }
}