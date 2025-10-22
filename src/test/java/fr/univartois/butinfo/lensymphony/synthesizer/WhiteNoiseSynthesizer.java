package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.*;
import fr.univartois.butinfo.lensymphony.synthesizer.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WhiteNoiseSynthesizerTest {

    @Test
    void createWhiteNoiseSynthesizer() {
        NoteSynthesizer base = new PureSound();
        NoteSynthesizer noisy = new WhiteNoiseSynthesizer(base, 0.02);

        assertNotNull(noisy);
    }

    @Test
    void synthesizeWithNoise() {
        NoteSynthesizer noisy = new WhiteNoiseSynthesizer(new PureSound(), 0.02);
        Note note = new PitchedNote(NotePitch.of(PitchClass.A, 4), NoteValue.QUARTER);

        double[] samples = noisy.synthesize(note, 120, 0.5);

        assertEquals(22050, samples.length);
    }

    @Test
    void differentFromPureSound() {
        NoteSynthesizer pure = new PureSound();
        NoteSynthesizer noisy = new WhiteNoiseSynthesizer(pure, 0.02);
        Note note = new PitchedNote(NotePitch.of(PitchClass.A, 4), NoteValue.QUARTER);

        double[] pureSamples = pure.synthesize(note, 120, 0.5);
        double[] noisySamples = noisy.synthesize(note, 120, 0.5);

        assertNotEquals(pureSamples[1000], noisySamples[1000]);
    }

    @Test
    void nullSynthesizer_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            new WhiteNoiseSynthesizer(null, 0.02);
        });
    }

    @Test
    void negativeAmplitude_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new WhiteNoiseSynthesizer(new PureSound(), -0.01);
        });
    }
}