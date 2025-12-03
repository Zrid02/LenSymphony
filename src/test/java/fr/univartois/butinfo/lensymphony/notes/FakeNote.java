package fr.univartois.butinfo.lensymphony.notes;

/**
 * A simple fake Note implementation for testing decorators.
 * It provides a fixed frequency and a fixed duration, ignoring tempo.
 */
class FakeNote implements Note {
    private final double frequency;
    private final int duration;

    /**
     * Creates a fake note with a fixed frequency and duration.
     * @param frequency The frequency this note will return.
     * @param duration The duration this note will return (ignores tempo).
     */
    public FakeNote(double frequency, int duration) {
        this.frequency = frequency;
        this.duration = duration;
    }

    @Override
    public double getFrequency() {
        return this.frequency;
    }

    @Override
    public int getDuration(int tempo) {
        // Ignore tempo and return the fixed duration
        return this.duration;
    }
}