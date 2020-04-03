package cn.theproudsoul.justwriteit.repository;

import cn.theproudsoul.justwriteit.model.ImageMetadataModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author TheProudSoul
 */
@Mapper
public interface ImageMetadataRepository {
    @Insert("INSERT IGNORE INTO image_metadata (user_id, image_url, origin_name) VALUES (#{model.userId}, #{model.imageUrl}, #{model.originName})")
    int insert(@Param("model") ImageMetadataModel model);

    @Select("SELECT image_url FROM image_metadata where user_id= #{userId}")
    List<String> getUserImageList(long userId);

    @Select("SELECT id id, user_id userId, image_url imageUrl FROM image_metadata where id= #{id}")
    ImageMetadataModel getById(long id);

    @Delete("DELETE FROM image_metadata where user_id= #{userId}")
    int deleteAll(long userId);

    @Delete("DELETE FROM image_metadata where id= #{id}")
    int deleteOne(long id);
}
