package util.algorithm.lottery.util;

/**
 * @author ctl
 * @createTime 2023/12/07 13:46
 * @description
 */
public interface BasePrize {

    /**
     * 该项奖品的中奖概率（奖池中的各个奖品的相对概率）
     */
    double getProbability();

}
