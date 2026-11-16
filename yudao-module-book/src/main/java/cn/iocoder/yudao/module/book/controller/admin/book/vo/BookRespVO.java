package cn.iocoder.yudao.module.book.controller.admin.book.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;

@Schema(description = "管理后台 - 图书 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BookRespVO {

    @Schema(description = "图书编号")
    @ExcelProperty("图书编号")
    private Long id;

    @Schema(description = "书名")
    @ExcelProperty("书名")
    private String name;

    @Schema(description = "作者")
    @ExcelProperty("作者")
    private String author;

    @Schema(description = "ISBN")
    @ExcelProperty("ISBN")
    private String isbn;

    @Schema(description = "出版社")
    @ExcelProperty("出版社")
    private String publisher;

    @Schema(description = "出版日期")
    @ExcelProperty("出版日期")
    private LocalDate publishDate;

    @Schema(description = "价格，单位：分")
    @ExcelProperty("价格，单位：分")
    private Integer price;

    @Schema(description = "库存")
    @ExcelProperty("库存")
    private Integer stock;

    @Schema(description = "状态：0 上架，1 下架")
    @ExcelProperty("状态：0 上架，1 下架")
    private Integer status;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
