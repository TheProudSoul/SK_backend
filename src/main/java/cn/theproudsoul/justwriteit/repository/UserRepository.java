package cn.theproudsoul.justwriteit.repository;

import cn.theproudsoul.justwriteit.model.UserModel;
import org.apache.ibatis.annotations.*;

/**
 * @author TheProudSoul
 */
@Mapper
public interface UserRepository {

    @Select("SELECT id FROM user where email= #{email}")
    Long findByEmail(String email);

    @Select("SELECT id id,username username,password password,email,email FROM user where username= #{username}")
    UserModel findByUsername(String username);

    @Insert("INSERT IGNORE INTO user (username, email, password) VALUES (#{model.username}, #{model.email}, #{model.password})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(@Param("model") UserModel model);

}
