package pal;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zikesjan
 */
public class Concat implements AbstractNode {

    public AbstractNode right;
    public AbstractNode left;
    private State beggining;
    private List<State> end;

    public Concat(AbstractNode right, AbstractNode left) {
        this.right = right;
        this.left = left;
        end = new ArrayList<State>();
        this.buildAutomaton();
    }

    @Override
    public void buildAutomaton() {
        this.beggining = left.getBeginning();
        this.end.addAll(right.getEnd());

        
        for (State s : left.getEnd()) {

            for (char ch : right.getBeginning().edges.keySet()) {
                for (State s2 : right.getBeginning().edges.get(ch)) {
                    s.addEdge(ch, s2);
                }
            }
        }
        if (right.getEnd().contains(right.getBeginning())) {
            this.end.addAll(left.getEnd());
        }

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
