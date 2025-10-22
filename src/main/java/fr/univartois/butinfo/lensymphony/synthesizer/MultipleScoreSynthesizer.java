package fr.univartois.butinfo.lensymphony.synthesizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a composite pattern to combine multiple music synthesizers together.
 * This synthesizer allows adding multiple synthesizers and combines their audio samples
 * by averaging them together.
 *
 * @author Dassonville Ugo
 * @version 0.1.0
 */
public class MultipleScoreSynthesizer implements MusicSynthesizer {

	/**
	 * The list of music synthesizers to combine.
	 */
	private final List<MusicSynthesizer> synthetizers = new ArrayList<>();

	/**
	 * The combined audio samples from all synthesizers.
	 */
	private double[] samples = new double[0];

	/**
	 * Adds a music synthesizer to this composite.
	 *
	 * @param synth The synthesizer to add (must not be null)
	 */
	public void add(MusicSynthesizer synth) {
		if (synth != null) {
			synthetizers.add(synth);
		}
	}

	/**
	 * Generates the audio samples by combining all the synthesizers.
	 * Each synthesizer is synthesized and their maximum length is used to create
	 * the combined samples array.
	 */
	@Override
	public void synthesize() {
		for (MusicSynthesizer synth : synthetizers) {
			synth.synthesize();
		}

		int maxLen = 0;
		for (MusicSynthesizer synth : synthetizers) {
			int len = synth.getSamples().length;
			if (len > maxLen) {
				maxLen = len;
			}
		}
		samples = new double[maxLen];
	}

	/**
	 * Gets the combined audio samples from all synthesizers.
	 * The samples are averaged by dividing each sample by the number of synthesizers.
	 *
	 * @return The averaged audio samples from all synthesizers
	 */
	@Override
	public double[] getSamples() {
		int count = synthetizers.size();

		for (MusicSynthesizer synth : synthetizers) {
			double[] cSamples = synth.getSamples();
			for (int i = 0; i < cSamples.length; i++) {
				samples[i] += cSamples[i] / count;
			}
		}
		return samples;
	}
}