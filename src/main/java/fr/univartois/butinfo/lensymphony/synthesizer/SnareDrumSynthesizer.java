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
     * @param volume the volume of the instruments
     * @return the envelope at an instant time
     */
    public double envelope(double t, double volume){
        double newVolume;
        if(t<attack){
             newVolume= t/attack;
        }else if(t>=attack){
            newVolume = Math.exp(15*(attack-t));
        }else{
            newVolume = 0;
        }

        return newVolume*volume;
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

        double noteDuration = note.getDuration(tempo)/1000.0;

        int nbSample = (int) (noteDuration*SAMPLE_RATE);

        double[] sounds = new double[nbSample];



        for(int i=0; i<nbSample; i++){
            double t = (double) i/SAMPLE_RATE;
            double r = random.nextDouble()*2.0-1.0;
            sounds[i] = volume* envelope(t,volume) * r ;
        }

        return sounds;
    }
}
