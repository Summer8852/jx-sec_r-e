package com.fantacg.user.service;

import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.pojo.user.Wallet;
import com.fantacg.common.utils.Result;
import com.fantacg.user.filter.LoginInterceptor;
import com.fantacg.user.mapper.WalletMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname WalletService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
public class WalletService {

    @Autowired
    WalletMapper walletMapper;

    /**
     * 查询钱余额
     * @return 返回余额信息
     */
//    public Result selectWalletByMemberId() {
//        Claims claims = LoginInterceptor.getLoginClaims();
//        Long id = ObjectUtils.toLong(claims.get(JwtConstans.JWT_KEY_ID));
//        String roles = (String) claims.get("roles");
//        Wallet wallet = new Wallet();
//        wallet.setMemberId(id);
//        wallet.setRoleType(roles);
//        wallet = walletMapper.selectOne(wallet);
//        return Result.success(wallet);
//    }

}
