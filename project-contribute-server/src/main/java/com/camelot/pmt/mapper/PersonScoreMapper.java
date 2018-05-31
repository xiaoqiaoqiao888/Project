package com.camelot.pmt.mapper;

import com.camelot.pmt.model.PersonScore;
import com.camelot.pmt.model.PersonScoreDTO;
import com.camelot.pmt.model.PersonScoreDetailsDTO;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PersonScoreMapper {
    /**
     * 价值分总分以及排名查询
     *
     * @param userId
     * @param state
     * @return
     */
    PersonScoreDTO sumScoreAndRank(@Param("userId") Integer userId, @Param("state") Integer state);

    /**
     * 价值分明细查询
     *
     * @param userId
     * @return
     */
    List<PersonScoreDetailsDTO> selectScoreDetails(@Param("userId") Integer userId, @Param("state") Integer state);

    /**
     * 根据主键id删除
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 增加个人分表数据
     *
     * @param personscore
     * @return
     */
    int insert(PersonScore personscore);

    /**
     * @mbggenerated
     */
    int insertSelective(PersonScore record);

    /**
     * @mbggenerated
     */
    PersonScore selectByPrimaryKey(Integer id);

    /**
     * 根据id更新价值表数据
     *
     * @param personScore
     * @return
     */
    int updateByPrimaryKeySelective(PersonScore personScore);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKey(PersonScore record);
}