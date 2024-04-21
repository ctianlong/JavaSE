package util.algorithm.lottery.util;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author ctl
 * @createTime 2023/12/07 15:52
 * @description
 */
public class LotteryUtils {


    /**
     * 抽奖工具，根据prizeTool奖池中各项奖品Prize定义的概率抽奖（此概率本质上为奖池中各项奖品间的相对概率，例如各项奖品的概率之和为100，则该概率可视为百分比概率）
     */
    public static <T extends BasePrize> T shake(List<T> prizeTool) {
        double total = prizeTool.stream().mapToDouble(BasePrize::getProbability).sum();
        double random = ThreadLocalRandom.current().nextDouble(total);
        double sumProb = 0;
        T result = null;
        for (T prize : prizeTool) {
            sumProb += prize.getProbability();
            if (random < sumProb) {
                result = prize;
                break;
            }
        }
        return result;
    }

}
