package rph.labyrinth.players;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import rph.labyrinth.Coordinate;
import rph.labyrinth.Path;
import rph.labyrinth.Player;
import rph.labyrinth.Labyrinth;

/*
 * Player that is using A* algorithm with Manhattan distance as a heuristic.
 * 
 * I was also trying something like keep on the longest passage, but it was
 * not good idea.
 * 
 * Player was implemented in this way because of the necessity to have implemented it
 * as one file (class) MyPlayer.
 */
public class MyPlayer extends Player {

    /*
     * coordinates of goal and start point
     */
    Coordinate start = null;
    Coordinate goal = null;
    /*
     * global width and height
     */
    int width;
    int height;
    /*
     * tree depth
     */
    int depth = 0;
    /*
     * last opened Node
     */
    Node actualNode;
    /*
     * sorry just need to have map as global variable (but it should be just the shallow copy)
     */
    public int[][] map;
    /*
     * list of the routing
     */
    List<Coordinate> routeList;
    /*
     * Priority Queue of all the opened Nodes
     */
    Queue<Node> opendList;

    @Override
    protected String getName() {
        return "MyPlayer";
    }

    @Override
    protected Path findPath(int[][] map) {

        /*
         * priority queue implementation
         */
        opendList = new PriorityQueue<Node>();

        /*
         * route list implementation
         */
        routeList = new ArrayList<Coordinate>();
        /*
         * just for the normal oop approach
         */
        this.map = map;

        /*
         * saving the memory (actualy I think that jvm deals with this, but just
         * to be sure)
         */
        map = null;

        /*
         * ehm RPH relict I like it more to work with this
         */
        width = this.map.length;
        height = this.map[0].length;

        /*
         * searching for the important fields
         */
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                /*
                 * searching for the start
                 */
                if (this.map[x][y] == Labyrinth.START_SQUARE) {
                    start = new Coordinate(x, y);
                }

                /*
                 * searching for the goal
                 */
                if (this.map[x][y] == Labyrinth.GOAL_SQUARE) {
                    goal = new Coordinate(x, y);
                }
            }
        }

        /*
         * part with basic logic of the A* algorithm
         */
        ////////////////////////////////////////////////////////////////////////
        actualNode = new Node(start, depth);
        this.map[actualNode.coordinate.getX()][actualNode.coordinate.getY()] = -3;

        /*
         * indefinite loop here shoul be some timer despite slides, but I
         * know that it will have the solution.
         */
        while (true) {

            /*
             * unpack all childerns
             */
            actualNode.calculateColocation();

            /*
             * retrive element with the highest priority
             */
            actualNode = opendList.poll();

            /*
             * tracking the path, bud I dont want to polute goal squere
             */
            if (this.map[actualNode.coordinate.getX()][actualNode.coordinate.getY()] != Labyrinth.GOAL_SQUARE) {
                this.map[actualNode.coordinate.getX()][actualNode.coordinate.getY()] = -3;

            } /*
             * end of the algorithm when the end is found
             */ else {
                break;
            }


        }
        ////////////////////////////////////////////////////////////////////////
        /*
         * end of the part with basic logic
         */

        /*
         * building the path
         */
        Path path = new Path();
        this.makeRoute(actualNode);
        /*
         * wierd loop because of need of the reversal order of the road
         */
        for (int i = routeList.size() - 1; i >= 0; i--) {
            //System.out.println(routeList.get(i).getX()+"  "+routeList.get(i).getY());
            path.addCoordinate(routeList.get(i));
        }

        /*
         * nulling before the next run of the algorithm
         */
        opendList = null;
        map = null;
        return path;
    }

    /*
     * method for making the road from the last Node
     */
    private void makeRoute(Node n) {

        /*
         * loading parrental node in the cycle (because I have low memory for recursion)
         */
        while (n.parrent != null) {
            routeList.add(n.coordinate);
            n = n.parrent;
        }
        /*
         * just for the case that the start is poluted,
         * but based on the agorithm I am sure that this node must be the start
         */
        this.map[n.coordinate.getX()][n.coordinate.getY()] = Labyrinth.START_SQUARE;
        routeList.add(n.coordinate);

    }

    /*
     * class for building the tree I know that it is not nice to use private classes,
     * but specification is give us implemented MyPlayer class, so here it is.
     */
    private class Node implements Comparable<Node> {

        public Node parrent;
        public int price;
        public int depth;
        public Coordinate coordinate;

        /*
         * just the constructor
         */
        public Node(Coordinate coordinate, int depth) {
            this.coordinate = coordinate;
            this.depth = depth;
            this.price = this.getPrice();
        }

        /*
         * method for calculation the price 
         */
        public int getPrice() {
            return this.manhattanDistance() + depth;
        }

        /*
         * calculating if the fields around are available
         */
        public void calculateColocation() {
            Coordinate top = new Coordinate(coordinate.getX(), coordinate.getY() - 1);
            Coordinate down = new Coordinate(coordinate.getX(), coordinate.getY() + 1);
            Coordinate left = new Coordinate(coordinate.getX() - 1, coordinate.getY());
            Coordinate right = new Coordinate(coordinate.getX() + 1, coordinate.getY());

            /*
             * increasing the depth befor making childerns
             */
            depth++;

            /*
             * making of the down childern
             */
            if (down.getY() < MyPlayer.this.height && MyPlayer.this.map[down.getX()][down.getY()] != Labyrinth.WALL_SQUARE) {
                Node childernNode = new Node(down, depth);

                /*
                 * protection from looping
                 */
                if (MyPlayer.this.map[down.getX()][down.getY()] != -3) {
                    childernNode.parrent = this;
                    MyPlayer.this.opendList.add(childernNode);
                }
            }

            if (right.getX() < MyPlayer.this.width && MyPlayer.this.map[right.getX()][right.getY()] != Labyrinth.WALL_SQUARE) {
                Node childernNode = new Node(right, depth);
                if (MyPlayer.this.map[right.getX()][right.getY()] != -3) {
                    childernNode.parrent = this;
                    MyPlayer.this.opendList.add(childernNode);
                }
            }
            if (top.getY() >= 0 && MyPlayer.this.map[top.getX()][top.getY()] != Labyrinth.WALL_SQUARE) {
                Node childernNode = new Node(top, depth);
                if (MyPlayer.this.map[top.getX()][top.getY()] != -3) {
                    childernNode.parrent = this;
                    MyPlayer.this.opendList.add(childernNode);
                }

            }
            if (left.getX() >= 0 && MyPlayer.this.map[left.getX()][left.getY()] != Labyrinth.WALL_SQUARE) {
                Node childernNode = new Node(left, depth);
                if (MyPlayer.this.map[left.getX()][left.getY()] != -3) {
                    childernNode.parrent = this;
                    MyPlayer.this.opendList.add(childernNode);
                }
            }
        }

        /*
         *  manhattan distance heuristic calculation 
         */
        private int manhattanDistance() {

            int x = this.coordinate.getX() - goal.getX();
            /*
             * calculating the absolute value
             */
            if (x < 0) {
                x = -x;
            }

            int y = this.coordinate.getY() - goal.getY();
            /*
             * calculating the absolute value
             */
            if (y < 0) {
                y = -y;
            }
            return x + y;
        }

        /*
         * because of priority queue
         */
        public int compareTo(Node o) {
            if (this.price <= o.price) {
                return -1;
            } else {
                return 1;
            }
        }

        /*
         * because of comparsion above
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Node other = (Node) obj;
            if (this.coordinate != other.coordinate && (this.coordinate == null || !this.coordinate.equals(other.coordinate))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 61 * hash + (this.coordinate != null ? this.coordinate.hashCode() : 0);
            return hash;
        }
    }
}