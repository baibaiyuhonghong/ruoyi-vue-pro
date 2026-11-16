package cn.iocoder.yudao.module.book.controller.admin.book.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 图书创建/修改 Request VO")
@Data
public class BookSaveReqVO {

    @Schema(description = "图书编号")
    private Long id;

    @Schema(description = "书名")
    @NotBlank(message = "书名不能为空")
    @Size(max = 200)
    private String name;

    @Schema(description = "作者")
    @NotBlank(message = "作者不能为空")
    @Size(max = 100)
    private String author;

    @Schema(description = "ISBN")
    @NotBlank(message = "ISBN 不能为空")
    @Size(max = 32)
    private String isbn;

    @Schema(description = "出版社")
    @NotBlank(message = "出版社不能为空")
    @Size(max = 200)
    private String publisher;

    @Schema(description = "出版日期")
    @NotNull(message = "出版日期不能为空")
    private LocalDate publishDate;

    @Schema(description = "价格，单位：分")
    @NotNull(message = "价格不能为空")
    @Min(value = 0, message = "价格不能为负数")
    private Integer price;

    @Schema(description = "库存")
    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    private Integer stock;

    @Schema(description = "状态：0 上架，1 下架")
    @NotNull(message = "状态不能为空")
    @Min(0)
    @Max(1)
    private Integer status;
}
