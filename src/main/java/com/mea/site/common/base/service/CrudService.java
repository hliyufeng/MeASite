package com.mea.site.common.base.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mea.site.common.base.mapper.BaseMapper;
import com.mea.site.common.base.model.AbstractEntity;
import com.mea.site.common.base.model.DataEntity;
import com.mea.site.common.support.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by Michael Jou on 2018/3/2. 14:32
 */
@Transactional(readOnly = true, rollbackFor = Exception.class)
@Slf4j
public abstract class CrudService<T extends DataEntity<T>, M extends BaseMapper<T>> extends BaseService {

    @Autowired
    protected M mapper;

    public T get(String id) {
        return mapper.get(id);
    }

    /**
     * 获取单条数据
     *
     * @param entity
     * @return
     */
    public T get(T entity) {
        return mapper.get(entity);
    }

    /**
     * 查询列表数据
     *
     * @param entity
     * @return
     */
    public List<T> findList(T entity) {
        return mapper.findList(entity);
    }

    /**
     * 查询分页数据
     *
     * @param page 分页对象
     * @return
     */
    public PageInfo<T> findPage(Page<T> page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<T> eList = mapper.findList(page.getData());
        PageInfo<T> pageInfos = new PageInfo<T>(eList);
        return pageInfos;
    }


    /**
     * 分页查询
     *
     * @param page
     * @return
     */
    public PageInfo<T> selectPage(Page<T> page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<T> eList = mapper.select(page.getData());
        PageInfo<T> pageInfos = new PageInfo<T>(eList);
        return pageInfos;
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param entity
     */
    @Transactional(readOnly = false)
    public int saveCustom(T entity) {
        if (entity.getIsNewRecord()) {
            entity.preInsert();
            return mapper.insertCustom(entity);
        } else {
            entity.preUpdate();
            return mapper.update(entity);
        }
    }

    @Transactional(readOnly = false)
    public int save(T entity) {
        if (entity.getIsNewRecord()) {
            entity.preInsert();
            return mapper.insert(entity);
        } else {
            entity.preUpdate();
            return mapper.insert(entity);
        }
    }

    /**
     * 删除数据
     *
     * @param entity
     */
    @Transactional(readOnly = false)
    public int deleteLogic(T entity) {
        return mapper.deleteLogic(entity);
    }

    /**
     * 物理删除
     *
     * @return
     */
    @Transactional(readOnly = false)
    public int delete(T entity, boolean mode) {
        if (!mode) {
            entity.preUpdate();
            entity.setDelFlag(T.DEL_FLAG_DELETE);
            return this.mapper.update(entity);
        }
        return this.mapper.delete(entity);
    }


    /**
     * 根据主键删除
     *
     * @param entity
     * @return
     */
    @Transactional(readOnly = false)
    public int deleteByPrimaryKey(T entity, boolean mode) {
        if (!mode) {
            entity.preUpdate();
            entity.setDelFlag(T.DEL_FLAG_DELETE);
            return this.mapper.updateByPrimaryKey(entity);
        }
        return this.mapper.deleteByPrimaryKey(entity);
    }


    @Transactional(readOnly = false)
    public int deleteByExample(Condition condition, T entity, boolean mode) {
        if (!mode) {
            entity.preUpdate();
            entity.setDelFlag(T.DEL_FLAG_DELETE);
            this.mapper.updateByExample(entity, condition);
        }
        return this.mapper.deleteByExample(condition);
    }

    /**
     * 获取所有
     *
     * @return
     */
    public List<T> findAll() {
        return mapper.selectAll();
    }

    /**
     * 根据条件获取所有
     *
     * @param e
     * @return
     */
    public List<T> find(T e) {
        return this.mapper.select(e);
    }

    /**
     * 更具条件获取
     *
     * @param e
     * @return
     */
    public T findOne(T e) {
        return this.mapper.selectByPrimaryKey(e);
    }

    public T findOne(String id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    public T selectOne(T t) {
        return this.mapper.selectOne(t);
    }

    public int selectCount(T t) {
        return this.mapper.selectCount(t);
    }

    /**
     * 查询count 数据
     *
     * @param example
     * @return
     */
    public int selectCountByExample(Example example) {
        return this.mapper.selectCountByExample(example);
    }

    @Transactional(readOnly = false)
    public int insert(T t, boolean mode) {
        if (!mode) {
            t.preInsert();
        }
        return this.mapper.insert(t);
    }

    /**
     * 不为空
     *
     * @param t
     * @param mode
     * @return
     */
    @Transactional(readOnly = false)
    public int insertSelective(T t, boolean mode) {
        if (!mode) {
            t.preInsert();
        }
        return this.mapper.insertSelective(t);
    }

    @Transactional(readOnly = false)
    public int insertList(List<T> tLists, boolean mode) {
        if (!mode) {
            if (CollectionUtils.isNotEmpty(tLists)) {
                tLists.forEach(t -> {
                    t.preInsert();
                });
            }
        }
        return this.mapper.insertList(tLists);
    }

    @Transactional(readOnly = false)
    public int updateByPrimaryKey(T t, boolean mode) {
        if (!mode) {
            t.preUpdate();
        }
        return this.mapper.updateByPrimaryKey(t);
    }

    @Transactional(readOnly = false)
    public int updateByPrimaryKeySelective(T t, boolean mode) {
        if (!mode) {
            t.preUpdate();
        }
        return this.mapper.updateByPrimaryKeySelective(t);
    }

    @Transactional(readOnly = false)
    public int updateByExample(Example example, T t, boolean mode) {
        if (!mode) {
            t.preUpdate();
        }
        return this.mapper.updateByExample(t, example);
    }

    /**
     * 更新
     *
     * @param t
     * @param example
     * @param mode
     * @return
     */
    @Transactional(readOnly = false)
    public int updateByExampleSelective(T t, Example example, boolean mode) {
        if (!mode) {
            t.preUpdate();
        }
        return this.mapper.updateByExampleSelective(t, example);
    }

    /**
     * 查询count 数据
     *
     * @param condition
     * @return
     */
    public int selectCountByExample(Condition condition) {
        return this.mapper.selectCountByExample(condition);
    }

    public List<T> selectByExample(Example example) {
        return this.mapper.selectByExample(example);
    }

    public Condition buildCondition(Class<T> eClass) {
        return new Condition(eClass);
    }

    public Example buildExample(Class<T> eClass) {
        return new Example(eClass);
    }
}
