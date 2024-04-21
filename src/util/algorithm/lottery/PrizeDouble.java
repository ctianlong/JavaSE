package util.algorithm.lottery;

/**
 * @author ctl
 * @createTime 2023/12/06 19:14
 * @description
 */
public class PrizeDouble {

    private long id;
    /**
     * 概率，0-100之间
     */
    private double probability;

    public PrizeDouble() {
    }

    public PrizeDouble(long id, double probability) {
        this.id = id;
        this.probability = probability;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
