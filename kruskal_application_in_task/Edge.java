package pal;

/**
 *
 * @author zikesjan
 */
public class Edge implements Comparable<Edge> {
 
    private double actualValue;
    private double price;
    private double discounedPrice;
    private int p1;
    private int p2;
    private int sortedIndex;

    public Edge(double actualValue ,double price, double discountedPrice,int p1, int p2) {
        this.actualValue = actualValue;
        this.price = price;
        this.discounedPrice = discountedPrice;
        this.p1 = p1;
        this.p2 = p2;
    }

    public double getActualValue() {
        return actualValue;
    }

    public void setActualValue(double actualValue) {
        this.actualValue = actualValue;
    }
    
    public double getDiscounedPrice() {
        return discounedPrice;
    }

    public void setDiscounedPrice(double discounedPrice) {
        this.discounedPrice = discounedPrice;
    }
    
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getP1() {
        return p1;
    }

    public void setP1(int p1) {
        this.p1 = p1;
    }

    public int getP2() {
        return p2;
    }

    public void setP2(int p2) {
        this.p2 = p2;
    }

    public int getSortedIndex() {
        return sortedIndex;
    }

    public void setSortedIndex(int sortedIndex) {
        this.sortedIndex = sortedIndex;
    }

    
    
    @Override
    public int compareTo(Edge t) {
        if (actualValue >= t.actualValue){
            return 1;
        }else{
            return -1;
        }
    }
    
    
    
}
