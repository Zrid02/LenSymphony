package fr.univartois.butinfo.lensymphony.notes;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the NoteFactory class.
 * This test also covers the default method from AbstractNoteFactory.
 */
class NoteFactoryTest {

    @Test
    void testGetInstanceIsSingleton() {
        NoteFactory instance1 = NoteFactory.getInstance();
        NoteFactory instance2 = NoteFactory.getInstance();
        assertSame(instance1, instance2, "NoteFactory should be a singleton.");
    }

    @Test
    void testCreateRest() {
        NoteFactory factory = NoteFactory.getInstance();
        Note note = factory.createRest(NoteValue.QUARTER);
        assertTrue(note instanceof Rest, "createRest should return an instance of Rest.");
    }

    @Test
    void testCreateNote() {
        NoteFactory factory = NoteFactory.getInstance();
        NotePitch pitch = NotePitch.of(PitchClass.C, 4);
        Note note = factory.createNote(pitch, NoteValue.EIGHTH);
        assertTrue(note instanceof PitchedNote, "createNote should return an instance of PitchedNote.");
    }

    @Test
    void testCreateDottedNote() {
        NoteFactory factory = NoteFactory.getInstance();
        Note baseNote = new FakeNote(1,1);
        Note note = factory.createDottedNote(baseNote);
        assertTrue(note instanceof DottedNote, "createDottedNote should return an instance of DottedNote.");
    }

    @Test
    void testCreateFermataOn() {
        NoteFactory factory = NoteFactory.getInstance();
        Note baseNote = new FakeNote(1,1);
        Note note = factory.createFermataOn(baseNote);
        assertTrue(note instanceof FermataNote, "createFermataOn should return an instance of FermataNote.");
    }

    @Test
    void testCreateTiedNotesFromList() {
        NoteFactory factory = NoteFactory.getInstance();
        List<Note> notes = List.of(new FakeNote(1,1), new FakeNote(2,2));
        Note note = factory.createTiedNotes(notes);
        assertTrue(note instanceof TiedNotes, "createTiedNotes(List) should return an instance of TiedNotes.");
    }

    @Test
    void testCreateTiedNotesFromVarArgs() {
        // This test covers the 'default' method in AbstractNoteFactory
        NoteFactory factory = NoteFactory.getInstance();
        Note note1 = new FakeNote(1,1);
        Note note2 = new FakeNote(2,2);
        Note note = factory.createTiedNotes(note1, note2); // Calls varargs version
        assertTrue(note instanceof TiedNotes, "createTiedNotes(Note...) should return an instance of TiedNotes.");
    }
}