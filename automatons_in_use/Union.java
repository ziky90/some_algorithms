package pal;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zikesjan
 */
public class Union implements AbstractNode{

    public AbstractNode right;
    public AbstractNode left;
    private State beginning;
    private List<State> end;
    
    public Union(AbstractNode right, AbstractNode left){
        this.left = left;
        this.right = right;
        end = new ArrayList<State>();
        this.buildAutomaton();
    }
    
    @Override
    public void buildAutomaton() {
        beginning = new State();
        for(char ch : left.getBeginning().edges.keySet()){
            for(State state : left.getBeginning().edges.get(ch)){
                beginning.addEdge(ch, state);
            }
        }
        if(left.getEnd().contains(left.getBeginning())){
                this.end.add(beginning);
            }
        for(char ch : right.getBeginning().edges.keySet()){
            for(State state : right.getBeginning().edges.get(ch)){
                beginning.addEdge(ch, state);
            }
        }
        if(right.getEnd().contains(right.getBeginning())){
                this.end.add(beginning);
            }
        StatesStorage states = StatesStorage.getInstance();
        states.addState(beginning);
        end.addAll(left.getEnd());
        end.addAll(right.getEnd());
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
