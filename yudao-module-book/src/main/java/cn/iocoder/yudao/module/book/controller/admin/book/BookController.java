package cn.iocoder.yudao.module.book.controller.admin.book;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.book.controller.admin.book.vo.*;
import cn.iocoder.yudao.module.book.dal.dataobject.book.BookDO;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.book.service.book.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "管理后台 - 图书管理")
@RestController
@RequestMapping("/book/book")
@Validated
public class BookController {
    @Resource
    private BookService bookService;

    @PostMapping("/create")
    @Operation(summary = "创建图书")
    @PreAuthorize("@ss.hasPermission('book:book:create')")
    public CommonResult<Long> createBook(@Valid @RequestBody BookSaveReqVO createReqVO) {
        return success(bookService.createBook(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改图书")
    @PreAuthorize("@ss.hasPermission('book:book:update')")
    public CommonResult<Boolean> updateBook(@Valid @RequestBody BookSaveReqVO updateReqVO) {
        bookService.updateBook(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除图书")
    @Parameter(name = "id", description = "图书编号", required = true)
    @PreAuthorize("@ss.hasPermission('book:book:delete')")
    public CommonResult<Boolean> deleteBook(@RequestParam("id") Long id) {
        bookService.deleteBook(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得图书")
    @Parameter(name = "id", description = "图书编号", required = true)
    @PreAuthorize("@ss.hasPermission('book:book:query')")
    public CommonResult<BookRespVO> getBook(@RequestParam("id") Long id) {
        return success(BeanUtils.toBean(bookService.getBook(id), BookRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得图书分页")
    @PreAuthorize("@ss.hasPermission('book:book:query')")
    public CommonResult<PageResult<BookRespVO>> getBookPage(@Valid BookPageReqVO pageReqVO) {
        return success(BeanUtils.toBean(bookService.getBookPage(pageReqVO), BookRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出图书 Excel")
    @PreAuthorize("@ss.hasPermission('book:book:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportBookExcel(@Valid BookPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BookDO> list = bookService.getBookPage(pageReqVO).getList();
        ExcelUtils.write(response, "图书.xls", "数据", BookRespVO.class,
                BeanUtils.toBean(list, BookRespVO.class));
    }
}
