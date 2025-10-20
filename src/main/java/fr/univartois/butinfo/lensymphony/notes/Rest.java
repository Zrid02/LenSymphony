package fr.univartois.butinfo.lensymphony.notes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Rest implements Note {
	private final NoteValue noteValue;
	private int dots;
	private final List<Note> tiedNotes;


	public Rest(NoteValue noteValue) {
		this.noteValue = Objects.requireNonNull(noteValue, "noteValue can't be null");
		this.dots = 0;
		this.tiedNotes = new ArrayList<>();
	}

	/**
	 * Ajoute un point (dot) à ce silence.
	 */
	public void addDot() {
		this.dots++;
	}

	/**
	 * Ajoute n points (dots).
	 */
	public void addDots(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("n must be >= 0");
		}
		this.dots += n;
	}

	/**
	 * Lie (tie) ce silence avec une autre note (la durée sera sommée).
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
	 * Retourne les notes liées (lecture seule).
	 */
	public List<Note> getTiedNotes() {
		return Collections.unmodifiableList(tiedNotes);
	}

	@Override
	public double getFrequency() {
		return 0.0;
	}

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
