package pal;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author zikesjan
 */
public class DFAState {
    public HashMap<Character, DFAState> edges;    
    public boolean start = false;
    public boolean end = false;
    public int id;
    public ArrayList<DoubleState> parrents = new ArrayList<DoubleState>();
    
    public DFAState(int id){
        edges = new HashMap<Character, DFAState>();
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DFAState other = (DFAState) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    
    
    
}
