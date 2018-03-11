package com.mea.site.module.sys.dto;

import com.mea.site.common.base.dto.BaseDto;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by lenovo on 2018/2/27.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto extends BaseDto {

    private String title;
    private String icon;
    private String href;
    private boolean spread = false;
    private List<MenuDto> children = Lists.newArrayList();

}
