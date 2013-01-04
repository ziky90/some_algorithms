package pal;

/**
 *
 * @author zikesjan
 */
public class Node {
    
    private Node ancestor;
        
    private int id;
    
    //private ArrayList<Edge> edges = new ArrayList<Edge>();
    private Edge[] edges;
    
    private int edgePointer = 0;
    
    

    public Node(int nodes) {
        edges = new Edge[nodes];
    }
    
    
    
    public Node getAncestor() {
        return ancestor;
    }

    public void setAncestor(Node ancestor) {
        this.ancestor = ancestor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Edge[] getEdges() {
        return edges;
    }
    
    public void addEdge(Edge e){
        edges[edgePointer] = e;
        edgePointer++;
    }
    
}
