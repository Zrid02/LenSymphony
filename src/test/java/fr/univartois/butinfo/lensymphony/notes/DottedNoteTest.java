package fr.univartois.butinfo.lensymphony.notes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DottedNoteTest {

    @Test
    void createDottedNote() {
        Note base = new PitchedNote(NotePitch.of(PitchClass.A, 4), NoteValue.QUARTER);
        Note dotted = new DottedNote(base);

        assertNotNull(dotted);
    }

    @Test
    void getFrequency_unchanged() {
        Note base = new PitchedNote(NotePitch.of(PitchClass.A, 4), NoteValue.QUARTER);
        Note dotted = new DottedNote(base);

        assertEquals(440.0, dotted.getFrequency(), 0.01);
    }

    @Test
    void getDuration_multipliedBy1point5() {
        Note base = new PitchedNote(NotePitch.of(PitchClass.A, 4), NoteValue.QUARTER);
        Note dotted = new DottedNote(base);

        // quarter@120 = 500 ms -> dotted = 500 * 1.5 = 750 ms
        assertEquals(750, dotted.getDuration(120));
    }

    @Test
    void nullNote_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            new DottedNote(null);
        });
    }

    @Test
    void dottedHalfNote() {
        Note base = new PitchedNote(NotePitch.of(PitchClass.C, 4), NoteValue.HALF);
        Note dotted = new DottedNote(base);

        // half@120 = 1000 ms -> dotted = 1000 * 1.5 = 1500 ms
        assertEquals(1500, dotted.getDuration(120));
    }
}