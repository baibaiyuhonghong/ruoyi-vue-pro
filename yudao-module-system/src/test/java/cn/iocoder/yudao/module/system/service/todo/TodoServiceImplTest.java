package cn.iocoder.yudao.module.system.service.todo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoStatusCountRespVO;
import cn.iocoder.yudao.module.system.controller.admin.todo.vo.TodoUpdateStatusReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.todo.TodoDO;
import cn.iocoder.yudao.module.system.dal.mysql.todo.TodoMapper;
import cn.iocoder.yudao.module.system.enums.todo.TodoPriorityEnum;
import cn.iocoder.yudao.module.system.enums.todo.TodoStatusEnum;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomLongId;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomPojo;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.TODO_NOT_EXISTS;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.TODO_NOT_OWNER;
import static org.junit.jupiter.api.Assertions.*;

@Import(TodoServiceImpl.class)
class TodoServiceImplTest extends BaseDbUnitTest {

    private static final Long LOGIN_USER_ID = 1L;
    private static final Long OTHER_USER_ID = 2L;

    @Resource
    private TodoServiceImpl todoService;

    @Resource
    private TodoMapper todoMapper;

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void login(Long userId) {
        SecurityFrameworkUtils.setLoginUser(new LoginUser().setId(userId), new MockHttpServletRequest());
    }

    private TodoDO insertTodo(Long userId, Integer status, Integer priority, LocalDateTime deadline) {
        TodoDO todo = randomPojo(TodoDO.class, o -> {
            o.setUserId(userId);
            o.setStatus(status);
            o.setPriority(priority);
            o.setDeadline(deadline);
            o.setFinishTime(TodoStatusEnum.isFinished(status) ? LocalDateTime.now() : null);
        });
        todoMapper.insert(todo);
        return todo;
    }

    private TodoUpdateStatusReqVO statusReqVO(Long id, Integer status) {
        TodoUpdateStatusReqVO reqVO = new TodoUpdateStatusReqVO();
        reqVO.setId(id);
        reqVO.setStatus(status);
        return reqVO;
    }

    @Test
    public void testCreateTodo_success() {
        login(LOGIN_USER_ID);
        // 准备参数。priority/status 带 @InEnum，randomPojo 不会填充，需显式设置
        TodoSaveReqVO reqVO = randomPojo(TodoSaveReqVO.class, o -> {
            o.setId(null);
            o.setPriority(TodoPriorityEnum.HIGH.getPriority());
            o.setStatus(TodoStatusEnum.PENDING.getStatus());
            o.setRemark("周五下班前发给主管");
        });

        // 调用
        Long todoId = todoService.createTodo(reqVO);

        // 校验插入属性是否正确，重点确认 userId 归属与 remark 被落库
        assertNotNull(todoId);
        TodoDO todo = todoMapper.selectById(todoId);
        assertPojoEquals(reqVO, todo, "id");
        assertEquals(LOGIN_USER_ID, todo.getUserId());
        assertEquals("周五下班前发给主管", todo.getRemark());
        assertNull(todo.getFinishTime()); // 未完成，不应有完成时间
    }

    @Test
    public void testCreateTodo_finished_setsFinishTime() {
        login(LOGIN_USER_ID);
        TodoSaveReqVO reqVO = randomPojo(TodoSaveReqVO.class, o -> {
            o.setId(null);
            o.setPriority(TodoPriorityEnum.LOW.getPriority());
            o.setStatus(TodoStatusEnum.FINISHED.getStatus());
        });

        Long todoId = todoService.createTodo(reqVO);

        assertNotNull(todoMapper.selectById(todoId).getFinishTime());
    }

    @Test
    public void testUpdateTodo_success() {
        login(LOGIN_USER_ID);
        TodoDO dbTodo = insertTodo(LOGIN_USER_ID, TodoStatusEnum.PENDING.getStatus(),
                TodoPriorityEnum.HIGH.getPriority(), null);
        TodoSaveReqVO reqVO = randomPojo(TodoSaveReqVO.class, o -> {
            o.setId(dbTodo.getId());
            o.setPriority(TodoPriorityEnum.MEDIUM.getPriority());
            o.setStatus(TodoStatusEnum.PROCESSING.getStatus());
            o.setRemark("改个备注");
        });

        todoService.updateTodo(reqVO);

        TodoDO todo = todoMapper.selectById(reqVO.getId());
        assertPojoEquals(reqVO, todo);
        assertEquals("改个备注", todo.getRemark());
        assertEquals(LOGIN_USER_ID, todo.getUserId()); // 归属不可被改写
        assertNull(todo.getFinishTime());
    }

