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
