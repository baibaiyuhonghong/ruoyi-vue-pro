package cn.iocoder.yudao.module.system.controller.admin.navlink.vo;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "管理后台 - 常用网址导航创建/修改 Request VO")
@Data
public class NavLinkSaveReqVO {

    @Schema(description = "编号", example = "1024")
    private Long id;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道源码")
    @NotBlank(message = "名称不能为空")
    @Size(max = 50, message = "名称长度不能超过 50 个字符")
    private String name;

    @Schema(description = "网址", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotBlank(message = "网址不能为空")
    @Size(max = 500, message = "网址长度不能超过 500 个字符")
    @URL(message = "网址格式不正确")
    private String url;

    @Schema(description = "分类，参见 system_nav_link_category 字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "分类不能为空")
    private Integer category;

    @Schema(description = "图标", example = "ep:link")
    @Size(max = 100, message = "图标长度不能超过 100 个字符")
    private String icon;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "状态，参见 CommonStatusEnum 枚举类", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "状态不能为空")
    @InEnum(CommonStatusEnum.class)
    private Integer status;

    @Schema(description = "备注", example = "常用的开发文档站点")
    @Size(max = 500, message = "备注长度不能超过 500 个字符")
    private String remark;

}
