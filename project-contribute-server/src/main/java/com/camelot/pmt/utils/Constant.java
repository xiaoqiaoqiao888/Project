package com.camelot.pmt.utils;

/**
 * @author gxl
 * @ClassName: Constant
 * @Description: TODO(常量类)
 * @date 2018年5月10日 下午2:55:32
 */
public class Constant {

    /**
     * 系统常量
     */
    public interface System {
        /**
         * 超级管理员ID
         */
        int SUPER_ADMIN = 1;
    }

    public interface Status {
        /**
         * 超预算
         */
        int OVER_BUDGET = 0;
        /**
         * 未超预算
         */
        int NO_OVER_BUDGET = 1;
        /**
         * 总任务
         */
        int GENERAL_TASKS = 3;
        /**
         * 未开始
         */
        int NO_START = 0;
        /**
         * 进行中
         */
        int HAVE_IN_HAND = 1;
        /**
         * 延期进行中
         */
        int DELAY_HAVE_IN_HAND = 4;
        /**
         * 已完成
         */
        int COMPLETED = 3;
        /**
         * 待验收
         */
        int WAIT_CHECK = 2;
    }

    /**
     * 数据状态
     */
    public interface DataStatus {
        /**
         * 无效
         */
        int INVALID = 0;
        /**
         * 有效
         */
        int EFFECTIVE = 1;
    }

    /**
     * 日志状态
     */
    public interface LogStatus {
        /**
         * 提交任务
         */
        String COMMENT_TASK = "提交任务";
        /**
         * 领取任务
         */
        String PULL_TASK = "领取任务";
        /**
         * 关闭任务
         */
        String CLOSE_TASK = "关闭任务";
        /**
         * 添加拆分任务
         */
        String SPLIT_TASK = "工程包拆分任务";
    }

    /**
     * 角色(用户角色)日志
     */
    public interface RoleLogStatus {
        /**
         * 新增角色
         */
        String ADD_ROLE = "新增角色";
        /**
         * 删除角色
         */
        String DELETE_ROLE = "删除角色";
        /**
         * 修改角色
         */
        String UPDATE_ROLE = "修改角色";
    }

    /**
     * 用户角色关联日志
     */
    public interface UserRoleLogStatus {
        /**
         * 新增角色
         */
        String User_ADD_ROLE = "赋予用户此角色";
        /**
         * 删除角色
         */
        String User_DELETE_ROLE = "删除用户此角色";
        /**
         * 修改角色
         */
        String Role_Add_User = "角色新增此用户";
        /**
         * 新增角色
         */
        String Role_DELETE_User = "角色删除此用户";
    }

    /**
     * 验收日志
     */
    public interface CheckManager {
        /**
         * 验收成功
         */
        String CHECK_SUCCESS = "验收成功";
        /**
         * 验收失败
         */
        String CHECK_FAILE = "验收失败";
        /**
         * 验收任务
         */
        String CHECK_TASK = "验收任务";
        /**
         * 验收工程包
         */
        String CHECK_WORK = "验收工程包";
        /**
         * 验收阶段
         */
        String CHECK_STAGE = "验收阶段";
        /**
         * 请求参数异常
         */
        String REQUEST_DATA_EXCEPTION = "请求参数异常";

        /**
         * 任务标识
         */
        int DIFF_TASK_SIGN = 1;
        /**
         * 工程包标识
         */
        int DIFF_WORK_SIGN = 2;
        /**
         * 阶段标识
         */
        int DIFF_STAGE_SIGN = 3;
    }

    /**
     * 工程包日志
     */
    public interface WorkManager {

        String ADD_WORK = "阶段拆分操作";

        String SUBMIT_WORK = "提交工程包";

        String CONFIRM_WORK = "确认工程包";

        String CHECK_WORK = "验收工程包";

        String UPDATE_WORK = "修改工程包";

        String DELETE_WORK = "删除工程包";

    }

    /**
     * 提示信息
     */
    public interface Manager {
        /**
         * 删除成功
         */
        String DELETE_SUCCESS = "删除成功";
        /**
         * 添加成功
         */
        String INSERT_SUCCESS = "添加成功";
        /**
         * 更新成功
         */
        String UPDATE_SUCCESS = "修改成功";
        /**
         * 请求参数异常
         */
        String REQUEST_DATA_EXCEPTION = "请求参数异常";
    }

    /**
     * 登录信息
     */
    public interface LoginMessage {
        /**
         * 用户名或密码为空
         */
        String USERNO_OR_PASSWORD_EMPTY = "用户名或密码为空";
        /**
         * 用户名或密码错误
         */
        String USERNO_OR_PASSWORD_ERROR = "用户名或密码错误";
        /**
         * 保存登录日志异常
         */
        String SAVE_LOGIN_LOG = "保存登录日志异常";
        /**
         * 保存登录日志异常
         */
        String UN_AUTHENTICATED = "认证失败";
    }

    /**
     * 操作描述
     */
    public interface OperateDesc {
        /**
         * 添加
         */
        String ADD = "添加";
        /**
         * 删除
         */
        String DELETE = "删除";
        /**
         * 修改
         */
        String UPDATE = "修改";
        /**
         * 成功
         */
        String SUCCESS = "成功";
        /**
         * 失败
         */
        String FAIL = "失败";
        /**
         * 请求参数异常
         */
        String REQUEST_DATA_EXCEPTION = "请求参数异常";
        /**
         * 操作人不存在
         */
        String OPERATOR_NOT_EXIST = "请求参数异常";
        /**
         * 部门级别限制最多三级
         */
        String MAXGROUPLEVEL = "部门最多添加三级！";
        /**
         * 部门组删除
         */
    }
}
