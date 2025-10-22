package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;

/**
 * this class represents the adsr envelope
 * @author Antoine Mouille
 */
public class ADSRSynthesizer extends NoteSynthesizerDecorator{

    private double attack=0.0;
    private double decay=0.0;
    private double release=0.0;
    private double sustain=0.0;

    /**
     * this constructer initialize the ADSR envelope
     * @param synthesizer the reference synthesizer
     * @param attack the attack of the ADSR
     * @param decay the decay of the ADSR
     * @param sustain the sustain of the ADSR
     * @param release the release of the ADSR
     */
    public ADSRSynthesizer(NoteSynthesizer synthesizer,double attack, double decay, double sustain, double release) {
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
    public double adsrEnvelope(double t,Note note, int tempo,double volume) {
        int noteDuration = note.getDuration(tempo);
        double newVolume;
        if(t>=0 && t<attack){
            newVolume = t/attack;
        }else if(t>=attack && t<attack+decay){
            newVolume = 1-((t-attack)/decay)*(1-sustain);
        }else if(t>=attack+decay && t<noteDuration-release){
             newVolume = sustain;
        }else if(t>=noteDuration-release && t<noteDuration){
            newVolume = sustain*(1-((t-(noteDuration-release))/release));
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
     * @return the sound of the note after the synthesizer
     */
    @Override
    public double[] synthesize(Note note, int tempo,double volume) {
            double[] sound = super.synthesize(note, tempo, volume);
            int noteDuration = note.getDuration(tempo);
            int n = sound.length;

            for(int i=0;i<n;i++){
                double t = (double) noteDuration*i/n;
                double envelope = adsrEnvelope(t, note, tempo, volume);

                sound[i]*=envelope;

            }

            return sound;

    }

}
