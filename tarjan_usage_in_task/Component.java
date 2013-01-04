package pal;

import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author zikesjan
 */
public class Component {
    
    public int id;
    
    public HashSet<Node> nodes = new HashSet<Node>();
    
    public int usagePrice;
    
    public int maxPath;
    
    public int minPath;
    
    public HashMap<Component, Integer> edges = new HashMap<Component, Integer>();

    
    
    
}
