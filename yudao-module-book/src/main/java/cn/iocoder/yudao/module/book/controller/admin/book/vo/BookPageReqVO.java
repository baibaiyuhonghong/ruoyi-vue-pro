package cn.iocoder.yudao.module.book.controller.admin.book.vo;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "管理后台 - 图书分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BookPageReqVO extends PageParam {

    @Schema(description = "书名，模糊匹配", example = "红楼梦")
    private String name;

    @Schema(description = "作者，模糊匹配", example = "曹雪芹")
    private String author;

    @Schema(description = "ISBN", example = "9787020002207")
    private String isbn;

    @Schema(description = "出版社，模糊匹配", example = "人民文学出版社")
    private String publisher;

    @Schema(description = "出版日期", example = "2008-07-01")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate publishDate;

    @Schema(description = "状态（0 上架，1 下架）", example = "0")
    @InEnum(CommonStatusEnum.class)
    private Integer status;

}
