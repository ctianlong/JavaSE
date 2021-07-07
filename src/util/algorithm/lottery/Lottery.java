package util.algorithm.lottery;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.mutable.MutableLong;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author ctl
 * @date 2021/5/31
 */
public class Lottery {

	public static void main(String[] args) {
		List<Prize> prizeTool = Lists.newArrayList();
		prizeTool.add(new Prize(1L, 1));
		prizeTool.add(new Prize(2L, 3));
		prizeTool.add(new Prize(3L, 4));
		prizeTool.add(new Prize(4L, 8));
		prizeTool.add(new Prize(5L, 8));
		prizeTool.add(new Prize(6L, 17));
		prizeTool.add(new Prize(7L, 29));
		prizeTool.add(new Prize(8L, 30));
		int total = prizeTool.stream().mapToInt(Prize::getProbability).sum();
		Map<Long, Double> truth = Maps.newTreeMap();
		prizeTool.forEach(p -> {
			truth.put(p.getId(), (double) p.getProbability() / (double) total);
		});
		System.out.println(truth);
		Map<Long, MutableLong> countMap = Maps.newHashMap();
		int times = 100000;
		// 预热
		for (int i = 0; i < times; i++) {
			shake(prizeTool, total);
		}
		for (int i = 0; i < times; i++) {
			Prize shake = shake(prizeTool, total);
			countMap.computeIfAbsent(shake.getId(), k -> new MutableLong(0)).add(1);
		}
		Map<Long, Double> result = Maps.newTreeMap();
		countMap.forEach((k, v) -> {
			result.put(k, v.doubleValue() / times);
		});
		System.out.println(result);
	}

	public static Prize shake(List<Prize> prizeTool, int total) {
		int random = ThreadLocalRandom.current().nextInt(total);
//		int random = new Random().nextInt(total);
		int sumProb = 0;
		Prize result = null;
		for (Prize prize : prizeTool) {
			sumProb += prize.getProbability();
			if (random < sumProb) {
				result = prize;
				break;
			}
		}
		return result;
	}

}
