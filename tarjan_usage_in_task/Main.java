package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 *
 * @author zikesjan
 */
public class Main {

    private static int numberOfHuts;
    private static long p1;
    private static int p2;
    private static int p3;
    private static int p4;
    private static int k;
    private static Node[] huts;
    private static int resultAlice;
    private static int resultBob;
    private static int index = 1;
    private static Stack<Node> stack = new Stack<Node>();
    private static Stack<Integer> edgesStack = new Stack<Integer>();
    private static HashMap<Integer, Component> roots = new HashMap<Integer, Component>(); 
    private static HashMap<Integer, Component> components = new HashMap<Integer, Component>();
    
    private static ArrayList<int[]>[] componentsConectors;
    
    private static int componentMax;
    private static int componentCounter;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
        try {
            readFromStdInFast();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        long start = System.currentTimeMillis();
        createGraph();
        //long end = System.currentTimeMillis();
        //System.out.println("Graph creating time was " + (end - start) + " ms.");
        //start = System.currentTimeMillis();
        componentsConectors = new ArrayList[numberOfHuts];   
        tarjansAlgorithm();
        //end = System.currentTimeMillis();
        findResults();
        //System.out.println("Tarjan's time was " + (end - start) + " ms.");
        System.out.println(resultAlice + " " + resultBob);
        long end = System.currentTimeMillis();
        System.out.println("Run time was " + (end - start) + " ms.");
        
    }
    
    static void findResults(){
        resultAlice = 2147483647;
        for(Component c : roots.values()){      
            if(c.maxPath > resultBob){
                resultBob = c.maxPath;
            }
            if(c.minPath < resultAlice){
                resultAlice = c.minPath;
            }
        }
        
    }
    
   

    static void tarjansAlgorithm() {

        for (Node n : huts) {
            if (n.index == 0) {
                findStronglyConnectedComponent(n, null);
            }
        }
    }

    static void findStronglyConnectedComponent(Node n, Node parrent) {
        
        n.index = index;
        n.lowLink = index;
        index++;
        stack.push(n);
        n.inStack = true;
        
        for (int i = 0; i < n.edgeNodes.size(); i++) {      
            Node sucessor = n.edgeNodes.get(i);
            if (sucessor.index == 0) {
                edgesStack.push(n.edgeValues.get(i));         
                findStronglyConnectedComponent(sucessor, n);                    
                if (sucessor.lowLink < n.lowLink) {
                    n.lowLink = sucessor.lowLink;
                }
            } else if (sucessor.inStack) {      
                if (sucessor.index < n.lowLink) {
                    n.lowLink = sucessor.index;
                }
                if (n.edgeValues.get(i) > n.componentMax) {  
                    n.componentMax = n.edgeValues.get(i);
                }
            } else {
                if(componentsConectors[n.id] == null){
                    componentsConectors[n.id] = new ArrayList<int[]>();
                }
                componentsConectors[n.id].add(new int[]{sucessor.componentId, n.edgeValues.get(i)});
                roots.remove(sucessor.componentId);        //removing potential roots that are actually not roots
                
                if(n.edgeValues.get(i) + sucessor.minPath < n.minPath || n.minPath==0){         //for Alice's path searching
                    n.minPath = n.edgeValues.get(i) + sucessor.minPath;
                }
            }
        }
        
        
        if (n.lowLink == n.index) {
            int componentMin = 2147483647;                     
            Node node = stack.pop();
            node.inStack = false;
            Component c = new Component();
            while (node.id != n.id) {
                node.componentId = componentCounter;
                c.nodes.add(node);
                int candidate = edgesStack.pop();
                if (candidate > componentMax) {         
                    componentMax = candidate;
                }

                
                if(componentsConectors[node.id] != null){
                    for(int[] connector : componentsConectors[node.id]){
                        Component component = components.get(connector[0]);
                        if(c.maxPath < (component.maxPath + connector[1])){
                            c.maxPath = component.maxPath + connector[1];
                        }
                    }
                }
                
                
                if(node.componentMax > componentMax){
                    componentMax = node.componentMax;
                }
                if(componentMin > node.minPath && node.minPath != 0){
                    componentMin = node.minPath;
                }
                node = stack.pop();
                node.inStack = false;
            }
            //I know twice the same code is not nice. If there's gonna be some time I'll repair this
            c.nodes.add(node);
            if(componentsConectors[node.id] != null){
                    for(int[] connector : componentsConectors[node.id]){
                        Component component = components.get(connector[0]);
                        if(c.maxPath < (component.maxPath + connector[1])){
                            c.maxPath = component.maxPath + connector[1];
                        }
                    }
                }
            if(node.componentMax > componentMax){
                    componentMax = node.componentMax;
                }
            if(componentMin > node.minPath && node.minPath != 0){
                    componentMin = node.minPath;
                }
            
            node.componentId = componentCounter;
            
            if(c.maxPath!=0){
                c.minPath = componentMin;
                for(Node tested : c.nodes){
                    if(tested.minPath >  (componentMin + componentMax) || tested.minPath == 0){
                        tested.minPath = (componentMin + componentMax);
                    }
                }
            }
            c.usagePrice = componentMax;
            c.maxPath = componentMax+c.maxPath; 
            
            
            
            if (stack.empty()) {
                
                roots.put(componentCounter, c);   
            }

            componentMax = 0;
            
            if (!edgesStack.empty()) {
                if(componentsConectors[parrent.id] == null){
                    componentsConectors[parrent.id] = new ArrayList<int[]>();
                }
                int value = edgesStack.pop();
                componentsConectors[parrent.id].add(new int[]{componentCounter, value});
                
                if(value + node.minPath < parrent.minPath || parrent.minPath==0){         //for Alice's path searching
                    parrent.minPath = value + node.minPath;
                }
                
            }
            c.id = componentCounter;
            components.put(componentCounter, c);
            componentCounter++;
        }
    }

    /**
     * creating the graph
     */
    static void createGraph() {
        //Creating huts it's maybe a bit overkill (if it's gonna be slow improve this thing)
        huts = new Node[numberOfHuts];
        for (int i = 0; i < huts.length; i++) {
            Node n = new Node();
            n.id = i;
            huts[i] = n;
        }
       

        for (int i = 0; i < numberOfHuts; i++) {
            int j;
            if ((i - k) < 0) {
                j = 0;
            } else {
                j = (i - k);
            }
            while (j <= k + i && j < numberOfHuts) {
                
                if (j != i) {
                    long id = i * ((2 * k) + 1) + (j - i + k - 1);
                    long preliminaryPrice = (p1*id) % p2;
                    if (preliminaryPrice >= p3 && preliminaryPrice <= p4) {
                        huts[i].edgeNodes.add(huts[j]);
                        huts[i].edgeValues.add((int) preliminaryPrice);
                    }
                }
                j++;
            }
        }

        //graph making test print
        /*for (Node n : huts){
         System.out.println(">>>"+ n.getId()+"n");
            
         for (Node print : n.getEdges().keySet()) {
         System.out.println("to: " + print.getId() + ", for: " + n.getEdges().get(print));
                
         }
            
         }*/

    }

    /**
     * just reading of the data from the system
     */
    static void readFromStdInFast() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        numberOfHuts = Integer.parseInt(st.nextToken());
        p1 = Long.parseLong(st.nextToken());
        p2 = Integer.parseInt(st.nextToken());
        p3 = Integer.parseInt(st.nextToken());
        p4 = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
    }
}
