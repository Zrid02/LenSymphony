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
	VIOLIN(new HarmonicSynthesizer(new ADSRSynthesizer(new VibratoSynthesizer(new PureSound(),0.01,5),0.1,0.2,0.7,0.3), 10)),

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

	FLUTE(new VibratoSynthesizer(new HarmonicSynthesizerComplex(new ADSRSynthesizer(new WhiteNoiseSynthesizer(new VibratoSynthesizer(new PureSound(),0.01,5), 0.003), 0.09, 0.0, 1.0, 0.3),5,i -> 2 * i - 1,(i, t) -> 1.0 / Math.sqrt(i)),0.01, 5.0	)),

	HARP(new HarmonicSynthesizerComplex(
			new ADSRSynthesizer(new PureSound(), 0.001, 0.1, 0.3, 1.5),
			10,
			i -> i,
			(i, t) -> Math.pow(0.7, i - 1) * Math.exp(-t * (i - 1) * 0.5)
	)),

	OCARINA(new HarmonicSynthesizerComplex(
			new ADSRSynthesizer(
					new VibratoSynthesizer(new PureSound(), 0.003, 4.0),
					0.08, 0.1, 0.75, 0.4
			),
			8,
			i -> i,
			(i, t) -> {
				if (i == 1) return 1.0;
				if (i == 2) return 0.4;
				if (i == 3) return 0.25;
				if (i == 4) return 0.15;
				return 0.08 / (i * i);
			}
	)),

	CELESTA(new HarmonicSynthesizerComplex(
			new ADSRSynthesizer(new PureSound(), 0.005, 0.2, 0.1, 1.2),
			12,
			i -> i,
			(i, t) -> {
				if (i == 1) return 1.0;
				if (i == 2) return 0.6;
				if (i == 3) return 0.4;
				if (i == 4) return 0.3;
				return Math.pow(0.65, i - 1) * Math.exp(-t * i * 0.8);
			}
	)),

	BASSE(new HarmonicSynthesizerComplex(
			new ADSRSynthesizer(
					new PureSound(),
					0.01, 0.08, 0.4, 1.8
			),
			8,
			i -> i,
			(i, t) -> {
				if (i == 1) return 1.0;
				if (i == 2) return 0.7;
				if (i == 3) return 0.5;
				if (i == 4) return 0.35;
				return 0.2 / i * Math.exp(-t * i * 0.6);
			}
	));





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