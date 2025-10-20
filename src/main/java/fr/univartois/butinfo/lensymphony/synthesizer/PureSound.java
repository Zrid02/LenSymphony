package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;

/**
 *
 * this class synthesize a pure sound
 *
 * @author antoine mouille
 */
public class PureSound implements NoteSynthesizer{
    public static final int SAMPLE_RATE = 44100;
    /**
     * this function synthesize a pure sound
     *
     * @param note the reference note to get the frequencies
     * @param tempo the tempo in beats per minutes (BPM)
     * @param volume the volume level for the note (0.0 to 1.0)
     *
     * @return an array of every sample of the synthesized pure sound
     */
    public double[] synthesize(Note note, int tempo, double volume){
        double frequency = note.getFrequency();

        double duration = note.getDuration(tempo);

        int nbSample = (int) (duration*SAMPLE_RATE);

        double[] sounds = new double[nbSample];

        for(int i=0;i<nbSample;i++){
            double t = (double) i /SAMPLE_RATE;
            sounds[i] = volume * Math.sin(2*Math.PI*frequency*t);
        }

        return sounds;
    }
}
