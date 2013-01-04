package pal;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zikesjan
 */
public class Star implements AbstractNode{

    public AbstractNode descendant;
    
    private State beginning;
    private List<State> end;
    
    public Star(AbstractNode descendant){
        this.descendant = descendant;
        end = new ArrayList<State>();
        this.buildAutomaton();
    }
    
    @Override
    public void buildAutomaton() {
        beginning = descendant.getBeginning();
        end.add(beginning);
        for(State s : descendant.getEnd()){
            for(char ch : beginning.edges.keySet()){
                for(State s2 : beginning.edges.get(ch)){
                    s.addEdge(ch, s2);
                }
            }
        }
        end.addAll(descendant.getEnd());
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public State getBeginning() {
        return beginning;
    }

    @Override
    public List<State> getEnd() {
        return end;
    }
    
}
