package util.algorithm.lottery;

/**
 * @author ctl
 * @date 2021/5/31
 */
public class Prize {

	private Long id;
	/**
	 * 概率，0-100之间
	 */
	private Integer probability;

	public Prize() {
	}

	public Prize(Long id, Integer probability) {
		this.id = id;
		this.probability = probability;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getProbability() {
		return probability;
	}

	public void setProbability(Integer probability) {
		this.probability = probability;
	}

	@Override
	public String toString() {
		return "Prize{" +
				"id=" + id +
				", probability=" + probability +
				'}';
	}


}