    @Test
    public void testUpdateTodo_toFinished_setsFinishTime() {
        login(LOGIN_USER_ID);
        TodoDO dbTodo = insertTodo(LOGIN_USER_ID, TodoStatusEnum.PENDING.getStatus(),
                TodoPriorityEnum.HIGH.getPriority(), null);
        TodoSaveReqVO reqVO = randomPojo(TodoSaveReqVO.class, o -> {
            o.setId(dbTodo.getId());
            o.setPriority(TodoPriorityEnum.HIGH.getPriority());
            o.setStatus(TodoStatusEnum.FINISHED.getStatus());
        });

        todoService.updateTodo(reqVO);

        assertNotNull(todoMapper.selectById(dbTodo.getId()).getFinishTime());
    }

    @Test
    public void testUpdateTodo_toPending_clearsFinishTime() {
        login(LOGIN_USER_ID);
        TodoDO dbTodo = insertTodo(LOGIN_USER_ID, TodoStatusEnum.FINISHED.getStatus(),
                TodoPriorityEnum.HIGH.getPriority(), null);
        assertNotNull(dbTodo.getFinishTime());
        TodoSaveReqVO reqVO = randomPojo(TodoSaveReqVO.class, o -> {
            o.setId(dbTodo.getId());
            o.setPriority(TodoPriorityEnum.HIGH.getPriority());
            o.setStatus(TodoStatusEnum.PENDING.getStatus());
        });

        todoService.updateTodo(reqVO);

        // updateById 默认忽略 null，这里必须真的被清空
        assertNull(todoMapper.selectById(dbTodo.getId()).getFinishTime());
    }

    @Test
    public void testUpdateTodo_notExists() {
        login(LOGIN_USER_ID);
        TodoSaveReqVO reqVO = randomPojo(TodoSaveReqVO.class, o -> {
            o.setId(randomLongId());
            o.setPriority(TodoPriorityEnum.HIGH.getPriority());
            o.setStatus(TodoStatusEnum.PENDING.getStatus());
        });

        assertServiceException(() -> todoService.updateTodo(reqVO), TODO_NOT_EXISTS);
    }

    @Test
    public void testUpdateTodo_notOwner() {
        login(LOGIN_USER_ID);
        TodoDO dbTodo = insertTodo(OTHER_USER_ID, TodoStatusEnum.PENDING.getStatus(),
                TodoPriorityEnum.HIGH.getPriority(), null);
        TodoSaveReqVO reqVO = randomPojo(TodoSaveReqVO.class, o -> {
            o.setId(dbTodo.getId());
            o.setPriority(TodoPriorityEnum.HIGH.getPriority());
            o.setStatus(TodoStatusEnum.PENDING.getStatus());
        });

        assertServiceException(() -> todoService.updateTodo(reqVO), TODO_NOT_OWNER);
    }

    @Test
    public void testUpdateTodoStatus_toFinished_setsFinishTime() {
        login(LOGIN_USER_ID);
        TodoDO dbTodo = insertTodo(LOGIN_USER_ID, TodoStatusEnum.PENDING.getStatus(),
                TodoPriorityEnum.HIGH.getPriority(), null);

        todoService.updateTodoStatus(statusReqVO(dbTodo.getId(), TodoStatusEnum.FINISHED.getStatus()));

        TodoDO todo = todoMapper.selectById(dbTodo.getId());
        assertEquals(TodoStatusEnum.FINISHED.getStatus(), todo.getStatus());
        assertNotNull(todo.getFinishTime());
    }

    @Test
    public void testUpdateTodoStatus_toPending_clearsFinishTime() {
        login(LOGIN_USER_ID);
        TodoDO dbTodo = insertTodo(LOGIN_USER_ID, TodoStatusEnum.FINISHED.getStatus(),
                TodoPriorityEnum.HIGH.getPriority(), null);
        assertNotNull(dbTodo.getFinishTime());

        todoService.updateTodoStatus(statusReqVO(dbTodo.getId(), TodoStatusEnum.PENDING.getStatus()));

        TodoDO todo = todoMapper.selectById(dbTodo.getId());
        assertEquals(TodoStatusEnum.PENDING.getStatus(), todo.getStatus());
        assertNull(todo.getFinishTime());
    }

