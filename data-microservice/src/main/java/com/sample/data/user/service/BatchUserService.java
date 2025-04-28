package com.sample.data.user.service;


import com.sample.data.user.dao.UserDao;
import com.sample.data.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BatchUserService {

    private static final int MAX_THREAD_COUNT = 10;
    private static final int MAX_USERS_PER_THREAD = 5;
    @Autowired
    UserDao userDao;
    ExecutorService executor = Executors.newFixedThreadPool(MAX_THREAD_COUNT);

    public void processBulkUser(List<User> userList) {

        List<Callable<Void>> callables = callCallable(userList);

        batchProcessBulkUser(callables);

    }

    public void batchProcessBulkUser(List<Callable<Void>> callables) {
        try {
            executor.invokeAll(callables);
        } catch (InterruptedException e) {
            log.error("Error processing bulk user", e);
        } finally {
            log.info("completed processing bulk users");
        }
    }


    public List<Callable<Void>> callCallable(List<User> userList) {
        List<List<User>> partitionUserLists = ListUtils.partition(userList, MAX_USERS_PER_THREAD);
        return partitionUserLists.stream().map(ulist -> (Callable<Void>) () -> {
            try {
                userDao.saveUsers(ulist);
            } catch (Exception e) {
                log.error("Error saving user", e);
            }
            return null;
        }).collect(Collectors.toList());
    }


}
