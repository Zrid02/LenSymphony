package fr.univartois.butinfo.lensymphony.notes;
import fr.univartois.butinfo.lensymphony.synthesizer.Stave;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * MusicPiece is the representation of a music piece
 * @author antoine mouille
 */
public class MusicPiece implements Iterable<Stave>{
    private List<Stave> staves = new ArrayList<>();

    /**
     * this method add a stave in our list of stave
     * @param stave a list of notes
     */
    public void addStave(Stave stave) {
        staves.add(stave);
    }

    /**
     * this method iterate our list of staves
     * @return the itiration of the staves
     */
    @Override
    public Iterator<Stave> iterator(){
        return staves.iterator();
    }
}
