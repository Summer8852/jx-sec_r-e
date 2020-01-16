package com.fantacg.project.mapper;

import com.fantacg.common.dto.project.DictVO;
import com.fantacg.common.pojo.project.DictionaryTradeTypeBound;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname DictionaryTradeTypeBoundMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface DictionaryTradeTypeBoundMapper  extends Mapper<DictionaryTradeTypeBound> {

    List<DictVO> dicTradeTypeBoundList();
}
