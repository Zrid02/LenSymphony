package fr.univartois.butinfo.lensymphony.notes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a silent musical event (a rest).
 *
 * <p>A Rest has a {@link NoteValue} that defines its base duration, an optional number of augmentation
 * dots which increase its duration, and an optional list of tied notes whose durations are summed
 * with this rest.</p>
 */
public class Rest implements Note {
	private final NoteValue noteValue;
	private int dots;
	private final List<Note> tiedNotes;

	/**
	 * Creates a Rest with the given {@code noteValue} and zero dots.
	 *
	 * @param noteValue the base value of this rest; must not be {@code null}
	 * @throws NullPointerException if {@code noteValue} is {@code null}
	 */
	public Rest(NoteValue noteValue) {
		this.noteValue = Objects.requireNonNull(noteValue, "noteValue can't be null");
		this.dots = 0;
		this.tiedNotes = new ArrayList<>();
	}

	/**
	 * Adds a single augmentation dot to this rest.
	 *
	 * <p>Each dot increases the duration by half of the previous addition (geometric series).</p>
	 */
	public void addDot() {
		this.dots++;
	}

	/**
	 * Adds {@code n} augmentation dots to this rest.
	 *
	 * @param n the number of dots to add; must be >= 0
	 * @throws IllegalArgumentException if {@code n} is negative
	 */
	public void addDots(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("n must be >= 0");
		}
		this.dots += n;
	}

	/**
	 * Ties this rest with another note. Only other rests (frequency == 0.0) are allowed.
	 *
	 * <p>The durations of tied notes are summed when computing the total duration.</p>
	 *
	 * @param other the note to tie with; must not be {@code null} and must be a rest
	 * @return this Rest instance (for fluent usage)
	 * @throws NullPointerException     if {@code other} is {@code null}
	 * @throws IllegalArgumentException if {@code other} is not a rest (frequency != 0.0)
	 */
	public Rest tieWith(Note other) {
		Objects.requireNonNull(other);
		if (other.getFrequency() != 0.0) {
			throw new IllegalArgumentException("Can only tie with silence (frequency = 0).");
		}
		tiedNotes.add(other);
		return this;
	}

	/**
	 * Returns an unmodifiable view of the tied notes.
	 *
	 * @return an unmodifiable list of tied notes
	 */
	public List<Note> getTiedNotes() {
		return Collections.unmodifiableList(tiedNotes);
	}

	/**
	 * Returns the frequency for a rest, which is always 0.0 Hz.
	 *
	 * @return {@code 0.0}
	 */
	@Override
	public double getFrequency() {
		return 0.0;
	}

	/**
	 * Computes the total duration of this rest in milliseconds for the given tempo.
	 *
	 * <p>The calculation uses the base duration from {@link NoteValue#duration(int)}, applies the
	 * augmentation dots (geometric sum), and adds the durations of any tied notes.</p>
	 *
	 * @param tempo the tempo in beats per minute; must be > 0
	 * @return the total duration in milliseconds as an {@code int}
	 * @throws IllegalArgumentException if {@code tempo} is less than or equal to 0
	 */
	@Override
	public int getDuration(int tempo) {
		if (tempo <= 0) {
			throw new IllegalArgumentException("Tempo must be > 0");
		}
		int base = noteValue.duration(tempo);
		// Multiplier for the dots
		double multiplier = 2.0 - Math.pow(0.5, dots);
		long total = Math.round(base * multiplier);

		for (Note n : tiedNotes) {
			total += n.getDuration(tempo);
		}

		return (int) total;
	}
}
