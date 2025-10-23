package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;

/**
 * TriangleSynthesizer generates a triangle-like waveform approximation by
 * summing harmonics. The number of harmonics included in the approximation
 * is controlled by the constructor parameter {@code n}.
 *
 * Note: This class relies on {@code SAMPLE_RATE} provided by the
 * {@code NoteSynthesizer} interface.
 */
public class TriangleSynthesizer implements NoteSynthesizer{
	/**
	 * Number of harmonics to include when synthesizing the waveform.
	 * Higher values produce a closer approximation of a triangle wave but
	 * require more CPU.
	 */
	private int n = 8;
	private static final TriangleSynthesizer INSTANCE = new TriangleSynthesizer();


	private TriangleSynthesizer() {}

	public static TriangleSynthesizer getInstance(){
		return INSTANCE;
	}

	/**
	 * Synthesize the audio samples for a given note.
	 *
	 * This method:
	 * - retrieves the note frequency,
	 * - computes the duration in seconds from the note and tempo,
	 * - allocates a buffer sized by duration * SAMPLE_RATE,
	 * - fills the buffer with computed samples.
	 *
	 * Current implementation computes a harmonic summation into the local
	 * variable {@code value} but returns a plain sine at the note frequency
	 * multiplied by {@code volume}. Adjust the output assignment if the
	 * harmonic summation should affect the final waveform.
	 *
	 * @param note the Note to synthesize
	 * @param tempo tempo in beats per minute used to compute note duration
	 * @param volume scalar volume multiplier (0.0 to 1.0 typical)
	 * @return an array of doubles representing the synthesized audio samples
	 */
	@Override
	public double[] synthesize(Note note, int tempo, double volume) {
		double frequency = note.getFrequency();

		double duration = note.getDuration(tempo)/1000.0;

		int nbSample = (int) (duration*SAMPLE_RATE);

		double[] sounds = new double[nbSample];
		if (frequency == 0){
			return sounds;
		}

		for(int i=0;i<nbSample;i++){
			double t = (double) i /SAMPLE_RATE;
			double value = 0.0;
			for (int j = 1; j <= n; j++) {
				double exp = Math.exp(-5*(0.5+0.3*j));
				double f = 2000+800*j;
				value += exp*Math.sin(4*Math.PI*f*t);
			}
			sounds[i] = volume*value;
		}

		return sounds;	}
}
