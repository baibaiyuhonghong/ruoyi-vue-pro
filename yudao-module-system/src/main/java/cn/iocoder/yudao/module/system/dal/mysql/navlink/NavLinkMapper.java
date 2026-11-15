package cn.iocoder.yudao.module.system.dal.mysql.navlink;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.controller.admin.navlink.vo.NavLinkPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.navlink.NavLinkDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 常用网址导航 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface NavLinkMapper extends BaseMapperX<NavLinkDO> {

    default PageResult<NavLinkDO> selectPage(NavLinkPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<NavLinkDO>()
                .likeIfPresent(NavLinkDO::getName, reqVO.getName())
                .eqIfPresent(NavLinkDO::getCategory, reqVO.getCategory())
                .eqIfPresent(NavLinkDO::getStatus, reqVO.getStatus())
                .orderByAsc(NavLinkDO::getSort)
                .orderByDesc(NavLinkDO::getId));
    }

}
