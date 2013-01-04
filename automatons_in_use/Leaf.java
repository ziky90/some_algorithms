package pal;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zikesjan
 */
public class Leaf implements AbstractNode{

    public char[] ch;
    private State beggining;
    private List<State> end;
    
    public Leaf(char[] ch){
        this.ch = ch;
        end = new ArrayList<State>();
        this.buildAutomaton();
    }
    
    @Override
    public void buildAutomaton() {
        beggining = new State();
        State endState = new State();
        
        StatesStorage states = StatesStorage.getInstance();
        for(char ch2 : ch){
            beggining.addEdge(ch2, endState);
        }
        end.add(endState);
        states.addState(beggining);
        states.addState(endState);
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public State getBeginning() {
        return beggining;
    }

    @Override
    public List<State> getEnd() {
        return end;
    }
    
}
