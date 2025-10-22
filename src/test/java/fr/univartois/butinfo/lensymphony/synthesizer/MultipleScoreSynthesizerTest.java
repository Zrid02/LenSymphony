package fr.univartois.butinfo.lensymphony.synthesizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MultipleScoreSynthesizerTest {

	private static final double EPS = 1e-9;

	private static class FakeSynth implements MusicSynthesizer {
		private final double[] samples;
		boolean synthesized = false;

		FakeSynth(double[] samples) {
			this.samples = samples.clone();
		}

		@Override
		public void synthesize() {
			synthesized = true;
		}

		@Override
		public double[] getSamples() {
			return samples.clone();
		}

		@Override
		public byte[] getAudioData() {
			return new byte[0];
		}

		@Override
		public void play() { /* no-op */ }

		@Override
		public void save(String filename) { /* no-op */ }
	}

	@Test
	void addNullIsIgnoredAndSingleSynthProducesSameSamples() {
		MultipleScoreSynthesizer composite = new MultipleScoreSynthesizer();
		FakeSynth s = new FakeSynth(new double[]{0.2, 0.4});

		composite.add(null);
		composite.add(s);

		composite.synthesize();

		assertTrue(s.synthesized, "Child synthesize() must be called");
		double[] out = composite.getSamples();
		assertArrayEquals(new double[]{0.2, 0.4}, out, EPS);
	}

	@Test
	void averageSamplesWithDifferentLengths() {
		MultipleScoreSynthesizer composite = new MultipleScoreSynthesizer();
		FakeSynth s1 = new FakeSynth(new double[]{1.0, 1.0});
		FakeSynth s2 = new FakeSynth(new double[]{3.0});

		composite.add(s1);
		composite.add(s2);

		composite.synthesize();

		double[] out = composite.getSamples();
		// expected: index0 -> (1 + 3) / 2 = 2.0
		// index1 -> (1 + 0) / 2 = 0.5  (second synth has no sample at index 1)
		assertArrayEquals(new double[]{2.0, 0.5}, out, EPS);
	}

	@Test
	void synthesizeInvokesChildren() {
		MultipleScoreSynthesizer composite = new MultipleScoreSynthesizer();
		FakeSynth s1 = new FakeSynth(new double[]{0.0});
		FakeSynth s2 = new FakeSynth(new double[]{0.0});

		composite.add(s1);
		composite.add(s2);

		composite.synthesize();

		assertTrue(s1.synthesized, "First child must have been synthesized");
		assertTrue(s2.synthesized, "Second child must have been synthesized");
	}
}
