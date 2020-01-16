package com.fantacg.common.constant;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname KeyConstant redis Key 值
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
public class KeyConstant {

    /**
     * 用户注册发送短信
     */
    public static final String MEMBER_REGISTER_CODE_KEY_PREFIX = "member:register:code:phone:";
    /**
     * 用户登录发送短信
     */
    public static final String MEMBER_LOGIN_CODE_KEY_PREFIX = "member:login:code:phone:";

    /**
     * 工人注册发送短信
     */
    public static final String WORKER_REGISTER_CODE_KEY_PREFIX = "worker:register:code:phone:";
    /**
     * 工人登录发送短信
     */
    public static final String WORKER_LOGIN_CODE_KEY_PREFIX = "worker:login:code:phone:";

    /**
     * 用户访问权限
     */
    public static final String PERMISS_KEY_PREFIX = "member:permission:id:";

    /**
     * 项目编号缓存
     */
    public static final String PROJECT_KEY_PREFIX = "project:code:";

    /**
     * uuid 手机号缓存
     */
    public static final String UUID_KEY_PHONECODE = "uuid:phone:";

    /**
     * 注册邮箱验证码
     */
    public static final String MEMBER_CODE_EMAIL_KEY = "member:code:email:";
    /**
     * 绑定邮箱验证码
     */
    public static final String MEMBER_BIND_EMAIL_KEY = "member:bind:email:";
    /**
     * 绑定手机验证码
     */
    public static final String MEMBER_BIND_PHONE_KEY = "member:bind:phone:";

    /**
     * 忘记密码（手机号）
     */
    public static final String FORGET_CODE_PHONE_KEY = "forget:code:phone:";
    /**
     * 忘记密码（邮箱）
     */
    public static final String FORGET_CODE_EMAIL_KEY = "forget:code:email:";


    /***************************  菜单目录的缓存 *******************************/

    /**
     * 菜单目录的缓存
     */
    public static final String MENU_LIST_KEY = "menu:list:";


    /***************************  字典表缓存KEY *******************************/

    /**
     * 字典表ID
     */
    public static final String DICT_NAEM_ID = "dict:name:id:";

    /**
     * 字典表类型
     */
    public static final String DICT_LIST_TYPE = "dict:list:type";

    /**
     * 字典表
     */
    public static final String DICT_LIST = "dict:key:";

    /**
     * 字典表Key
     */
    public static final String DICTIONARY_KEY = "dictionary:key:all";

    /**
     * 字典表 （type 查询）
     */
    public static final String DICTIONARY_TYPE_KEY = "dictionary:type:key:";


    /**
     * 企业资质资格专业类别 字典表
     */
    public static final String DICTIONARY_TRADETYPEBOUND_KEY = "dictionary:tradetypebound:key:";

    /**
     * 企业资质等级 字典表
     */
    public static final String DICTIONARY_CERTTITLELEVEL_KEY = "dictionary:certtitlelevel:key:";


    /************************** 视频类缓存 *************************************/


    /**
     * 视频分类缓存
     */
    public static final String VIDEO_LIST = "video";

    /**
     * 视频分类缓存
     */
    public static final String VIDEO_CATEGORY_LIST = "video:category:";

    /**
     * 视频中心
     */
    public static final String INDEX_VIDEO_LIST = "index:video:";
    /**
     * 培训视频列表
     */
    public static final String TRAIN_VIDEO_LIST = "train:video:";
    /**
     * 视频播放记录
     */
    public static final String VIDEO_PLAY_LOG = "video:play:log:";


    /**
     * 视频考试题目
     */
    public static final String VIDEO_TITLE_ALL = "video:title:all";

    /******************** 支付 **************************/

    public static final String ALIPLAY_KEY = "aliplay:";

    /******************* 行政区域和编码缓存 ********************/
    /**
     * 行政区域和编码列表缓存
     */
    public static final String AREA_CODE_LIST = "area:code:list";

    public static final String AREA_CODENAME = "area:codename:";


    /*******************世界各国和地区名称代码 *******************/
    /**
     * 世界各国和地区名称代码列表缓存
     */
    public static final String NATION_LIST = "nation:list";

    public static final String NATION_ID = "nation:id:";


    /****************** 项目缓存 ****************************/
    public static final String PROJECT_DETAIL = "project:detail:id:";

    /****************** 邮箱服务账号缓存 ****************************/

    /**
     * 邮箱服务账号缓存
     */
    public static final String SMTP_MAIL = "smtp:mail";


    /**************************** 台式居民身份证阅读机 ************************************/
    public static final String MEMBER_SAMIDEX = "member:samidex:id:";

    /**************************** 考试试题 ************************************/
    /**
     * 试题Id
     */
    public static final String ANSWER_ID_KEY_PREFIX = "answer:id:";


    /**
     * 工种试题
     */
    public static final String ANSWER_WORKTYPE_KEY_PREFIX = "answer:worktype:";

    /**
     * 所有试题
     */
    public static final String ANSWER_LIST_KEY_PREFIX = "answer:list";
    /**
     * 试题（快速学习）
     */
    public static final String QUICKANSWER_LIST_KEY_PREFIX = "quickanswer:list";
    /**
     * 培训列表（快速学习）
     */
    public static final String QUICKPROJECTTRAINING_MEMBER_LIST_PREFIX = "QuickProjectTraining:memberid:list:";

    /**
     * 工种培训内容
     */
    public static final String TRAININGCONTENT_WORKTYPE_KEY_PREFIX = "trainingcontent:worktype:";

    /**
     * 所有分类视频
     */
    public static final String VIDEO_LIST_KEY_PREFIX = "video:list:memberId:";

    /**
     * 培训详情视频列表
     */
    public static final String TRAINING_VIDEO_KEY_PREFIX = "training:video:list:";
    /**
     * 培训详情视频列表
     */
    public static final String TRAINING_ANSWER_KEY_PREFIX = "training:answer:list:";

    /**
     * 所有试题
     */
    public static final String TRAININGCONTENT_LIST_KEY_PREFIX = "trainingcontent:list";

    /**
     * 工种Id
     */
    public static final String WORKTYPE_ID_KEY_PREFIX = "worktype:id:";

    /**
     * 所有工种
     */
    public static final String WORKTYPE_LIST_KEY_PREFIX = "worktype:list";

    /**
     * 实人信息
     */
    public static final String WORKINFO_CARD_KEY_PREFIX = "workinfo:card:key:";

    /**
     * 籍贯
     */
    public static final String BIRTH_PLACE_KEY_PREFIX = "birthplace:id:";


    /**
     * 模板文档
     */
    public static final String WORD_ID = "word:id:";

    public static final String MEMBER_WORKERS = "memberWorkers:";


}
