package pal;

import java.util.List;

/**
 *
 * @author zikesjan
 */
public interface AbstractNode {
    
    public void buildAutomaton();
    
    public String getName();
    
    public State getBeginning();
    
    public List<State> getEnd();    //If it's goona be slow rewrite by HashSet
}
