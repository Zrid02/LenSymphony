package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;

/**
 * this class represents the adsr envelope
 * @author Antoine Mouille
 */
public class ADSRSynthesizer extends NoteSynthesizerDecorator{

    private int attack, decay, release;
    private double sustain;

    /**
     * this constructer initialize the ADSR envelope
     * @param synthesizer the reference synthesizer
     * @param attack the attack of the ADSR
     * @param decay the decay of the ADSR
     * @param sustain the sustain of the ADSR
     * @param release the release of the ADSR
     */
    public ADSRSynthesizer(NoteSynthesizer synthesizer,int attack, int decay, double sustain, int release) {
        super(synthesizer);
        this.attack=attack;
        this.decay=decay;
        this.sustain=sustain;
        this.release=release;
    }

    /**
     * this method is the ADSR envelope, it changes the volume depending on the parameters of this method and the initialisation of the ADSR
     * @param t the duration of the note
     * @param note the note
     * @param tempo the tempo of the note
     * @param volume the volume of the note
     * @return the new volume after the ADSR
     */
    public double ADSREnvelope(double t,Note note, int tempo,double volume) {
        int T = note.getDuration(tempo);
        double newVolume;
        if(t>=0 && t<attack){
            newVolume = t/attack;
        }else if(t>=attack && t<attack+decay){
            newVolume = 1-((t-attack)/decay)*(1-sustain);
        }else if(t>=attack+decay && t<T-release){
             newVolume = sustain;
        }else if(t>=T-release && t<T){
            newVolume = sustain*(1-((t-(T-release))/release));
        }else{
            newVolume =0;
        }
        return volume*newVolume;
    }

    /**
     *  the synthesized of the note with the ADSR
     * @param note   The note to synthesize.
     * @param tempo  The tempo in beats per minute (BPM).
     * @param volume The volume level for the note (0.0 to 1.0).
     *
     * @return
     */
    public double[] synthesize(Note note, int tempo,double volume) {
            double[] sound = super.synthesize(note, tempo, volume);
            int T = note.getDuration(tempo);
            int n = sound.length;

            for(int i=0;i<n;i++){
                double t = (double) T*i/n;
                double envelope = ADSREnvelope(t, note, tempo, volume);

                sound[i]*=envelope;

            }

            return sound;

    }

}
