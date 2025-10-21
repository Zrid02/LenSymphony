package fr.univartois.butinfo.lensymphony.notes;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * MusicPiece is the representation of a music piece
 * @author antoine mouille
 */
public class MusicPiece implements Iterable<Score>{
    private List<Score> scores = new ArrayList<>();
    private int tempo;

    /**
     * this constructor create a music piece with a tempo
     * @param tempo the tempo of the music piece
     */
    public MusicPiece(int tempo) {
        this.tempo = tempo;
    }

    /**
     * this method return the list of scores
     * @return the list of scores
     */
    public List<Score> getScores() {
        return scores;
    }

    /**
     * this method return the tempo of the music piece
     * @return the tempo
     */
    public int getTempo() {
        return tempo;
    }
    /**
     * this method add a stave in our list of stave
     * @param score a list of notes
     */
    public void addScore(Score score) {
        scores.add(score);
    }

    /**
     * this method iterate our list of staves
     * @return the itiration of the staves
     */
    @Override
    public Iterator<Score> iterator(){
        return scores.iterator();
    }
}
