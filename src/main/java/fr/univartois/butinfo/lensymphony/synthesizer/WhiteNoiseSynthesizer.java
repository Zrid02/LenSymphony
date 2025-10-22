/**
 * LenSymphony - A simple music synthesizer library developed in Lens, France.
 * Copyright (c) 2025 Romain Wallon - Université d'Artois.
 */

package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;

import java.util.Random;

/**
 * A decorator for {@link NoteSynthesizer} that adds white noise to simulate
 * wind instrument breath sounds.
 * <p>
 * Formula: s'(t) = s(t) + random(-b, b)
 * <p>
 * where b is the noise amplitude.
 *
 * @author Dassonville Ugo
 * @version 0.1.0
 */
public class WhiteNoiseSynthesizer extends NoteSynthesizerDecorator {

	/**
	 * The amplitude of the white noise to add.
	 */
	private final double noiseAmplitude;

	/**
	 * Random number generator for generating noise.
	 */
	private final Random random;

	/**
	 * Creates a new white noise synthesizer that decorates the given synthesizer.
	 *
	 * @param synthesizer    The synthesizer to decorate
	 * @param noiseAmplitude The amplitude of the white noise to add (must be >= 0)
	 * @throws IllegalArgumentException if noiseAmplitude is negative
	 */
	public WhiteNoiseSynthesizer(NoteSynthesizer synthesizer, double noiseAmplitude) {
		super(synthesizer);
		if (noiseAmplitude < 0) {
			throw new IllegalArgumentException("noiseAmplitude must be >= 0");
		}
		this.noiseAmplitude = noiseAmplitude;
		this.random = new Random();
	}

	/**
	 * Synthesizes the given note and adds white noise to the samples.
	 *
	 * @param note   The note to synthesize
	 * @param tempo  The tempo in beats per minute (BPM)
	 * @param volume The volume level for the note (0.0 to 1.0)
	 * @return The synthesized samples with added white noise
	 */
	@Override
	public double[] synthesize(Note note, int tempo, double volume) {
		double[] samples = super.synthesize(note, tempo, volume);

		for (int i = 0; i < samples.length; i++) {
			double noise = (random.nextDouble() * 2 - 1) * noiseAmplitude;
			samples[i] = samples[i] + noise;
		}

		return samples;
	}
}