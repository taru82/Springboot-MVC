package com.sample.data.user.service;

import com.sample.data.user.mapper.sampledb.UserMapper;
import com.sample.data.user.model.UserUpdate;
import com.sample.data.util.CsvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class UserExportService {

    private static final String FILE_NAME = "OUTPUT_USER_DATA.csv";

    @Autowired
    UserMapper userMapper;
    @Autowired
    ResourceLoader resourceLoader;

    public String batchDownloadUsers() {
        CompletableFuture.runAsync(() -> {
            try {
                processUserData();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("User Feed Job Failed", exception);
            } else {
                log.info("User Feed Job Success");
            }
        });
        return "Success";
    }

    public void processUserData() throws Exception {
        List<UserUpdate> userUpdateList = userMapper.selectAllUsers();
        CsvUtil.writeCsvFromBean(userUpdateList, UserUpdate.class, FILE_NAME);
    }
}
