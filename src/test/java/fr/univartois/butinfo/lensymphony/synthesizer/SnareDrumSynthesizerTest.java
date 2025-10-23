package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;
import fr.univartois.butinfo.lensymphony.notes.NoteFactory;
import fr.univartois.butinfo.lensymphony.notes.NotePitch;
import fr.univartois.butinfo.lensymphony.notes.NoteValue;
import fr.univartois.butinfo.lensymphony.notes.PitchClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires de la classe SnareDrumSynthesizer.
 */
public class SnareDrumSynthesizerTest {

    private SnareDrumSynthesizer snare;
    private Note note;

    @BeforeEach
    void setUp() {
        snare = SnareDrumSynthesizer.getInstance();

        // Création d'une note pour les tests
        NoteFactory factory = NoteFactory.getInstance();
        note = factory.createNote(
                NotePitch.of(PitchClass.C, 4),
                NoteValue.QUARTER
        );
    }

    @Test
    void testSingletonInstance() {
        SnareDrumSynthesizer s1 = SnareDrumSynthesizer.getInstance();
        SnareDrumSynthesizer s2 = SnareDrumSynthesizer.getInstance();
        assertSame(s1, s2, "SnareDrumSynthesizer doit être un singleton");
    }

    @Test
    void testEnvelopeAttackPhase() {
        double v = snare.envelope(0.005, 1.0); // au milieu de l’attaque (0.01 s)
        assertTrue(v > 0.4 && v < 0.6, "Le volume doit augmenter durant l’attaque");
    }


    @Test
    void testEnvelopeLongAfterAttack() {
        double v = snare.envelope(0.3, 1.0); // après 300 ms
        assertTrue(v < 0.05, "Le volume doit presque être nul après la décroissance");
    }

    @Test
    void testEnvelopeNeverNegative() {
        for (double t = 0; t < 1.0; t += 0.001) {
            double v = snare.envelope(t, 1.0);
            assertTrue(v >= 0.0, "L’enveloppe ne doit jamais être négative");
        }
    }

    @Test
    void testSynthesizeProducesNoise() {
        double[] sound = snare.synthesize(note, 120, 1.0);
        assertNotNull(sound, "Le tableau de son ne doit pas être nul");
        assertTrue(sound.length > 0, "Le son doit contenir des échantillons");

        // Vérifie qu'il y a de la variabilité (du bruit)
        double first = sound[0];
        boolean hasVariation = false;
        for (double s : sound) {
            if (s != first) {
                hasVariation = true;
                break;
            }
        }
        assertTrue(hasVariation, "Le son doit contenir des variations (bruit blanc)");
    }

    @Test
    void testEnvelopeAffectsAmplitude() {
        double[] sound = snare.synthesize(note, 120, 1.0);
        double maxBeforeAttack = 0.0;
        double maxAfterAttack = 0.0;

        // Premier 5 ms (attaque)
        int samples = sound.length;
        int attackEnd = (int) (0.01 * SnareDrumSynthesizer.SAMPLE_RATE);

        for (int i = 0; i < attackEnd && i < samples; i++) {
            maxBeforeAttack = Math.max(maxBeforeAttack, Math.abs(sound[i]));
        }

        // Après 50 ms
        int after = (int) (0.05 * SnareDrumSynthesizer.SAMPLE_RATE);
        for (int i = after; i < Math.min(after + 500, samples); i++) {
            maxAfterAttack = Math.max(maxAfterAttack, Math.abs(sound[i]));
        }

        assertTrue(maxBeforeAttack > maxAfterAttack,
                "Le signal doit décroître après la phase d’attaque");
    }
}
