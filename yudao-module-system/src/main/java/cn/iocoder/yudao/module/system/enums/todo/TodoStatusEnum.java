package cn.iocoder.yudao.module.system.enums.todo;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 个人待办状态枚举
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum TodoStatusEnum implements ArrayValuable<Integer> {

    PENDING(0, "待处理"),
    PROCESSING(1, "进行中"),
    FINISHED(2, "已完成");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(TodoStatusEnum::getStatus).toArray(Integer[]::new);

    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static boolean isFinished(Integer status) {
        return ObjUtil.equal(FINISHED.status, status);
    }

}