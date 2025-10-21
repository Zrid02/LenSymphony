package fr.univartois.butinfo.lensymphony.notes;

import fr.univartois.butinfo.lensymphony.synthesizer.HarmonicSynthesizer;
import fr.univartois.butinfo.lensymphony.synthesizer.NoteSynthesizer;
import fr.univartois.butinfo.lensymphony.synthesizer.PureSound;

/**
 * An enumeration of musical instruments that can be used to play notes.
 * Each instrument has its own synthesizer that defines how its notes should sound.
 *
 * @author antoine mouille & Dassonville Ugo
 */
public enum Instruments {
	/**
	 * A bass drum using pure sound synthesis.
	 */
	BASS_DRUM(new PureSound()),

	/**
	 * A snare drum using 4 harmonics.
	 */
	SNARE_DRUM(new HarmonicSynthesizer(new PureSound(), 4)),

	/**
	 * A cymbal using 5 harmonics.
	 */
	CYMBAL(new HarmonicSynthesizer(new PureSound(), 5)),

	/**
	 * A triangle using 6 harmonics.
	 */
	TRIANGLE(new HarmonicSynthesizer(new PureSound(), 6)),

	/**
	 * A timpani using 7 harmonics.
	 */
	TIMPANI(new HarmonicSynthesizer(new PureSound(), 7)),

	/**
	 * A xylophone using 8 harmonics.
	 */
	XYLOPHONE(new HarmonicSynthesizer(new PureSound(), 8));

	/**
	 * The synthesizer used to generate the instrument's sound.
	 */
	private final NoteSynthesizer synthesizer;

	/**
	 * Creates a new instrument with the specified synthesizer.
	 *
	 * @param synthesizer The synthesizer to use for this instrument
	 */
	Instruments(NoteSynthesizer synthesizer) {
		this.synthesizer = synthesizer;
	}

	/**
	 * Gets the synthesizer for this instrument.
	 *
	 * @return The synthesizer used to generate this instrument's sound
	 */
	public NoteSynthesizer getSynthesizer() {
		return synthesizer;
	}
}