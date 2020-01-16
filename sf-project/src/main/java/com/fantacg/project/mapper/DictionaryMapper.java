package com.fantacg.project.mapper;

import com.fantacg.common.dto.project.DictVO;
import com.fantacg.common.pojo.project.Dictionary;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname DictionaryMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface DictionaryMapper  extends Mapper<Dictionary> {

    Page<DictVO> selectDictPage(Map<String, Object> params);

    List<DictVO> selectDictParentId(Long parentId);

    List<DictVO> selectDictByType(String type);

    List<DictVO> selectDictParentList();

    HashMap<String,Object> selectDictById(String id);
}
