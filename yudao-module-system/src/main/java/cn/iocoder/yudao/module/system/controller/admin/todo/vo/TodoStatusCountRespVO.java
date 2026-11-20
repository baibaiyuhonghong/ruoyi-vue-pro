package cn.iocoder.yudao.module.system.controller.admin.todo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 个人待办按状态统计 Response VO")
@Data
public class TodoStatusCountRespVO {

    @Schema(description = "待处理数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    private Long pendingCount;

    @Schema(description = "进行中数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Long processingCount;

    @Schema(description = "已完成数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private Long finishedCount;

    @Schema(description = "总数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Long totalCount;

}