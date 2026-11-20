package cn.iocoder.yudao.module.system.dal.dataobject.todo;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import cn.iocoder.yudao.module.system.enums.todo.TodoPriorityEnum;
import cn.iocoder.yudao.module.system.enums.todo.TodoStatusEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 个人待办 DO
 *
 * @author 芋道源码
 */
@TableName("system_todo")
@KeySequence("system_todo_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
public class TodoDO extends TenantBaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 所属用户编号
     *
     * 关联 system_users 的 id 字段，创建时取当前登录用户，所有读写都以它为归属边界
     */
    private Long userId;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 优先级
     *
     * 枚举 {@link TodoPriorityEnum}
     * 对应字典 {@link DictTypeConstants#TODO_PRIORITY}
     */
    private Integer priority;
    /**
     * 状态
     *
     * 枚举 {@link TodoStatusEnum}
     * 对应字典 {@link DictTypeConstants#TODO_STATUS}
     */
    private Integer status;
    /**
     * 截止时间
     */
    private LocalDateTime deadline;
    /**
     * 完成时间
     *
     * 由服务端按状态推导：置为已完成时写入，改回未完成时清空，不接受前端传值
     */
    private LocalDateTime finishTime;
    /**
     * 备注
     */
    private String remark;

}