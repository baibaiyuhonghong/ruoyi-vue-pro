package cn.iocoder.yudao.module.system.controller.admin.todo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 个人待办 Response VO")
@Data
public class TodoRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "所属用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long userId;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "提交本周周报")
    private String title;

    @Schema(description = "内容", example = "汇总本周进展与下周计划")
    private String content;

    @Schema(description = "优先级，参见 system_todo_priority 字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer priority;

    @Schema(description = "状态，参见 system_todo_status 字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "截止时间")
    private LocalDateTime deadline;

    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

    @Schema(description = "备注", example = "周五下班前发给主管")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}