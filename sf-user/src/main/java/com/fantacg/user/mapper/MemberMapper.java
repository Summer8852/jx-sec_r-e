package com.fantacg.user.mapper;

import com.fantacg.common.pojo.user.Member;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname MemberMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface MemberMapper extends Mapper<Member> {

    /**
     * 查询手机号是否已注册
     * @param phone
     * @return
     */
    Map<String,Object> selectMemberByPhone(String phone);

    /**
     * 手机号查询
     * @param phone
     * @return
     */
    int selectLoginMemberByPhone(String phone);

    /**
     * 邮箱查询
     * @param email
     * @return
     */
    int selectMemberByEmail(String email);

}
