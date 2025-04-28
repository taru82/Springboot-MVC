package com.sample.data.user.dao;

import com.sample.data.user.mapper.sampledb.UserMapper;
import com.sample.data.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class UserDao {
    int BATCH_SIZE = 5;
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public int saveUsers(List<User> userList) {
        int rowCount = 0;

        if (userList.isEmpty()) {
            return 0;
        }

        try {
            long time = System.currentTimeMillis();
            log.info("saving " + userList.size() + " userList ...");
            for (User row : userList) {
                userMapper.insertUser(row);
                if (rowCount % BATCH_SIZE == 0) {
                    userMapper.flush();
                }
                rowCount++;
            }
            userMapper.flush();
            log.info("saved " + userList.size() + " userList in " + (System.currentTimeMillis() - time) + " milliseconds");
            return userList.size();
        } catch (Exception e) {
            log.error("error saving userList" + userList, e);
            throw e;
        }
    }

}
