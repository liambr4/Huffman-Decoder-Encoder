public class Pair implements Comparable<Pair> {
	private char value;
	private double prob;
	public Pair() {} //empty constructor
	public Pair(char v, double p) { //constructor with parameters
		this.value = v;
		this.prob = p;
	}
	public char getValue() { //getters & setters
		return this.value;
	}
	public double getProb() {
		return this.prob;
	}
	public void setValue(char v) {
		value = v;
	}
	public void setProb(double p) {
		prob = p;
	}
	public String toString() { //to string
		return value + " " + prob + " " ;
	}
	public int compareTo(Pair p) { //override of compareto
		return Double.compare(this.getProb(), p.getProb());
	}
}
