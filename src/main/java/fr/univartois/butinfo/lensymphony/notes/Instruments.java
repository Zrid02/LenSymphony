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
	BASS_DRUM(BassDrumSynthesizer.getInstance()),

	/**
	 * A snare drum using 4 harmonics.
	 */
	SNARE_DRUM(SnareDrumSynthesizer.getInstance()),

	/**
	 * A cymbal using 5 harmonics.
	 */
	CYMBAL(CymbaleSynthesizer.getInstance()),

	/**
	 * A triangle using 6 harmonics.
	 */
	TRIANGLE(TriangleSynthesizer.getInstance()),

	/**
	 * A timpani using 7 harmonics.
	 */
	TIMPANI(TimpaniSynthesizer.getInstance()),

	/**
	 * A xylophone using 8 harmonics.
	 */
	XYLOPHONE(XylophoneSynthesizer.getInstance()), //BaseFrequency on s'en fout un peu

	/*
	 A violin using 10 harmonics
	 */
	VIOLIN(new VibratoSynthesizer(
			new ADSRSynthesizer(new HarmonicSynthesizer(new PureSound(), 10),0.1, 0.2, 0.7, 0.3
			),
			0.01,5
	)),
	/*
	A guitar using 8 harmonics
	 */

	GUITAR(new VibratoSynthesizer(
			new ADSRSynthesizer(
					new HarmonicSynthesizer(new PureSound(), 8),
					0.008, 0.05, 0.2, 2.5
			),
			0.02,3)
	),

	/*
	A piano using 10 harmonics
	 */

	PIANO(new ADSRSynthesizer(
			new HarmonicSynthesizerComplex(
					new PureSound(),
					10,
					i -> i,
					(i, t) -> Math.exp(-2 * i * t) / i
			),
			0.01, 0.3, 0.2, 0.5
	)),


	FRENCH_HORN(new VibratoSynthesizer(
			new ADSRSynthesizer(
					new HarmonicSynthesizer(new PureSound(), 11),
					0.1, 0.08, 0.75, 0.9
			),
			0.012, 3.8)
	),

	ACCORDION(new VibratoSynthesizer(
			new ADSRSynthesizer(
					new HarmonicSynthesizer(new PureSound(), 8),
					0.04, 0.02, 0.85, 0.5
			),
			0.015, 4.2)
	),

	GASBA(new WhiteNoiseSynthesizer(
			new VibratoSynthesizer(
					new ADSRSynthesizer(
							new HarmonicSynthesizer(new PureSound(), 4),
							0.1, 0.05, 0.55, 1.2
					),
					0.012, 4.0
			),
			0.015)
	),


	/*
	A flute using harmonics odd
	 */

	FLUTE(new VibratoSynthesizer(
			new HarmonicSynthesizerComplex(
					new ADSRSynthesizer(
							new WhiteNoiseSynthesizer(new PureSound(), 0.003),
							0.09, 0.0, 1.0, 0.3
					),
					5,
					i -> 2 * i - 1,
					(i, t) -> 1.0 / Math.pow(3, i - 1)
			),
			0.01, 5.0
	)),

	BANJO(new ADSRSynthesizer(
			new HarmonicSynthesizer(new PureSound(), 12),
			0.003, 0.15, 0.3, 1.8
	)),

	HARP(new HarmonicSynthesizerComplex(
			new ADSRSynthesizer(new PureSound(), 0.001, 0.1, 0.3, 1.5),
			10,
			i -> i,
			(i, t) -> Math.pow(0.7, i - 1) * Math.exp(-t * (i - 1) * 0.5)
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