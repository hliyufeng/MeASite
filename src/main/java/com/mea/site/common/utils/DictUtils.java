package com.mea.site.common.utils;


import com.alibaba.fastjson.JSON;
import com.mea.site.module.sys.mapper.DictMapper;
import com.mea.site.module.sys.model.Dict;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jou on 2018/3/5. 13:53
 */
public class DictUtils {
    private static DictMapper dictMapper = SpringContextHolder.getBean(DictMapper.class);

    public static final String CACHE_DICT_MAP = "dictMap";

    public static String getDictLabel(String value, String type, String defaultValue){
        if (org.apache.commons.lang3.StringUtils.isNotBlank(type) && org.apache.commons.lang3.StringUtils.isNotBlank(value)){
            for (Dict dict : getDictList(type)){
                if (type.equals(dict.getType()) && value.equals(dict.getValue())){
                    return dict.getLabel();
                }
            }
        }
        return defaultValue;
    }

    public static String getDictLabels(String values, String type, String defaultValue){
        if (org.apache.commons.lang3.StringUtils.isNotBlank(type) && org.apache.commons.lang3.StringUtils.isNotBlank(values)){
            List<String> valueList = Lists.newArrayList();
            for (String value : org.apache.commons.lang3.StringUtils.split(values, ",")){
                valueList.add(getDictLabel(value, type, defaultValue));
            }
            return org.apache.commons.lang3.StringUtils.join(valueList, ",");
        }
        return defaultValue;
    }

    public static String getDictValue(String label, String type, String defaultLabel){
        if (org.apache.commons.lang3.StringUtils.isNotBlank(type) && org.apache.commons.lang3.StringUtils.isNotBlank(label)){
            for (Dict dict : getDictList(type)){
                if (type.equals(dict.getType()) && label.equals(dict.getLabel())){
                    return dict.getValue();
                }
            }
        }
        return defaultLabel;
    }

    public static List<Dict> getDictList(String type){
        @SuppressWarnings("unchecked")
        Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>)CacheUtils.get(CACHE_DICT_MAP);
        if (dictMap==null){
            dictMap = Maps.newHashMap();
            for (Dict dict : dictMapper.findAllList(new Dict())){
                List<Dict> dictList = dictMap.get(dict.getType());
                if (dictList != null){
                    dictList.add(dict);
                }else{
                    dictMap.put(dict.getType(), Lists.newArrayList(dict));
                }
            }
            CacheUtils.put(CACHE_DICT_MAP, dictMap);
        }
        List<Dict> dictList = dictMap.get(type);
        if (dictList == null){
            dictList = Lists.newArrayList();
        }
        return dictList;
    }

    /**
     * 返回字典列表（JSON）
     * @param type
     * @return
     */
    public static String getDictListJson(String type){
        return JSON.toJSONString(getDictList(type));
    }
}
