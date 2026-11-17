package cn.iocoder.yudao.module.book.controller.admin.book.vo;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "管理后台 - 图书创建/修改 Request VO")
@Data
public class BookSaveReqVO {

    @Schema(description = "图书编号，修改时必填", example = "1024")
    private Long id;

    @Schema(description = "书名", requiredMode = Schema.RequiredMode.REQUIRED, example = "红楼梦")
    @NotBlank(message = "书名不能为空")
    @Size(max = 200, message = "书名不能超过 200 个字符")
    private String name;

    @Schema(description = "作者", requiredMode = Schema.RequiredMode.REQUIRED, example = "曹雪芹")
    @NotBlank(message = "作者不能为空")
    @Size(max = 100, message = "作者不能超过 100 个字符")
    private String author;

    @Schema(description = "ISBN", requiredMode = Schema.RequiredMode.REQUIRED, example = "9787020002207")
    @NotBlank(message = "ISBN 不能为空")
    @Size(max = 32, message = "ISBN 不能超过 32 个字符")
    private String isbn;

    @Schema(description = "出版社", requiredMode = Schema.RequiredMode.REQUIRED, example = "人民文学出版社")
    @NotBlank(message = "出版社不能为空")
    @Size(max = 200, message = "出版社不能超过 200 个字符")
    private String publisher;

    @Schema(description = "出版日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2008-07-01")
    @NotNull(message = "出版日期不能为空")
    private LocalDate publishDate;

    @Schema(description = "价格（分）", requiredMode = Schema.RequiredMode.REQUIRED, example = "5990")
    @NotNull(message = "价格不能为空")
    @PositiveOrZero(message = "价格不能小于 0")
    private Integer price;

    @Schema(description = "库存", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    @NotNull(message = "库存不能为空")
    @PositiveOrZero(message = "库存不能小于 0")
    private Integer stock;

    @Schema(description = "状态（0 上架，1 下架）", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "状态不能为空")
    @InEnum(CommonStatusEnum.class)
    private Integer status;

}
