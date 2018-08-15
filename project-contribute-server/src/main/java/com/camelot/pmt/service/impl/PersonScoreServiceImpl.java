package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.PersonScoreMapper;
import com.camelot.pmt.model.PersonScore;
import com.camelot.pmt.model.PersonScoreDTO;
import com.camelot.pmt.model.PersonScoreDetailsDTO;
import com.camelot.pmt.service.PersonScoreService;
import com.camelot.pmt.utils.TokenUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qiaodj
 * @date 2018年5月15日
 */
@Service
public class PersonScoreServiceImpl implements PersonScoreService {
    @Autowired
    private PersonScoreMapper personScoreMapper;

    /**
     * 价值分明细查询
     *
     * @param userId
     * @return
     */
    @Override
    public PageInfo<PersonScoreDetailsDTO> selectScoreDetails(Integer userId, Integer state, Integer pageNum,
            Integer pageSize) {
        if (userId == null) {
            return null;
        }
        PageHelper.startPage(pageNum == null || pageNum == 0 ? 1 : pageNum,
                pageSize == null || pageSize == 0 ? 10 : pageSize);
        List<PersonScoreDetailsDTO> list = personScoreMapper.selectScoreDetails(userId, state).stream().sorted(//
                Comparator.comparing(PersonScoreDetailsDTO::getCreateTime)).collect(Collectors.toList());
        // 分页查询
        PageInfo<PersonScoreDetailsDTO> pageResult = new PageInfo<>(list);
        pageResult.setList(list);
        return pageResult;
    }

    /**
     * 个人价值分总分以及排名查询
     *
     * @return
     */
    @Override
    public PersonScoreDTO sumScoreAndRank(Integer userId, Integer state) {
        if (userId == null) {
            return null;
        }
        // 只查询有效价值分
        return personScoreMapper.sumScoreAndRank(userId, state);
    }

    /**
     * 增加个人分表数据
     *
     * @param personScore
     * @return
     */
    @Override
    @Transactional
    public int insert(PersonScore personScore) {
        if (personScore.getUserId() == null || personScore.getProjectId() == null || personScore.getStageId() == null
                || personScore.getTaskId() == null || personScore.getTaskValue() == null
                || personScore.getWorkId() == null || personScore.getState() == null) {
            return 0;
        }
        personScore.setCreateBy(TokenUtil.getUserFromToken().getId());
        personScore.setUpdateBy(TokenUtil.getUserFromToken().getId());
        return personScoreMapper.insert(personScore);
    }

    /**
     * 根据主键id删除
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int deleteByPrimaryKey(Integer id) {
        if (id == null) {
            return 0;
        }
        return personScoreMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据id更新价值表数据
     *
     * @param personScore
     * @return
     */
    @Override
    @Transactional
    public int updateByPrimaryKeySelective(PersonScore personScore) {
        if (personScore.getId() == null) {
            return 0;
        }
        personScore.setUpdateBy(TokenUtil.getUserFromToken().getId());
        return personScoreMapper.updateByPrimaryKeySelective(personScore);
    }
}
