package com.fantacg.common.dto.user;

import com.fantacg.common.utils.QpGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname WorkerDto 注册时使用  不需要添加数据库
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerDto {

    /**
     * 工人姓名
     */
    @NotEmpty(message = "姓名不能为空", groups = QpGroup.Add.class)
    private String name;

    /**
     * 证件类型
     */
    @NotEmpty(message = "证件类型不能为空", groups = QpGroup.Add.class)
    private String idCardType;

    /**
     * 证件号码
     */
    @NotEmpty(message = "证件号码不能为空", groups = QpGroup.Add.class)
    private String idCardNumber;

    /**
     * 工人性别
     */
    @NotEmpty(message = "性别不能为空", groups = QpGroup.Add.class)
    private String gender;

    /**
     * 民族
     */
    @NotEmpty(message = "民族不能为空", groups = QpGroup.Add.class)
    private String nation;

    /**
     * 出生日期
     */
    @NotNull(message = "出生日期不能为空", groups = QpGroup.Add.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    /**
     * 籍贯
     */
    private String birthPlaceCode;

    /**
     * 住址
     */
    @NotEmpty(message = "住址不能为空", groups = QpGroup.Add.class)
    private String address;

    /**
     * 头像
     */
    @NotEmpty(message = "头像不能为空", groups = QpGroup.Add.class)
    private String headImageUrl;

    /**
     * 手机号码
     */
    private String cellPhone;

    /**
     * 发证机关
     */
    @NotEmpty(message = "发证机关不能为空", groups = QpGroup.Add.class)
    private String grantOrg;

    /**
     * 正面照 URL
     */
    @NotEmpty(message = "无效的身份证正面", groups = QpGroup.Add.class)
    private String positiveIdCardImageUrl;

    /**
     * 反面照 URL
     */
    @NotEmpty(message = "无效的身份证反面", groups = QpGroup.Add.class)
    private String negativeIdCardImageUrl;

    /**
     * 有效期开始日期
     */
    @NotNull(message = "有效期开始日期不能为空", groups = QpGroup.Add.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    /**
     * 有效期结束日期
     */
    @NotNull(message = "有效期结束日期不能为空", groups = QpGroup.Add.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expiryDate;
}
