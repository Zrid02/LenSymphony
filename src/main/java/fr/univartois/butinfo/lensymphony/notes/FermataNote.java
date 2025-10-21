package fr.univartois.butinfo.lensymphony.notes;

public class FermataNote extends NoteDecorator {

    /**
     * Creates a new fermata note that decorates the given note.
     *
     * @param note The note to decorate (must not be {@code null}).
     */
    public FermataNote(Note note) {
        super(note);
    }

    /**
     * Returns the duration of the fermeta note, in milliseconds, for the given tempo.
     * <p>
     * A fermata note's normal duration is not fixed, but for simplicity, we will
     *
     * @param tempo The tempo in beats per minute (BPM).
     *
     * @return The duration in milliseconds.
     */
    @Override
    public int getDuration(int tempo) {
        return super.getDuration(tempo) * 4; // Fermata note during is 4 times the original note duration
    }
}
