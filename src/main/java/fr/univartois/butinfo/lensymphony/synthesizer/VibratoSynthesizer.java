package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;

public class VibratoSynthesizer extends NoteSynthesizerDecorator {
	private double d; //depth
	private double s; //speed

	/**
	 * Creates a new synthesizer decorator that wraps the given synthesizer.
	 *
	 * @param synthesizer The synthesizer to decorate (must not be {@code null}).
	 * @throws NullPointerException If {@code synthesizer} is {@code null}.
	 */
	public VibratoSynthesizer(NoteSynthesizer synthesizer, double depth, double speed) {
		super(synthesizer);
		if (depth < 0 || speed < 0) {
			throw new IllegalArgumentException("depth and speed must be positive");
		}
		this.d = depth;
		this.s = speed;
	}


	public double[] synthesize(Note note, int tempo, double volume) {
		double[] sounds = super.synthesize(note, tempo, volume);
		for (int i = 0; i < sounds.length; i++) {
			double t = (double) i / SAMPLE_RATE;
			sounds[i] += d * Math.sin(2 * Math.PI * s * t);
		}
		return sounds;
	}

}
