package pal;

import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author zikesjan
 */
public class DoubleState {
    
    public State xState;
    public State yState;
    public boolean start = false;
    public boolean end = false;
    public int id1;
    public int id2;
    public HashMap<Character, HashSet<DoubleState>> edges;

    public DoubleState(State xState, State yState, int id1, int id2) {
        this.xState = xState;
        this.yState = yState;
        this.id1 = id1;
        this.id2 = id2;
        edges = new HashMap<Character, HashSet<DoubleState>>();
    }
    
    public void addEdge(char ch, DoubleState state){
        if(edges.containsKey(ch)){
            if(!edges.get(ch).contains(state)){
                edges.get(ch).add(state);
            }
        }else{
            HashSet<DoubleState> hs = new HashSet<DoubleState>();
            hs.add(state);
            edges.put(ch, hs);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final DoubleState other = (DoubleState) obj;
        if (this.id1 != other.id1) {
            return false;
        }
        if (this.id2 != other.id2) {
            return false;
        }
        return true;
    }
    
    
    
    
}
