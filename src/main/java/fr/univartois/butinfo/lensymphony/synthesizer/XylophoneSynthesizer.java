package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;

public class XylophoneSynthesizer implements NoteSynthesizer {
	private final int harmonics;
	private final double baseFrequency;

	public XylophoneSynthesizer(int harmonics, double baseFrequency) {
		if (harmonics < 1) {
			throw new IllegalArgumentException("harmonics must be >= 1");
		}
		this.harmonics = harmonics;
		this.baseFrequency = baseFrequency;
	}

	@Override
	public double[] synthesize(Note note, int tempo, double volume) {
		double frequency = note.getFrequency();
		if (frequency <= 0) {
			frequency = baseFrequency;
		}

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
