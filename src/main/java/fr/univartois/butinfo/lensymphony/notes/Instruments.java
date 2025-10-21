package fr.univartois.butinfo.lensymphony.notes;

import fr.univartois.butinfo.lensymphony.synthesizer.HarmonicSynthesizer;
import fr.univartois.butinfo.lensymphony.synthesizer.NoteSynthesizer;
import fr.univartois.butinfo.lensymphony.synthesizer.PureSound;

public enum Instruments {
	BASS_DRUM(new PureSound()),
	SNARE_DRUM(new HarmonicSynthesizer(new PureSound(),4)),
	CYMBAL(new HarmonicSynthesizer(new PureSound(),5)),
	TRIANGLE(new HarmonicSynthesizer(new PureSound(),6 )),
	TIMPANI(new HarmonicSynthesizer(new PureSound(),7)),
	XYLOPHONE(new HarmonicSynthesizer(new PureSound(),8));

	private final NoteSynthesizer synthesizer;

	Instruments(NoteSynthesizer synthesizer){
		this.synthesizer = synthesizer;
	}

	public NoteSynthesizer getSynthesizer(){
		return synthesizer;
	}
}
