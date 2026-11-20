package cn.iocoder.yudao.module.system.controller.admin.todo.vo;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.system.enums.todo.TodoPriorityEnum;
import cn.iocoder.yudao.module.system.enums.todo.TodoStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 个人待办创建/修改 Request VO")
@Data
public class TodoSaveReqVO {

    @Schema(description = "编号", example = "1024")
    private Long id;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "提交本周周报")
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题长度不能超过 100 个字符")
    private String title;

    @Schema(description = "内容", example = "汇总本周进展与下周计划")
    @Size(max = 1000, message = "内容长度不能超过 1000 个字符")
    private String content;

    @Schema(description = "优先级，参见 system_todo_priority 字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "优先级不能为空")
    @InEnum(TodoPriorityEnum.class)
    private Integer priority;

    @Schema(description = "状态，参见 system_todo_status 字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "状态不能为空")
    @InEnum(TodoStatusEnum.class)
    private Integer status;

    @Schema(description = "截止时间")
    private LocalDateTime deadline;

    @Schema(description = "备注", example = "周五下班前发给主管")
    @Size(max = 500, message = "备注长度不能超过 500 个字符")
    private String remark;

}