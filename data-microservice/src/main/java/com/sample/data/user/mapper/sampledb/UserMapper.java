package com.sample.data.user.mapper.sampledb;

import com.sample.data.user.model.User;
import com.sample.data.user.model.UserUpdate;
import org.apache.ibatis.annotations.Flush;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    void insertUser(User user);

    List<UserUpdate> selectAllUsers();

    @Flush
    List flush();


}
