package Index;

public class Pair<L,R> {
    private L l;
    private R r;
    public Pair(L wordInt, R tf_idf){
        this.l = wordInt;
        this.r = tf_idf;
    }

	public L getL(){ return l; }
    public R getR(){ return r; }
    public void setL(L l){ this.l = (L) l; }
    public void setR(R r){ this.r = (R) r; }
}