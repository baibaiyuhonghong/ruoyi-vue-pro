package cn.iocoder.yudao.module.system.controller.admin.navlink.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 常用网址导航分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class NavLinkPageReqVO extends PageParam {

    @Schema(description = "名称，模糊匹配", example = "芋道")
    private String name;

    @Schema(description = "分类，参见 system_nav_link_category 字典", example = "1")
    private Integer category;

    @Schema(description = "状态，参见 CommonStatusEnum 枚举类", example = "0")
    private Integer status;

}
