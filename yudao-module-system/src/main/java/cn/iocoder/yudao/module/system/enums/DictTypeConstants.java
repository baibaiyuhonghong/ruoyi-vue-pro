package cn.iocoder.yudao.module.system.enums;

/**
 * System 字典类型的枚举类
 *
 * @author 芋道源码
 */
public interface DictTypeConstants {

    String USER_TYPE = "user_type"; // 用户类型
    String COMMON_STATUS = "common_status"; // 系统状态

    // ========== SYSTEM 模块 ==========

    String USER_SEX = "system_user_sex"; // 用户性别
    String DATA_SCOPE = "system_data_scope"; // 数据范围

    String LOGIN_TYPE = "system_login_type"; // 登录日志的类型
    String LOGIN_RESULT = "system_login_result"; // 登录结果

    String SMS_CHANNEL_CODE = "system_sms_channel_code"; // 短信渠道编码
    String SMS_TEMPLATE_TYPE = "system_sms_template_type"; // 短信模板类型
    String SMS_SEND_STATUS = "system_sms_send_status"; // 短信发送状态
    String SMS_RECEIVE_STATUS = "system_sms_receive_status"; // 短信接收状态

    String NAV_LINK_CATEGORY = "system_nav_link_category"; // 网址导航分类

    String TODO_STATUS = "system_todo_status"; // 个人待办状态
    String TODO_PRIORITY = "system_todo_priority"; // 个人待办优先级

}
