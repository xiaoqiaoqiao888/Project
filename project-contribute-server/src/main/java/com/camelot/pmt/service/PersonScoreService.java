package com.camelot.pmt.service;

import com.camelot.pmt.model.PersonScore;
import com.camelot.pmt.model.PersonScoreDTO;
import com.camelot.pmt.model.PersonScoreDetailsDTO;
import com.github.pagehelper.PageInfo;

public interface PersonScoreService {

    /**
     * 个人价值分总分以及排名查询
     *
     * @param userId
     * @param state
     * @return
     */
    PersonScoreDTO sumScoreAndRank(Integer userId, Integer state);

    /**
     * 价值分明细查询
     *
     * @param userId
     * @param state
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<PersonScoreDetailsDTO> selectScoreDetails(Integer userId, Integer state, Integer pageNum,
            Integer pageSize);

    /**
     * 增加个人分表数据
     *
     * @param personscore
     * @return
     */
    int insert(PersonScore personscore);

    /**
     * 根据主键id删除
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 根据id更新价值表数据
     *
     * @param personScore
     * @return
     */
    int updateByPrimaryKeySelective(PersonScore personScore);
}
