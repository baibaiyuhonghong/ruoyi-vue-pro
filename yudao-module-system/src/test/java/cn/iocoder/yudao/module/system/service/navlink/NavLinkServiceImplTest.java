package cn.iocoder.yudao.module.system.service.navlink;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.system.controller.admin.navlink.vo.NavLinkPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.navlink.vo.NavLinkSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.navlink.NavLinkDO;
import cn.iocoder.yudao.module.system.dal.mysql.navlink.NavLinkMapper;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import jakarta.annotation.Resource;

import java.util.Arrays;

import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomLongId;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomPojo;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.NAV_LINK_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

@Import(NavLinkServiceImpl.class)
class NavLinkServiceImplTest extends BaseDbUnitTest {

    @Resource
    private NavLinkServiceImpl navLinkService;

    @Resource
    private NavLinkMapper navLinkMapper;

    @Test
    public void testCreateNavLink_success() {
        // 准备参数。status 带 @InEnum，randomPojo 不会填充，需显式设置
        NavLinkSaveReqVO reqVO = randomPojo(NavLinkSaveReqVO.class, o -> {
            o.setId(null); // 避免 id 被赋值
            o.setStatus(CommonStatusEnum.ENABLE.getStatus());
            o.setRemark("常用的开发文档站点");
        });

        // 调用
        Long navLinkId = navLinkService.createNavLink(reqVO);

        // 校验插入属性是否正确，重点确认 remark 被落库
        assertNotNull(navLinkId);
        NavLinkDO navLink = navLinkMapper.selectById(navLinkId);
        assertPojoEquals(reqVO, navLink, "id");
        assertEquals("常用的开发文档站点", navLink.getRemark());
    }

    @Test
    public void testUpdateNavLink_success() {
        // 插入前置数据
        NavLinkDO dbNavLink = randomPojo(NavLinkDO.class);
        navLinkMapper.insert(dbNavLink);
        // 准备更新参数。status 带 @InEnum，randomPojo 不会填充，需显式设置
        NavLinkSaveReqVO reqVO = randomPojo(NavLinkSaveReqVO.class, o -> {
            o.setId(dbNavLink.getId());
            o.setStatus(CommonStatusEnum.ENABLE.getStatus());
            o.setRemark("改个备注");
        });

        // 调用
        navLinkService.updateNavLink(reqVO);

        // 校验是否更新成功
        NavLinkDO navLink = navLinkMapper.selectById(reqVO.getId());
        assertPojoEquals(reqVO, navLink);
        assertEquals("改个备注", navLink.getRemark());
    }

    @Test
    public void testUpdateNavLink_notExists() {
        // 准备参数
        NavLinkSaveReqVO reqVO = randomPojo(NavLinkSaveReqVO.class, o -> o.setId(randomLongId()));

        // 调用，并校验异常
        assertServiceException(() -> navLinkService.updateNavLink(reqVO), NAV_LINK_NOT_EXISTS);
    }

    @Test
    public void testDeleteNavLink_success() {
        // 插入前置数据
        NavLinkDO dbNavLink = randomPojo(NavLinkDO.class);
        navLinkMapper.insert(dbNavLink);

        // 调用
        navLinkService.deleteNavLink(dbNavLink.getId());

        // 校验是否删除成功（逻辑删除）
        assertNull(navLinkMapper.selectById(dbNavLink.getId()));
    }

    @Test
    public void testDeleteNavLink_notExists() {
        assertServiceException(() -> navLinkService.deleteNavLink(randomLongId()), NAV_LINK_NOT_EXISTS);
    }

    @Test
    public void testDeleteNavLinkList_success() {
        // 插入前置数据
        NavLinkDO navLink01 = randomPojo(NavLinkDO.class);
        navLinkMapper.insert(navLink01);
        NavLinkDO navLink02 = randomPojo(NavLinkDO.class);
        navLinkMapper.insert(navLink02);
        NavLinkDO navLink03 = randomPojo(NavLinkDO.class);
        navLinkMapper.insert(navLink03);

        // 调用
        navLinkService.deleteNavLinkList(Arrays.asList(navLink01.getId(), navLink02.getId()));

        // 校验：前两条被删除，第三条保留
        assertNull(navLinkMapper.selectById(navLink01.getId()));
        assertNull(navLinkMapper.selectById(navLink02.getId()));
        assertNotNull(navLinkMapper.selectById(navLink03.getId()));
    }

    @Test
    public void testGetNavLink_success() {
        // 插入前置数据
        NavLinkDO dbNavLink = randomPojo(NavLinkDO.class);
        navLinkMapper.insert(dbNavLink);

        // 调用
        NavLinkDO navLink = navLinkService.getNavLink(dbNavLink.getId());

        // 校验插入与读取对象是否一致
        assertNotNull(navLink);
        assertPojoEquals(dbNavLink, navLink);
    }

    @Test
    public void testGetNavLinkPage_success() {
        // 插入前置数据：命中一条
        NavLinkDO dbNavLink = randomPojo(NavLinkDO.class, o -> {
            o.setName("芋道源码");
            o.setCategory(1);
            o.setStatus(CommonStatusEnum.ENABLE.getStatus());
        });
        navLinkMapper.insert(dbNavLink);
        // 名称不匹配
        navLinkMapper.insert(cloneIgnoreId(dbNavLink, o -> o.setName("尼古拉斯赵四")));
        // 分类不匹配
        navLinkMapper.insert(cloneIgnoreId(dbNavLink, o -> o.setCategory(2)));
        // 状态不匹配
        navLinkMapper.insert(cloneIgnoreId(dbNavLink, o -> o.setStatus(CommonStatusEnum.DISABLE.getStatus())));
        // 准备参数
        NavLinkPageReqVO reqVO = new NavLinkPageReqVO();
        reqVO.setName("芋道");
        reqVO.setCategory(1);
        reqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());

        // 调用
        PageResult<NavLinkDO> pageResult = navLinkService.getNavLinkPage(reqVO);

        // 校验查询结果经过筛选
        assertEquals(1, pageResult.getTotal());
        assertEquals(1, pageResult.getList().size());
        assertPojoEquals(dbNavLink, pageResult.getList().get(0));
    }

    @Test
    public void testGetNavLinkPage_orderBySort() {
        // 插入前置数据：sort 大的先入库
        NavLinkDO navLinkSort02 = randomPojo(NavLinkDO.class, o -> o.setSort(2));
        navLinkMapper.insert(navLinkSort02);
        NavLinkDO navLinkSort01 = randomPojo(NavLinkDO.class, o -> o.setSort(1));
        navLinkMapper.insert(navLinkSort01);
        // 准备参数
        NavLinkPageReqVO reqVO = new NavLinkPageReqVO();

        // 调用
        PageResult<NavLinkDO> pageResult = navLinkService.getNavLinkPage(reqVO);

        // 校验按 sort 升序
        assertEquals(2, pageResult.getTotal());
        assertEquals(navLinkSort01.getId(), pageResult.getList().get(0).getId());
        assertEquals(navLinkSort02.getId(), pageResult.getList().get(1).getId());
    }

}
