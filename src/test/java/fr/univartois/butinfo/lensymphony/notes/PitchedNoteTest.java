package fr.univartois.butinfo.lensymphony.notes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PitchedNoteTest {

    @Test
    void a4Quarter_at120bpm() {
        Note n = new PitchedNote(NotePitch.of(PitchClass.A, 4), NoteValue.QUARTER);
        assertEquals(440.0, n.getFrequency(), 1e-9);
        assertEquals(500, n.getDuration(120)); // whole@120=2000ms -> quarter=500ms
    }

    @Test
    void c4Half_at90bpm() {
        Note n = new PitchedNote(NotePitch.of(PitchClass.C, 4), NoteValue.HALF);
        assertEquals(261.625565, n.getFrequency(), 1e-3);
        assertEquals(1333, n.getDuration(90)); // 2666.66 * 0.5 -> 1333 (cast int)
    }

    @Test
    void e5Eighth_at120bpm() {
        Note n = new PitchedNote(NotePitch.of(PitchClass.E, 5), NoteValue.EIGHTH);
        assertEquals(659.255, n.getFrequency(), 1e-2);
        assertEquals(250, n.getDuration(120)); // 2000 * 0.125
    }
}