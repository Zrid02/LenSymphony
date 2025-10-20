package fr.univartois.butinfo.lensymphony.notes;

import java.util.List;

public final class NoteFactory implements AbstractNoteFactory {

    private static final NoteFactory INSTANCE = new NoteFactory();

    public NoteFactory() {
        // Private constructor to prevent instantiation
    }

    public static NoteFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Creates a rest with the given value.
     *
     * @param value The value of the rest.
     * @return The created rest.
     */

    @Override
    public Note createRest(NoteValue value) {
        return new Rest(value);
    }

    /**
     * Creates a note with the given pitch and value.
     *
     * @param pitch The pitch of the note.
     * @param value The value of the note.
     * @return The created note.
     */
    @Override
    public Note createNote(NotePitch pitch, NoteValue value) {
        return new PitchedNote(pitch, value);
    }

    /**
     * Creates a dotted note from the given existing note.
     *
     * @param note The existing note on which to add a dot.
     * @return The created dotted note.
     */

    @Override
    public Note createDottedNote(Note note) {
        throw new UnsupportedOperationException("createDottedNote not implemented yet");
        // When DottedNote will be created created : return new DottedNote(note);
    }

    /**
     * Creates a note representing a fermata on the given note.
     *
     * @param note The note on which to add a fermata.
     * @return The created note with a fermata.
     */

    @Override
    public Note createFermataOn(Note note) {
        throw new UnsupportedOperationException("createFermataOn not implemented yet");
        // When FermataNote will be created : return new FermataNote(note);
    }

    /**
     * Creates a note representing the tie of the given notes.
     *
     * @param notes The notes to tie together.
     *
     * @return The created tied note.
     */

    @Override
    public Note createTiedNotes(Note... notes) {
        return createTiedNotes(List.of(notes));
    }

    /**
     * Creates a note representing the tie of the given notes.
     *
     * @param notes The notes to tie together.
     * @return The created tied note.
     */

    @Override
    public Note createTiedNotes(List<Note> notes) {
        throw new UnsupportedOperationException("createTiedNotes(List<Note>) not implemented yet");
        // When FermataNote will be created : return new TiedNotes(note);

    }

}
