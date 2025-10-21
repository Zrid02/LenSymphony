package fr.univartois.butinfo.lensymphony.synthesizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite that adds multiple musicSynthesizers
 */
public class MultipleScoreSynthesizer implements MusicSynthesizer {

	private final List<MusicSynthesizer> synthetizers = new ArrayList<>();
	private double[] samples = new double[0];

	public void add(MusicSynthesizer synth) {
		if (synth != null) {
			synthetizers.add(synth);
		}
	}

	@Override
	public void synthesize() {
		// synthétiser chaque synthetizer
		for (MusicSynthesizer synth : synthetizers) {
			synth.synthesize();
		}

		// déterminer la longueur maximale
		int maxLen = 0;
		for (MusicSynthesizer synth : synthetizers) {
			int len = synth.getSamples().length;
			if (len > maxLen) {
				maxLen = len;
			}
		}
		samples = new double[maxLen];
	}

	@Override
	public double[] getSamples() {
		// additionner les échantillons
		int count = synthetizers.size();

		for (MusicSynthesizer synth : synthetizers) {
			double[] cSamples = synth.getSamples();
			for (int i = 0; i < cSamples.length; i++) {
				samples[i] += cSamples[i]/count;
			}
		}
		return samples;
	}
}
