package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;

import java.util.Random;

public class SnareDrumSynthesizer implements NoteSynthesizer{

    private double attack=0.0;
    Random random = new Random();

    /**
     * constructor of this class
     * @param attack the attack of the sound
     */
    public SnareDrumSynthesizer(double attack){
        if(attack<=0){
            throw new IllegalArgumentException();
        }
        this.attack=attack;
    }

    /**
     * this method calculate the envelope of the sound at an instant time
     * @param t a given instant time
     * @return the envelope at an instant time
     */
    public double envelope(double t){
        double e;

        if(t<attack){
             e = t/attack;
        }else{
            e = Math.exp(15*(attack-t));
        }

        return e;
    }

    /**
     * Computes the audio samples for a given note.
     *
     * @param note   The note to synthesize.
     * @param tempo  The tempo in beats per minute (BPM).
     * @param volume The volume level for the note (0.0 to 1.0).
     * @return An array of audio sample representing the synthesized note.
     */
    @Override
    public double[] synthesize(Note note, int tempo, double volume) {
        int noteDuration = note.getDuration(tempo);
        double[] sounds = new double[noteDuration];

        for(int i=0; i<noteDuration; i++){
            sounds[i] = volume * envelope(i) * random.nextDouble(-1,1) ;
        }

        return sounds;
    }
}