    @Test
    public void testUpdateTodoStatus_keepFinished_notRefreshFinishTime() {
        login(LOGIN_USER_ID);
        LocalDateTime oldFinishTime = LocalDateTime.of(2026, 11, 1, 10, 0, 0);
        TodoDO dbTodo = insertTodo(LOGIN_USER_ID, TodoStatusEnum.FINISHED.getStatus(),
                TodoPriorityEnum.HIGH.getPriority(), null);
        todoMapper.update(null, new LambdaUpdateWrapper<TodoDO>()
                .eq(TodoDO::getId, dbTodo.getId()).set(TodoDO::getFinishTime, oldFinishTime));

        todoService.updateTodoStatus(statusReqVO(dbTodo.getId(), TodoStatusEnum.FINISHED.getStatus()));

        assertEquals(oldFinishTime, todoMapper.selectById(dbTodo.getId()).getFinishTime());
    }

    @Test
    public void testUpdateTodoStatus_notOwner() {
        login(LOGIN_USER_ID);
        TodoDO dbTodo = insertTodo(OTHER_USER_ID, TodoStatusEnum.PENDING.getStatus(),
                TodoPriorityEnum.HIGH.getPriority(), null);

        assertServiceException(() -> todoService.updateTodoStatus(
                statusReqVO(dbTodo.getId(), TodoStatusEnum.FINISHED.getStatus())), TODO_NOT_OWNER);
    }

    @Test
    public void testDeleteTodo_success() {
        login(LOGIN_USER_ID);
        TodoDO dbTodo = insertTodo(LOGIN_USER_ID, TodoStatusEnum.PENDING.getStatus(),
                TodoPriorityEnum.HIGH.getPriority(), null);

        todoService.deleteTodo(dbTodo.getId());

        assertNull(todoMapper.selectById(dbTodo.getId()));
    }

    @Test
    public void testDeleteTodo_notExists() {
        login(LOGIN_USER_ID);
        assertServiceException(() -> todoService.deleteTodo(randomLongId()), TODO_NOT_EXISTS);
    }

    @Test
    public void testDeleteTodo_notOwner() {
        login(LOGIN_USER_ID);
        TodoDO dbTodo = insertTodo(OTHER_USER_ID, TodoStatusEnum.PENDING.getStatus(),
                TodoPriorityEnum.HIGH.getPriority(), null);

        assertServiceException(() -> todoService.deleteTodo(dbTodo.getId()), TODO_NOT_OWNER);
        assertNotNull(todoMapper.selectById(dbTodo.getId()));
    }

    @Test
    public void testGetTodo_success() {
        login(LOGIN_USER_ID);
        TodoDO dbTodo = insertTodo(LOGIN_USER_ID, TodoStatusEnum.PENDING.getStatus(),
                TodoPriorityEnum.HIGH.getPriority(), null);

        TodoDO todo = todoService.getTodo(dbTodo.getId());

        assertNotNull(todo);
        assertPojoEquals(dbTodo, todo);
    }

    @Test
    public void testGetTodo_notOwner() {
        login(LOGIN_USER_ID);
        TodoDO dbTodo = insertTodo(OTHER_USER_ID, TodoStatusEnum.PENDING.getStatus(),
                TodoPriorityEnum.HIGH.getPriority(), null);

        assertServiceException(() -> todoService.getTodo(dbTodo.getId()), TODO_NOT_OWNER);
    }

    @Test
    public void testGetTodoPage_filter() {
        login(LOGIN_USER_ID);
        // 命中一条
        TodoDO match = randomPojo(TodoDO.class, o -> {
            o.setUserId(LOGIN_USER_ID);
            o.setTitle("提交本周周报");
            o.setStatus(TodoStatusEnum.PENDING.getStatus());
            o.setPriority(TodoPriorityEnum.HIGH.getPriority());
            o.setFinishTime(null);
        });
        todoMapper.insert(match);
        // 标题不匹配
        todoMapper.insert(cloneTodo(match, o -> o.setTitle("其它事项")));
        // 状态不匹配
        todoMapper.insert(cloneTodo(match, o -> o.setStatus(TodoStatusEnum.PROCESSING.getStatus())));
        // 优先级不匹配
        todoMapper.insert(cloneTodo(match, o -> o.setPriority(TodoPriorityEnum.LOW.getPriority())));
        // 他人的待办，即使条件全中也不可见
        todoMapper.insert(cloneTodo(match, o -> o.setUserId(OTHER_USER_ID)));
        TodoPageReqVO reqVO = new TodoPageReqVO();
        reqVO.setTitle("周报");
        reqVO.setStatus(TodoStatusEnum.PENDING.getStatus());
        reqVO.setPriority(TodoPriorityEnum.HIGH.getPriority());

        PageResult<TodoDO> pageResult = todoService.getTodoPage(reqVO);

        assertEquals(1, pageResult.getTotal());
        assertEquals(1, pageResult.getList().size());
        assertPojoEquals(match, pageResult.getList().get(0));
    }

