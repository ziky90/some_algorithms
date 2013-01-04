package pal;

import java.util.ArrayList;

/**
 *
 * @author zikesjan
 */
public class Node {
    
    public int id;
    
    public ArrayList<Integer> edgeValues = new ArrayList<Integer>();
    
    public ArrayList<Node> edgeNodes = new ArrayList<Node>();
    
    public int index = 0;
    
    public int lowLink;
    
    public int componentId;
    
    public int minPath;
    
    public int componentMax;
    
    public boolean inStack;

   
    
    
}
