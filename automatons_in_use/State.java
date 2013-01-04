package pal;

import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author zikesjan
 */
public class State {
    
    public HashMap<Character, HashSet<State>> edges;    
    public boolean start = false;
    public boolean end = false;
    public int id;
    
    public State(){
        edges = new HashMap<Character, HashSet<State>>();
    }
    
    public void addEdge(char ch, State state){
        if(edges.containsKey(ch)){
            if(!edges.get(ch).contains(state)){
                edges.get(ch).add(state);
            }
        }else{
            HashSet<State> hs = new HashSet<State>();
            hs.add(state);
            edges.put(ch, hs);
        }
    }
    
}
