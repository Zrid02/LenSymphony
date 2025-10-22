package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;

/**
 * A decorator for {@link NoteSynthesizer} that adds vibrato effect to the sound.
 * <p>
 * Vibrato is a musical effect consisting of regular, pulsating change of pitch.
 * It is used to add warmth and expressiveness to the sound.
 * <p>
 * The vibrato effect is created by adding a sinusoidal modulation to the original sound:
 * <pre>
 * s'(t) = s(t) + d * sin(2π * s * t)
 * </pre>
 * where:
 * <ul>
 *   <li>s(t) is the original sound</li>
 *   <li>d is the depth (amplitude) of the vibrato</li>
 *   <li>s is the speed (frequency) of the vibrato oscillation</li>
 *   <li>t is the time</li>
 * </ul>
 *
 * @author Dassonville Ugo
 * @version 0.1.0
 */
public class VibratoSynthesizer extends NoteSynthesizerDecorator {
	private double d; //depth
	private double s; //speed

	/**
	 * Creates a new vibrato synthesizer decorator.
	 *
	 * @param synthesizer The synthesizer to decorate (must not be {@code null}).
	 * @param depth       The depth (amplitude) of the vibrato effect (must be positive).
	 * @param speed       The speed (frequency) of the vibrato oscillation (must be positive).
	 * @throws NullPointerException     If {@code synthesizer} is {@code null}.
	 * @throws IllegalArgumentException If {@code depth} or {@code speed} is negative.
	 */
	public VibratoSynthesizer(NoteSynthesizer synthesizer, double depth, double speed) {
		super(synthesizer);
		if (depth < 0 || speed < 0) {
			throw new IllegalArgumentException("depth and speed must be positive");
		}
		this.d = depth;
		this.s = speed;
	}

	/**
	 * Synthesizes the given note with vibrato effect applied.
	 * <p>
	 * First gets the base sound from the decorated synthesizer, then applies
	 * the vibrato modulation to create a pulsating pitch effect.
	 *
	 * @param note   The note to synthesize.
	 * @param tempo  The tempo in beats per minute (BPM).
	 * @param volume The volume level for the note (0.0 to 1.0).
	 * @return An array of audio samples with vibrato effect applied.
	 */
	@Override
	public double[] synthesize(Note note, int tempo, double volume) {
		double[] sounds = super.synthesize(note, tempo, volume);
		for (int i = 0; i < sounds.length; i++) {
			double t = (double) i / SAMPLE_RATE;
			sounds[i] += d * Math.sin(2 * Math.PI * s * t);
		}
		return sounds;
	}

}