package cn.theproudsoul.justwriteit.repository;

import cn.theproudsoul.justwriteit.model.VersionControlModel;
import cn.theproudsoul.justwriteit.web.vo.VersionControlVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
}
