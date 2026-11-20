package cn.iocoder.yudao.module.system.enums.todo;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 个人待办优先级枚举
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum TodoPriorityEnum implements ArrayValuable<Integer> {

    HIGH(1, "高"),
    MEDIUM(2, "中"),
    LOW(3, "低");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(TodoPriorityEnum::getPriority).toArray(Integer[]::new);

    /**
     * 优先级值
     */
    private final Integer priority;
    /**
     * 优先级名
     */
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}