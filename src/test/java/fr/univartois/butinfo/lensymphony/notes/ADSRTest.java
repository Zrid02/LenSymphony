package fr.univartois.butinfo.lensymphony.notes;

import fr.univartois.butinfo.lensymphony.synthesizer.ADSRSynthesizer;
import fr.univartois.butinfo.lensymphony.synthesizer.NoteSynthesizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires de la classe ADSRSynthesizer.
 */
public class ADSRTest {

    private ADSRSynthesizer adsr;
    private NoteSynthesizer mockSynth;
    private Note note;

    @BeforeEach
    void setUp() {
        // Synthétiseur factice qui renvoie une onde constante (1.0)
        mockSynth = new NoteSynthesizer() {
            @Override
            public double[] synthesize(Note note, int tempo, double volume) {
                int n = 1000; // 1000 échantillons
                double[] sound = new double[n];
                for (int i = 0; i < n; i++) {
                    sound[i] = 1.0; // signal constant
                }
                return sound;
            }
        };

        // ADSR : attack=100, decay=100, sustain=0.5, release=100
        adsr = new ADSRSynthesizer(mockSynth, 100, 100, 0.5, 100);
        NoteFactory noteFactory = NoteFactory.getInstance();
        note = noteFactory.createNote(NotePitch.of(PitchClass.A, 4), NoteValue.EIGHTH);

    }

    @Test
    void testAttackPhase() {
        double v = adsr.adsrEnvelope(50, note, 120, 1.0);
        assertTrue(v > 0.4 && v < 0.6, "Volume doit augmenter pendant l'attack");
    }

    @Test
    void testDecayPhase() {
        double v = adsr.adsrEnvelope(150, note, 120, 1.0);
        assertTrue(v < 1.0 && v > 0.4, "Volume doit baisser pendant le decay");
    }

    @Test
    void testSustainPhase() {
        double v = adsr.adsrEnvelope(250, note, 120, 1.0);
        assertEquals(0.5, v, 0.01, "Volume doit rester au niveau de sustain");
    }

    @Test
    void testReleasePhase() {
        double v = adsr.adsrEnvelope(380, note, 120, 1.0);
        assertTrue(v < 0.5 && v > 0.0, "Volume doit décroître pendant le release");
    }

    @Test
    void testAfterNoteEnds() {
        double v = adsr.adsrEnvelope(450, note, 120, 1.0);
        assertEquals(0.0, v, 0.0001, "Volume doit être nul après la fin de la note");
    }

    @Test
    void testSynthesizeModifiesSignal() {
        double[] result = adsr.synthesize(note, 120, 1.0);

        // Vérifie qu'il y a bien modulation (tous les échantillons ne valent pas 1.0)
        boolean hasDifferentValues = false;
        for (double d : result) {
            if (d != 1.0) {
                hasDifferentValues = true;
                break;
            }
        }
        assertTrue(hasDifferentValues, "Le signal doit être modulé par l’enveloppe ADSR");
    }
}
