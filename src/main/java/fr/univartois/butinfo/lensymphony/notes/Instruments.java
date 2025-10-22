package fr.univartois.butinfo.lensymphony.notes;

import fr.univartois.butinfo.lensymphony.synthesizer.*;

/**
 * An enumeration of musical instruments that can be used to play notes.
 * Each instrument has its own synthesizer that defines how its notes should sound.
 *
 * @author Mouille Antoine, Rabhi Nessim & Dassonville Ugo
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
	XYLOPHONE(new HarmonicSynthesizer(new PureSound(), 8)),

	/*
	 A violin using 10 harmonics
	 */
	VIOLIN(new HarmonicSynthesizer(new PureSound(), 10)), // changed PureSound

	/*
	A guitar using 8 harmonics
	 */

	GUITAR(new HarmonicSynthesizer(new ADSRSynthesizer(new VibratoSynthesizer(new PureSound(),0.02,3),0.008,0.05,0.2,2.5), 8)),

	/*
	A piano using 10 harmonics
	 */

	PIANO(new HarmonicSynthesizer(new ADSRSynthesizer(new PureSound(),0.01,0.3,0.2,0.5),10)), // changed PureSound

	/*
	A flute using harmonics odd
	 */

	FLUTE(new VibratoSynthesizer(new HarmonicSynthesizerComplex(new ADSRSynthesizer(new WhiteNoiseSynthesizer(new VibratoSynthesizer(new PureSound(),0.01,5), 0.003), 0.09, 0.0, 1.0, 0.3),5,i -> 2 * i - 1,(i, t) -> 1.0 / Math.sqrt(i)),0.01, 5.0	));

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