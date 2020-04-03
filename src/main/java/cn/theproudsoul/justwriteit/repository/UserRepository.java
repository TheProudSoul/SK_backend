package cn.theproudsoul.justwriteit.repository;

import cn.theproudsoul.justwriteit.model.UserModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author TheProudSoul
 */
@Mapper
public interface UserRepository {
    @Select("SELECT id FROM user where email= #{email}")
    Long findByEmail(String email);

    @Insert("INSERT IGNORE INTO user (username, email, password) VALUES (#{model.username}, #{model.email}, #{model.password})")
    int save(@Param("model") UserModel model);
}
