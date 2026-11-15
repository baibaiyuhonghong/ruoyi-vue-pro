package cn.iocoder.yudao.module.system.controller.admin.navlink;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.system.controller.admin.navlink.vo.NavLinkPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.navlink.vo.NavLinkRespVO;
import cn.iocoder.yudao.module.system.controller.admin.navlink.vo.NavLinkSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.navlink.NavLinkDO;
import cn.iocoder.yudao.module.system.service.navlink.NavLinkService;
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

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 常用网址导航")
@RestController
@RequestMapping("/system/nav-link")
@Validated
public class NavLinkController {

    @Resource
    private NavLinkService navLinkService;

    @PostMapping("/create")
    @Operation(summary = "创建常用网址导航")
    @PreAuthorize("@ss.hasPermission('system:nav-link:create')")
    public CommonResult<Long> createNavLink(@Valid @RequestBody NavLinkSaveReqVO createReqVO) {
        return success(navLinkService.createNavLink(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改常用网址导航")
    @PreAuthorize("@ss.hasPermission('system:nav-link:update')")
    public CommonResult<Boolean> updateNavLink(@Valid @RequestBody NavLinkSaveReqVO updateReqVO) {
        navLinkService.updateNavLink(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除常用网址导航")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:nav-link:delete')")
    public CommonResult<Boolean> deleteNavLink(@RequestParam("id") Long id) {
        navLinkService.deleteNavLink(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "批量删除常用网址导航")
    @Parameter(name = "ids", description = "编号列表", required = true)
    @PreAuthorize("@ss.hasPermission('system:nav-link:delete')")
    public CommonResult<Boolean> deleteNavLinkList(@RequestParam("ids") List<Long> ids) {
        navLinkService.deleteNavLinkList(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得常用网址导航")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:nav-link:query')")
    public CommonResult<NavLinkRespVO> getNavLink(@RequestParam("id") Long id) {
        NavLinkDO navLink = navLinkService.getNavLink(id);
        return success(BeanUtils.toBean(navLink, NavLinkRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得常用网址导航分页")
    @PreAuthorize("@ss.hasPermission('system:nav-link:query')")
    public CommonResult<PageResult<NavLinkRespVO>> getNavLinkPage(@Validated NavLinkPageReqVO pageReqVO) {
        PageResult<NavLinkDO> pageResult = navLinkService.getNavLinkPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, NavLinkRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出常用网址导航 Excel")
    @PreAuthorize("@ss.hasPermission('system:nav-link:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportNavLinkExcel(HttpServletResponse response, @Validated NavLinkPageReqVO reqVO) throws IOException {
        reqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<NavLinkDO> list = navLinkService.getNavLinkPage(reqVO).getList();
        // 输出
        ExcelUtils.write(response, "常用网址导航数据.xls", "常用网址导航列表", NavLinkRespVO.class,
                BeanUtils.toBean(list, NavLinkRespVO.class));
    }

}
