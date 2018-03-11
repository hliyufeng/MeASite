package com.mea.site.common.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Michael Jou
 * @PACKAGE com.gladtrust.measite.common.support.status
 * @program: measite-manage
 * @description: ${description}
 * @create: 2018-02-08 16:09
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Page<T> implements Serializable{
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private T data;

    public Page put(T t){
        this.data = t;
        return this;
    }
}
