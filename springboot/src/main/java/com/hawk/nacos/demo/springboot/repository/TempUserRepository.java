package com.hawk.nacos.demo.springboot.repository;

import com.hawk.nacos.demo.springboot.domian.TempUserDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author Hawk
 */
@Repository
public interface TempUserRepository extends JpaRepository<TempUserDo, Long>, JpaSpecificationExecutor {

    /**
     * 查询没有unionid数据集合
     *
     * @return
     */
    @Query(value = "SELECT * FROM temp_user t WHERE unionid  IS NULL OR unionid = ''", nativeQuery = true)
    List<TempUserDo> findLackUnionIdList();

    /**
     * 查询有unionid数据集合
     *
     * @return
     */
    @Query(value = "SELECT * FROM temp_user t WHERE unionid  IS NOT NULL AND unionid <> ''", nativeQuery = true)
    List<TempUserDo> findUnionIdList();

}
