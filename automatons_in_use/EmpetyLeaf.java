package pal;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zikesjan
 */
public class EmpetyLeaf implements AbstractNode{

    private State beggining;
    private List<State> end;
    
    public EmpetyLeaf(){
        end = new ArrayList<State>();
        buildAutomaton();
    }
    
    @Override
    public void buildAutomaton() {
        beggining = new State();
        end.add(beggining);
        StatesStorage states = StatesStorage.getInstance();
        states.addState(beggining);
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