    @Test
    public void testGetTodoPage_onlyOwn() {
        login(LOGIN_USER_ID);
        insertTodo(LOGIN_USER_ID, TodoStatusEnum.PENDING.getStatus(), TodoPriorityEnum.HIGH.getPriority(), null);
        insertTodo(OTHER_USER_ID, TodoStatusEnum.PENDING.getStatus(), TodoPriorityEnum.HIGH.getPriority(), null);

        PageResult<TodoDO> pageResult = todoService.getTodoPage(new TodoPageReqVO());

        assertEquals(1, pageResult.getTotal());
        assertEquals(LOGIN_USER_ID, pageResult.getList().get(0).getUserId());
    }

    @Test
    public void testGetTodoPage_orderByDeadline() {
        login(LOGIN_USER_ID);
        LocalDateTime earlier = LocalDateTime.of(2026, 11, 18, 18, 0, 0);
        LocalDateTime later = LocalDateTime.of(2026, 11, 20, 18, 0, 0);
        TodoDO todoLater = insertTodo(LOGIN_USER_ID, TodoStatusEnum.PENDING.getStatus(),
                TodoPriorityEnum.HIGH.getPriority(), later);
        TodoDO todoEarlier = insertTodo(LOGIN_USER_ID, TodoStatusEnum.PENDING.getStatus(),
                TodoPriorityEnum.HIGH.getPriority(), earlier);
        TodoDO todoNoDeadline = insertTodo(LOGIN_USER_ID, TodoStatusEnum.PENDING.getStatus(),
                TodoPriorityEnum.HIGH.getPriority(), null);
        TodoDO todoEarlierNewer = insertTodo(LOGIN_USER_ID, TodoStatusEnum.PENDING.getStatus(),
                TodoPriorityEnum.HIGH.getPriority(), earlier);

        PageResult<TodoDO> pageResult = todoService.getTodoPage(new TodoPageReqVO());

        // 截止时间升序；同一截止时间按 id 倒序；无截止时间的排最后
        assertEquals(4, pageResult.getTotal());
        List<Long> ids = pageResult.getList().stream().map(TodoDO::getId).toList();
        assertEquals(List.of(todoEarlierNewer.getId(), todoEarlier.getId(), todoLater.getId(), todoNoDeadline.getId()), ids);
    }

    @Test
    public void testGetTodoStatusCount() {
        login(LOGIN_USER_ID);
        insertTodo(LOGIN_USER_ID, TodoStatusEnum.PENDING.getStatus(), TodoPriorityEnum.HIGH.getPriority(), null);
        insertTodo(LOGIN_USER_ID, TodoStatusEnum.PENDING.getStatus(), TodoPriorityEnum.LOW.getPriority(), null);
        insertTodo(LOGIN_USER_ID, TodoStatusEnum.PROCESSING.getStatus(), TodoPriorityEnum.HIGH.getPriority(), null);
        insertTodo(LOGIN_USER_ID, TodoStatusEnum.FINISHED.getStatus(), TodoPriorityEnum.HIGH.getPriority(), null);
        // 他人的待办不计入
        insertTodo(OTHER_USER_ID, TodoStatusEnum.PENDING.getStatus(), TodoPriorityEnum.HIGH.getPriority(), null);

        TodoStatusCountRespVO respVO = todoService.getTodoStatusCount();

        assertEquals(2L, respVO.getPendingCount());
        assertEquals(1L, respVO.getProcessingCount());
        assertEquals(1L, respVO.getFinishedCount());
        assertEquals(4L, respVO.getTotalCount());
    }

    @Test
    public void testGetTodoStatusCount_empty() {
        login(LOGIN_USER_ID);

        TodoStatusCountRespVO respVO = todoService.getTodoStatusCount();

        assertEquals(0L, respVO.getPendingCount());
        assertEquals(0L, respVO.getProcessingCount());
        assertEquals(0L, respVO.getFinishedCount());
        assertEquals(0L, respVO.getTotalCount());
    }

    private TodoDO cloneTodo(TodoDO source, Consumer<TodoDO> consumer) {
        TodoDO target = randomPojo(TodoDO.class, o -> {
            o.setUserId(source.getUserId());
            o.setTitle(source.getTitle());
            o.setContent(source.getContent());
            o.setStatus(source.getStatus());
            o.setPriority(source.getPriority());
            o.setDeadline(source.getDeadline());
            o.setFinishTime(source.getFinishTime());
            o.setRemark(source.getRemark());
        });
        consumer.accept(target);
        return target;
    }

}