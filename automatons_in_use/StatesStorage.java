package pal;

import java.util.HashSet;

/**
 *
 * @author zikesjan
 */
public class StatesStorage {
    
    private static StatesStorage instance;
    private HashSet<State> automaton1;
    private HashSet<State> automaton2;
    private boolean automata2;
    private int id;
    
    private StatesStorage(){
        automaton1 = new HashSet<State>();
        automaton2 = new HashSet<State>();
        automata2 = false;
    }
    
    /**
     * 
     * will not work with threads here, but hope that threads will not be needed in PAL 
     */
    public static StatesStorage getInstance(){
        if(instance == null){
            instance = new StatesStorage();
        }
        return instance;
    }
    
    public void addState(State state){
        if(!automata2){
            state.id = id;
            automaton1.add(state);
            id++;
        }else{
            state.id = id;
            automaton2.add(state);
            id++;
        }
    }
    
    public void removeState(State state){
        if(!automata2){
            automaton1.remove(state);
        }else{
            automaton2.remove(state);
        }
    }
    
    public HashSet<State> getAutoma1(){
        return automaton1;
    }
    
    public HashSet<State> getAutoma2(){
        return automaton2;
    }
    
    public void switchAutoma(){
        automata2 = true;
        id = 0;
    }
    
}
