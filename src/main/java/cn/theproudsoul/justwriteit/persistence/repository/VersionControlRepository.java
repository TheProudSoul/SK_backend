package cn.theproudsoul.justwriteit.persistence.repository;

import cn.theproudsoul.justwriteit.persistence.model.VersionControlModel;
import cn.theproudsoul.justwriteit.web.vo.VersionControlVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author TheProudSoul
 */
@Mapper
public interface VersionControlRepository {
    /**
     * 插入一行记录
     */
    @Insert("INSERT IGNORE INTO version_control (id,user_id,name) VALUES (#{model.id}, #{model.userId}, #{model.name})")
    int insert(@Param("model") VersionControlModel model);

    @Select("SELECT id id, name name, update_time time FROM version_control where user_id= #{userId}")
    List<VersionControlVo> getUserVersionControlList(@Param("userId") long userId);

    @Select("SELECT name FROM version_control where id = #{id}")
    String getNameById(long id);

    @Select("SELECT user_id FROM version_control where id = #{id}")
    long getUserIdById(long id);

    @Delete("delete from version_control where id = #{id}")
    void delete(long id);

    @Select("SELECT COUNT(*) FROM version_control where user_id = #{user_id}")
    int getCountByUserId(long userId);
}
