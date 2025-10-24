package fr.univartois.butinfo.lensymphony.notes;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Score class.
 */
class ScoreTest {

    @Test
    void testConstructorAndGetters() {
        List<Note> noteList = new ArrayList<>();
        Score score = new Score(Instruments.PIANO, noteList);

        assertEquals(Instruments.PIANO, score.getInstrument(), "getInstrument() returned wrong value.");
        assertSame(noteList, score.getNotes(), "getNotes() should return the original list.");
    }

    @Test
    void testAddNote() {
        List<Note> noteList = new ArrayList<>();
        Score score = new Score(Instruments.FLUTE, noteList);

        Note note1 = new FakeNote(440.0, 500);
        score.addNote(note1);

        assertEquals(1, noteList.size(), "addNote() did not add note to the list.");
        assertSame(note1, noteList.get(0), "The correct note was not added.");
    }

    @Test
    void testIterator() {
        Note note1 = new FakeNote(440.0, 500);
        Note note2 = new FakeNote(261.63, 500);
        List<Note> noteList = new ArrayList<>(List.of(note1, note2));

        Score score = new Score(Instruments.GUITAR, noteList);

        int count = 0;
        for (Note n : score) {
            assertNotNull(n);
            count++;
        }
        assertEquals(2, count, "Iterator did not loop over the correct number of notes.");
    }
}