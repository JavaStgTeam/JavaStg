package user.player;

/**
 * 自机类型枚举
 * 定义所有可用的自机类型
 */
public enum PlayerType {
	DEFAULT("默认自机", "默认的双发弹幕模式"),
	REIMU("博丽灵梦", "灵符-梦想封印，高火力广域攻击"),
	MARISA("雾雨魔理沙", "恋符-极限火花，集中火力高伤害");

	private final String name;
	private final String description;

	PlayerType(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return name;
	}
}

