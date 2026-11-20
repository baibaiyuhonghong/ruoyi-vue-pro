package cn.iocoder.yudao.module.system.controller.admin.todo.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 个人待办分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class TodoPageReqVO extends PageParam {

    @Schema(description = "标题，模糊匹配", example = "周报")
    private String title;

    @Schema(description = "状态，参见 system_todo_status 字典", example = "0")
    private Integer status;

    @Schema(description = "优先级，参见 system_todo_priority 字典", example = "1")
    private Integer priority;

}