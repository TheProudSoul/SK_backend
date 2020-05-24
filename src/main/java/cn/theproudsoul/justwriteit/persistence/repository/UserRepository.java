package cn.theproudsoul.justwriteit.persistence.repository;

import cn.theproudsoul.justwriteit.persistence.model.UserModel;
import org.apache.ibatis.annotations.*;

/**
 * @author TheProudSoul
 */
@Mapper
public interface UserRepository {

//    @Select("SELECT id FROM user where email= #{email}")
//    Long getIdByEmail(String email);

    @Select("SELECT id id, username username,password password, email,email FROM user where email= #{email}")
    UserModel findByEmail(String email);

    @Select("SELECT id id, username username, email,email FROM user where id= #{id}")
    UserModel findByUserId(long id);

    @Insert("INSERT IGNORE INTO user (username, email, password) VALUES (#{model.username}, #{model.email}, #{model.password})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(@Param("model") UserModel model);

}
