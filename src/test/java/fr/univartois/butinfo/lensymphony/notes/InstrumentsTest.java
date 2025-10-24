package fr.univartois.butinfo.lensymphony.notes;

import fr.univartois.butinfo.lensymphony.synthesizer.NoteSynthesizer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Instruments enum.
 */
class InstrumentsTest {

    @Test
    void testAllInstrumentsHaveSynthesizer() {
        // This test simply iterates all enum values to ensure getSynthesizer() works
        for (Instruments instrument : Instruments.values()) {
            NoteSynthesizer synthesizer = instrument.getSynthesizer();
            assertNotNull(synthesizer,
                    "Instrument " + instrument.name() + " has a null synthesizer.");
        }
    }

    @Test
    void testSynthesizerIsSameInstance() {
        // Test that composed synthesizers (like PIANO) return the same instance
        NoteSynthesizer piano1 = Instruments.PIANO.getSynthesizer();
        NoteSynthesizer piano2 = Instruments.PIANO.getSynthesizer();

        assertSame(piano1, piano2,
                "getSynthesizer() should return the same instance each time.");
    }
}