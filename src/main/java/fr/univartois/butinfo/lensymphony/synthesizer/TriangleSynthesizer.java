package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;

public class TriangleSynthesizer implements NoteSynthesizer{
	private int n;

	public TriangleSynthesizer( int n) {
		this.n=n;
	}

	@Override
	public double[] synthesize(Note note, int tempo, double volume) {
		double frequency = note.getFrequency();

		double duration = note.getDuration(tempo)/1000.0;

		int nbSample = (int) (duration*SAMPLE_RATE);

		double[] sounds = new double[nbSample];

		for(int i=0;i<nbSample;i++){
			double t = (double) i /SAMPLE_RATE;
			double value = 0.0;
			for (int j = 1; j < n; j++) {
				value += Math.exp(-5*(0.5+0.3-j))*Math.sin(4*Math.PI*(2000+800*j)*t);
			}
			sounds[i] = volume * Math.sin(2*Math.PI*frequency*t);

		}

		return sounds;	}
}
