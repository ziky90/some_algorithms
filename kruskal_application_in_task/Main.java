package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 *
 * @author zikesjan
 */
public class Main {

    static int numberOfBuildings;
    static int numberOfBackboneBuildings;
    static int p1;
    static int p2;
    static int discounnt;
    static Node[] nodes;
    static int superNodeId1 = -1;
    static int superNodeId2 = -1;
    static Edge[][] adjecencyMatrix;
    static Edge[] edges;
    static int edgesLength;
    static HashSet<Integer> used = new HashSet<Integer>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            readFromStdInFast();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //long start = System.currentTimeMillis();
        double result = kruskal(makeGraph());
        double resultDiscount = calculateCheapestDiscounted();
        DecimalFormat df = new DecimalFormat("#.00");
        if (superNodeId2 == -1) {
            System.out.println(df.format(result) + " " + df.format(resultDiscount) + "\n" + superNodeId1);
        } else {
            System.out.println(df.format(result) + " " + df.format(resultDiscount) + "\n" + superNodeId1 + " " + superNodeId2);
        }
        //long end = System.currentTimeMillis();
        //System.out.println("Execution time was " + (end - start) + " ms.");
    }

    static double calculateCheapestDiscounted() {
        double cheapest = Double.MAX_VALUE;
        if (numberOfBackboneBuildings == 1) {
            int[] indexes = new int[numberOfBuildings-1];
            for (int i = 0; i < numberOfBuildings; i++) {
                Edge[] discountedEdges = nodes[i].getEdges();
                used.clear();
                int position = 0;
                for (Edge e : discountedEdges) {
                    e.setActualValue(e.getDiscounedPrice());
                    indexes[position] = e.getSortedIndex();
                    used.add(e.getSortedIndex());
                    position++;
                }
                
                
                reprepareNodes();
                double value = kruskal(mergeArrayLists(edges, indexes));
                for (Edge e : discountedEdges) {
                    e.setActualValue(e.getPrice());
                }
                if (value < cheapest) {
                    cheapest = value;
                    superNodeId1 = i;
                }
            }
        } else {
            int[] indexesi = new int[numberOfBuildings-1];
            int[] indexesj = new int[numberOfBuildings-2];
            
            for (int i = 0; i < numberOfBuildings - 1; i++) {
                
                Edge[] discountedEdgesi = nodes[i].getEdges();
                int positioni = 0;
                for (Edge e : discountedEdgesi) {
                    e.setActualValue(e.getDiscounedPrice());
                    indexesi[positioni] = e.getSortedIndex();
                    positioni++;
                }
                
                for (int j = i + 1; j < numberOfBuildings; j++) {

                    Edge[] discountedEdgesj = nodes[j].getEdges();
                    
                    int positionj = 0;
                    for (Edge e : discountedEdgesj) {
                        if(e.getP1() != i && e.getP2() != i){
                            e.setActualValue(e.getDiscounedPrice());
                            indexesj[positionj] = e.getSortedIndex();
                            positionj++;
                        }
                    }
                    
                    

                    reprepareNodes();
                    
                    double value = kruskal(mergeArrayLists(edges, mergeIntegerArrays(indexesi, indexesj)));
                    
                    
                    for (Edge e : discountedEdgesj) {
                        if(e.getP1() != i && e.getP2() != i){
                            e.setActualValue(e.getPrice());
                        }
                    }
                    
                    
                    if (value < cheapest) {
                        cheapest = value;
                        superNodeId1 = i;
                        superNodeId2 = j;
                    }
                }
                
                for (Edge e : discountedEdgesi) {
                        e.setActualValue(e.getPrice());
                }
                
            }
        }
        


        return cheapest;
    }

    
    static int[] mergeIntegerArrays(int[] a, int[] b){
        int[] result = new int[(2*numberOfBuildings)-3];
        int pointerA = 0;
        int pointerB = 0;
        int aLength = numberOfBuildings-1;
        int bLength = numberOfBuildings-2;
        used.clear();
        while(pointerA<aLength && pointerB<bLength){
            if(a[pointerA]<b[pointerB]){
                result[pointerA+pointerB] = a[pointerA];
                used.add(a[pointerA]);
                pointerA++;
            }else{
                result[pointerA+pointerB] = b[pointerB];
                used.add(b[pointerB]);
                pointerB++;
            }
        }
        if(pointerA==aLength){
            while(pointerB<bLength){
                result[pointerA+pointerB] = b[pointerB];
                used.add(b[pointerB]);
                pointerB++;
            }
        }else{
            while(pointerA<aLength){
                result[pointerA+pointerB] = a[pointerA];
                used.add(a[pointerA]);
                pointerA++;
            }
        }
        return result;
    }
    
    
    static Edge[] mergeArrayLists(Edge[] original, int[] indexes) {
        Edge[] result = new Edge[edgesLength];
        int counter = 0;
        int pointerI = 0;
        int resultPointer = 0;
        
        while (counter<edgesLength && pointerI<indexes.length && resultPointer<edgesLength) {
            
            
            if (original[counter].compareTo(original[indexes[pointerI]]) == 1) {
                result[resultPointer] = original[indexes[pointerI]];
                resultPointer++;
                pointerI++;
            } else {
                
                result[resultPointer] = original[counter];
                resultPointer++;
                counter++;
                
                
            }
            
            while (used.contains(counter)) {
                    counter++;
            }
        }
        if (pointerI<indexes.length && resultPointer<edgesLength) {
            for (int i = counter; i < edgesLength; i++) {
                if (!used.contains(i)) {
                    result[resultPointer] = original[i];
                    resultPointer++;
                }
            }
        }
        
        return result;
    }

    /*
     * creating priority queue from all the edges
     */
    static Edge[] makeGraph() {
        int offset = 1;
        nodes = new Node[numberOfBuildings];
        edges = new Edge[((numberOfBuildings*numberOfBuildings)-numberOfBuildings)/2];
        adjecencyMatrix = new Edge[numberOfBuildings][numberOfBuildings];
        int id;
        int price;
        int edgesPosition = 0;
        for (int i = 0; i < numberOfBuildings; i++) {
            nodes[i] = new Node(numberOfBuildings-1);
            nodes[i].setId(i);
            for (int j = offset; j < numberOfBuildings; j++) {
                id = 1 + i + (j * (j - 1) / 2);
                price = (p1 * id) % p2;
                Edge e = new Edge(price, price, price - (price * (discounnt / 100d)), i, j);
                edges[edgesPosition] = e;
                adjecencyMatrix[i][j] = e;
                edgesPosition++;
            }
            offset++;
        }
        
        Arrays.sort(edges);
        int position = 0;
        for (Edge e : edges) {
            e.setSortedIndex(position);
            nodes[e.getP1()].addEdge(e);
            nodes[e.getP2()].addEdge(e);
            position++;
        }
        edgesLength = edges.length;
        return edges;

    }

    static void reprepareNodes() {

        
        for(Node n : nodes){
            n.setAncestor(null);
        }

    }

    /*
     * Kruskal's algorithm to find the Spanning tree
     */
    static double kruskal(Edge[] edges) {
        double price = 0;
        int numOfEdges = 0;
        int pointer = 0;
        while (numOfEdges < numberOfBuildings - 1) {

            Edge edge = edges[pointer];
            pointer++;
            if (find(edge.getP1()) != find(edge.getP2())) {
                price += edge.getActualValue();
                numOfEdges++;
                union(edge.getP1(), edge.getP2());
            }
        }
        return price;
    }

    static int union(int x, int y) {
        Node a = nodes[find(x)];
        Node b = nodes[find(y)];
        b.setAncestor(a);
        return a.getId();
    }

    static int find(int x) {
        Node node = nodes[x];
        int jump = 0;
        while (node.getAncestor() != null) {
            node = node.getAncestor();
            jump++;
        }
        if (jump > 1) {
            Node current = nodes[x];
            while (current.getId() != node.getId()) {
                current.setAncestor(nodes[node.getId()]);
                current = current.getAncestor();;
            }
        }
        return node.getId();
    }

    /*
     * just reading of the data from the system
     */
    static void readFromStdInFast() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        numberOfBuildings = Integer.parseInt(st.nextToken());
        numberOfBackboneBuildings = Integer.parseInt(st.nextToken());
        p1 = Integer.parseInt(st.nextToken());
        p2 = Integer.parseInt(st.nextToken());
        discounnt = Integer.parseInt(st.nextToken());



    }
}