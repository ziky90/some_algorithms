package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 *
 * @author zikesjan
 */
public class Main {

    private static String alphabetString;
    private static char[] alphabet;
    private static String a;
    private static String b;
    private static int n;
    private static AbstractNode top1;
    private static AbstractNode top2;
    private static StatesStorage statesStorage = StatesStorage.getInstance();
    private static HashMap<String, DoubleState> finalAutoma = new HashMap<String, DoubleState>();
    private static DoubleState startDouble;
    private static DFAState start;
    private static HashMap<HashSet<DoubleState>, DFAState> DFAStates = new HashMap<HashSet<DoubleState>, DFAState>();
    private static int dfaNodeId = 0;
    
    
    public static void main(String[] args) {
        try {
            readFromStdInFast();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        preprocessAlphabet(alphabetString);
        parseRegEx(a);
        statesStorage.switchAutoma();
        parseRegEx(".*(" + b + ").*");

        cartesianProduct();

        resultToDFA();

        System.out.println(resultFromDFA());
        
    }

    static long resultFromDFA() {
        
        long[][] rows = new long[DFAStates.size()][n+1];
        rows[0][0] = 0;
        HashSet<DFAState> usableStates;
        
        HashSet<DFAState> nextLevelStates = new HashSet<DFAState>();
        nextLevelStates.add(start); 
        
        for (int i = 1; i <= n; i++) {
            usableStates = nextLevelStates;
            nextLevelStates = new HashSet<DFAState>();
            for(DFAState dfas : usableStates){
                for(DFAState dfas2 : dfas.edges.values()){
                    if(rows[dfas.id][i-1] == 0){
                        rows[dfas2.id][i]++;
                    }else{
                        rows[dfas2.id][i] = rows[dfas2.id][i] + rows[dfas.id][i-1];
                    }
                    if(!nextLevelStates.contains(dfas2)){
                        nextLevelStates.add(dfas2);
                    }
                }
            }
            
        }
        
        long value = 0;
        for(DFAState ds : DFAStates.values()){
            if(ds.end){
                value = value + rows[ds.id][n];
            }
        }
        
        return value;
    }


    static void resultToDFA() {

        start = new DFAState(dfaNodeId);
        dfaNodeId++;
        start.parrents.add(startDouble);
        start.start = true;
        HashSet<DoubleState> doubleNodes = new HashSet<DoubleState>();
        doubleNodes.add(startDouble);
        DFAStates.put(doubleNodes, start);
        Stack<DFAState> statesToDo = new Stack<DFAState>();
        statesToDo.push(start);
        
        while (!statesToDo.isEmpty()) {
            DFAState dfs = statesToDo.pop();
            for (DoubleState ds : dfs.parrents) {   
                if (ds.end) {
                    dfs.end = true;
                }
            }


            for (char ch : alphabet) {


                
                HashSet<DoubleState> members = new HashSet<DoubleState>();

                for (DoubleState ds : dfs.parrents) { 

                    if (ds.edges.containsKey(ch)) {




                        for (DoubleState ds2 : ds.edges.get(ch)) {
                            if (!members.contains(ds2)) {
                                members.add(ds2);
                            }
                        }





                    } 
                }
                if (!members.isEmpty()) {
                    if (DFAStates.containsKey(members)) {        
                        dfs.edges.put(ch, DFAStates.get(members));
                    } else {
                        DFAState addedState = new DFAState(dfaNodeId);
                        dfaNodeId++;
                        dfs.edges.put(ch, addedState);
                        addedState.parrents.addAll(members);
                        statesToDo.push(addedState);
                        DFAStates.put(members, addedState);
                    }
                }
            }
        }
    }

    static void cartesianProduct() {

        for (State s : statesStorage.getAutoma1()) {
            for (State s2 : statesStorage.getAutoma2()) {
                DoubleState ds = new DoubleState(s, s2, s.id, s2.id);
                //dealing with start
                if (s.start && s2.start) {
                    ds.start = true;
                    startDouble = ds;
                }
                //dealing with end
                if (s.end && s2.end) {
                    ds.end = true;
                }

                finalAutoma.put(ds.id1 + "x" + ds.id2, ds);
            }
        }

        for (DoubleState ds : finalAutoma.values()) {
            for (char ch : alphabet) {
                if (ds.xState.edges.containsKey(ch)) {
                    if (ds.yState.edges.containsKey(ch)) {

                        for (State s : ds.xState.edges.get(ch)) {
                            for (State s2 : ds.yState.edges.get(ch)) {
                                ds.addEdge(ch, finalAutoma.get(s.id + "x" + s2.id));
                            }
                        }
                    }
                }
            }
        }

    }

    static void parseRegEx(String expression) {
        StringBuilder sb = new StringBuilder(expression);
        for (int i = expression.length() - 2; i >= 0; i--) {
            if(sb.charAt(i) != '(' && sb.charAt(i) != '|' && sb.charAt(i + 1) != ')' && sb.charAt(i + 1) != '*' && sb.charAt(i + 1) != '|') {
                sb.insert(i + 1, '‡');
                
            }
            
            if(i == expression.length() - 2 && sb.charAt(i+1) == '|'){
                sb.insert(i+2, 'ˇ');
                sb.insert(i+1, "ˇ");
            }
            
            if ((i == 0 && sb.charAt(i) == '|') || (sb.charAt(i) == '(' && sb.charAt(i+1) == ')') || (sb.charAt(i) == '|' && sb.charAt(i+1) == '|') || (sb.charAt(i) == '(' && sb.charAt(i+1) == '|') || (sb.charAt(i) == '|' && sb.charAt(i+1) == ')') || (sb.charAt(i) == ')' && sb.charAt(i+1) == '|') || (sb.charAt(i) == '|' && sb.charAt(i+1) == '*') || (sb.charAt(i) == '(' && sb.charAt(i+1) == '*')){
                if(i == 0 && sb.charAt(i) == '|'){
                    sb.insert(i + 1, 'ˇ');
                    sb.insert(i, 'ˇ');
                }else if(sb.charAt(i) == ')' && sb.charAt(i+1) == '|'){
                    sb.insert(i + 1, "‡ˇ");
                }else if((sb.charAt(i) == '|' && sb.charAt(i+1) == '*') || (sb.charAt(i) == '(' && sb.charAt(i+1) == '*')){
                    sb.insert(i+1, "ˇ");
                }else{
                    sb.insert(i + 1, 'ˇ');
                    
                }
                
            }
            
            if(sb.charAt(i) == '|' && sb.charAt(i+1) == '|'){
                sb.deleteCharAt(i+1);
            }
            
            
            
            
        }
        if(sb.charAt(0) == '*'){
            sb.insert(0, 'ˇ');
        }
        expression = sb.toString();

        Stack<Character> signStack = new Stack<Character>();
        Stack<AbstractNode> leafStack = new Stack<AbstractNode>();
        Stack<Integer> priorities = new Stack<Integer>();
        char ch;
        int priority = 0;
        for (int i = 0; i < expression.length(); i++) {
            ch = expression.charAt(i);
            if (ch == '|') {    //dealing with unions
                if (priority < 1) {
                    signStack.push(ch);
                } else {
                    char ch2 = signStack.peek();
                    while (ch2 == '*' || ch2 == '‡' || ch2 == '|') {
                        signStack.pop();
                        if (ch2 == '*') {
                            Star star = new Star(leafStack.pop());
                            leafStack.push(star);
                        } else if (ch2 == '‡') {
                            Concat concat = new Concat(leafStack.pop(), leafStack.pop());
                            leafStack.push(concat);
                        } else {
                            Union union = new Union(leafStack.pop(), leafStack.pop());
                            leafStack.push(union);
                        }
                        if (!signStack.isEmpty()) {
                            ch2 = signStack.peek();
                        } else {
                            break;
                        }
                    }
                    signStack.push(ch);
                }
                priority = 1;
            } else if (ch == '‡') {   //dealing with concats
                if (priority < 2) {
                    signStack.push(ch);
                } else {
                    char ch2 = signStack.peek();
                    while (ch2 == '*' || ch2 == '‡') {
                        signStack.pop();
                        if (ch2 == '*') {
                            Star star = new Star(leafStack.pop());
                            leafStack.push(star);
                        } else {
                            Concat concat = new Concat(leafStack.pop(), leafStack.pop());
                            leafStack.push(concat);
                        }
                        if (!signStack.isEmpty()) {
                            ch2 = signStack.peek();
                        } else {
                            break;
                        }
                    }
                    signStack.push(ch);
                }
                priority = 2;
            } else if (ch == '*') {    //dealing with star
                signStack.push(ch);
                priority = 3;
            } else if (ch == '.') {
                Leaf l = new Leaf(alphabet);
                leafStack.push(l);
            } else if (ch == '(') {
                signStack.push(ch);
                priorities.push(priority);
                priority = 0;
            } else if (ch == ')') {
                char ch2 = signStack.pop();
                while (ch2 != '(') {
                    if (ch2 == '*') {
                        Star star = new Star(leafStack.pop());
                        leafStack.push(star);
                    } else if (ch2 == '‡') {
                        Concat concat = new Concat(leafStack.pop(), leafStack.pop());
                        leafStack.push(concat);
                    } else {
                        Union union = new Union(leafStack.pop(), leafStack.pop());
                        leafStack.push(union);
                    }
                    ch2 = signStack.pop();
                }
                priority = priorities.pop();
            } else {
                if(ch == 'ˇ'){
                    EmpetyLeaf el = new EmpetyLeaf();
                    leafStack.push(el);
                }else{
                    Leaf l = new Leaf(new char[]{ch});
                    leafStack.push(l);
                }
            }
        }
        while (!signStack.isEmpty()) {
            char ch2 = signStack.pop();
            if (ch2 == '*') {
                Star star = new Star(leafStack.pop());
                leafStack.push(star);
            } else if (ch2 == '‡') {
                Concat concat = new Concat(leafStack.pop(), leafStack.pop());
                leafStack.push(concat);
            } else {
                Union union = new Union(leafStack.pop(), leafStack.pop());
                leafStack.push(union);
            }
        }
        if (top1 == null) {
            top1 = leafStack.peek();
            top1.getBeginning().start = true;
            for (State s : top1.getEnd()) {
                s.end = true;
            }
        } else {
            top2 = leafStack.peek();
            top2.getBeginning().start = true;
            for (State s : top2.getEnd()) {
                s.end = true;
            }
        }
    }

    static void preprocessAlphabet(String alphabetString) {
        alphabet = new char[alphabetString.length()];
        for (int i = 0; i < alphabetString.length(); i++) {
            alphabet[i] = alphabetString.charAt(i);
        }
    }

    /**
     * just reading of the data from the system
     */
    static void readFromStdInFast() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        alphabetString = br.readLine();
        a = br.readLine();
        b = br.readLine();
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
    }
}
