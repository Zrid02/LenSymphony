package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VibratoSynthesizerTest {

	@Test
	void createVibratoSynthesizer() {
		NoteSynthesizer base = new PureSound();
		NoteSynthesizer vib = new VibratoSynthesizer(base, 0.02, 5.0);

		assertNotNull(vib);
	}

	@Test
	void synthesizeWithVibrato() {
		NoteSynthesizer vib = new VibratoSynthesizer(new PureSound(), 0.02, 5.0);
		Note note = new PitchedNote(NotePitch.of(PitchClass.A, 4), NoteValue.QUARTER);

		double[] samples = vib.synthesize(note, 120, 0.5);

		assertEquals(22050, samples.length);
	}

	@Test
	void differentFromPureSound() {
		NoteSynthesizer pure = new PureSound();
		NoteSynthesizer vib = new VibratoSynthesizer(pure, 0.02, 5.0);
		Note note = new PitchedNote(NotePitch.of(PitchClass.A, 4), NoteValue.QUARTER);

		double[] pureSamples = pure.synthesize(note, 120, 0.5);
		double[] vibSamples = vib.synthesize(note, 120, 0.5);

		assertNotEquals(pureSamples[1000], vibSamples[1000]);
	}

	@Test
	void nullSynthesizer_throwsException() {
		assertThrows(NullPointerException.class, () -> {
			new VibratoSynthesizer(null, 0.02, 5.0);
		});
	}

	@Test
	void negativeDepth_throwsException() {
		assertThrows(IllegalArgumentException.class, () -> {
			new VibratoSynthesizer(new PureSound(), -0.01, 5.0);
		});
	}

	@Test
	void negativeSpeed_throwsException() {
		assertThrows(IllegalArgumentException.class, () -> {
			new VibratoSynthesizer(new PureSound(), 0.02, -1.0);
		});
	}
}
