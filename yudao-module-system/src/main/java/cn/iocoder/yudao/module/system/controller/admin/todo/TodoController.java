package cn.iocoder.yudao.module.system.controller.admin.todo;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoRespVO;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoStatusCountRespVO;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoUpdateStatusReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.todo.TodoDO;
import cn.iocoder.yudao.module.system.service.todo.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 个人待办")
@RestController
@RequestMapping("/system/todo")
@Validated
public class TodoController {

    @Resource
    private TodoService todoService;

    @PostMapping("/create")
    @Operation(summary = "创建个人待办")
    @PreAuthorize("@ss.hasPermission('system:todo:create')")
    public CommonResult<Long> createTodo(@Valid @RequestBody TodoSaveReqVO createReqVO) {
        return success(todoService.createTodo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改个人待办")
    @PreAuthorize("@ss.hasPermission('system:todo:update')")
    public CommonResult<Boolean> updateTodo(@Valid @RequestBody TodoSaveReqVO updateReqVO) {
        todoService.updateTodo(updateReqVO);
        return success(true);
    }

    @PutMapping("/update-status")
    @Operation(summary = "变更个人待办状态")
    @PreAuthorize("@ss.hasPermission('system:todo:update')")
    public CommonResult<Boolean> updateTodoStatus(@Valid @RequestBody TodoUpdateStatusReqVO updateReqVO) {
        todoService.updateTodoStatus(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除个人待办")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:todo:delete')")
    public CommonResult<Boolean> deleteTodo(@RequestParam("id") Long id) {
        todoService.deleteTodo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得个人待办")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:todo:query')")
    public CommonResult<TodoRespVO> getTodo(@RequestParam("id") Long id) {
        TodoDO todo = todoService.getTodo(id);
        return success(BeanUtils.toBean(todo, TodoRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得个人待办分页")
    @PreAuthorize("@ss.hasPermission('system:todo:query')")
    public CommonResult<PageResult<TodoRespVO>> getTodoPage(@Validated TodoPageReqVO pageReqVO) {
        PageResult<TodoDO> pageResult = todoService.getTodoPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, TodoRespVO.class));
    }

    @GetMapping("/status-count")
    @Operation(summary = "获得我的待办按状态统计")
    @PreAuthorize("@ss.hasPermission('system:todo:query')")
    public CommonResult<TodoStatusCountRespVO> getTodoStatusCount() {
        return success(todoService.getTodoStatusCount());
    }

}