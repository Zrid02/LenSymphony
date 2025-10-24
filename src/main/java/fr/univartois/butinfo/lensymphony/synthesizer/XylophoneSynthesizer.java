package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;

/**
 * XylophoneSynthesizer generates a simple percussive sound resembling a xylophone.
 *
 * <p>The synthesizer produces a decaying exponential amplitude envelope and a
 * harmonic series where each harmonic is a power-of-two multiple of the base
 * frequency. Harmonic amplitudes are reduced exponentially to simulate the
 * metallic/percussive timbre.</p>
 *
 * <p>This class implements {@code NoteSynthesizer} and relies on the interface
 * constant {@code SAMPLE_RATE} to convert time to samples.</p>
 */
public class XylophoneSynthesizer implements NoteSynthesizer {
	/**
	 * Number of harmonics to synthesize. Must be >= 1.
	 */
	private final int harmonics = 8;
	private static final XylophoneSynthesizer INSTANCE = new XylophoneSynthesizer();


	/**
	 * Frequency to use when a note's frequency is invalid (<= 0).
	 */
	private final double baseFrequency = 3;

	private XylophoneSynthesizer() {}

	public static XylophoneSynthesizer getInstance() {
		return INSTANCE;
	}

	/**
	 * Synthesize audio samples for the given note.
	 *
	 * <p>The produced buffer contains {@code durationSeconds * SAMPLE_RATE} samples.
	 * The amplitude is scaled by {@code volume}. The per-sample value is computed
	 * as a sum of sinusoids at harmonic frequencies multiplied by an overall
	 * exponential decay envelope.</p>
	 *
	 * @param note the musical note to synthesize
	 * @param tempo tempo in beats per minute used to interpret the note duration
	 * @param volume overall gain multiplier for the generated samples
	 * @return an array of double samples (PCM-like) representing the synthesized sound;
	 *         returns an empty array if the computed number of samples is zero or negative
	 */
	@Override
	public double[] synthesize(Note note, int tempo, double volume) {
		double frequency = note.getFrequency();

		double durationSeconds = note.getDuration(tempo) / 1000.0;
		int nbSample = (int) (durationSeconds * SAMPLE_RATE);
		if (nbSample <= 0) {
			return new double[0];
		}

		double[] sounds = new double[nbSample];

		for (int s = 0; s < nbSample; s++) {
			double t = (double) s / SAMPLE_RATE;
			double env = Math.exp(-3.0 * t);
			double sum = 0.0;
			for (int i = 0; i < harmonics; i++) {
				sum += Math.sin(2.0 * Math.PI * Math.pow(2.0, i) * frequency * t) * Math.exp(-(2 * i + 1));
			}
			sounds[s] = volume * env * sum;
		}

		return sounds;
	}
}