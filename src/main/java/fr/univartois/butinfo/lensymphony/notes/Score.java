package fr.univartois.butinfo.lensymphony.notes;

import java.util.Iterator;
import java.util.List;

/**
 * The Score class represents a musical staff that contains a sequence of musical notes.
 * It implements the Iterable interface to allow iteration over the contained notes.
 * A stave is associated with a specific musical instrument and maintains an ordered list
 * of notes that can be played by that instrument.
 *
 * @author Dassonville Ugo
 */
public class Score implements Iterable<Note> {

	/**
	 * The list of musical notes contained in this stave.
	 */
	private List<Note> notes;

	/**
	 * The musical instrument associated with this stave.
	 */
	private Instruments instrument;


	/**
	 * Creates a new stave for the given musical instrument.
	 *
	 * @param instrument The instrument that will play the notes in this stave.
	 */
	public Score(Instruments instrument, List<Note> notes) {
		this.instrument = instrument;
		this.notes = notes;
	}

	/**
	 * Gets the musical instrument associated with this stave.
	 *
	 * @return The instrument that plays the notes in this stave.
	 */
	public Instruments getInstrument() {
		return instrument;
	}

	/**
	 * Adds a new note to this stave.
	 *
	 * @param note The musical note to add to the stave.
	 */
	public void addNote(Note note) {
		notes.add(note);
	}

	/**
	 * Returns an iterator over the notes in this stave.
	 *
	 * @return An iterator over the notes in this stave.
	 */
	@Override
	public Iterator<Note> iterator() {
		return notes.iterator();
	}
}