package Index;

public class Pair<L,R> {
    private long l;
    private double r;
    public Pair(long wordInt, double tf_idf){
        this.l = wordInt;
        this.r = tf_idf;
    }

	public long getL(){ return l; }
    public double getR(){ return r; }
    public void setL(L l){ this.l = (long) l; }
    public void setR(R r){ this.r = (double) r; }
}