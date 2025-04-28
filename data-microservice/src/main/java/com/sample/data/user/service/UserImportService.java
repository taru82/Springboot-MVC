package com.sample.data.user.service;

import com.sample.data.user.dao.UserDao;
import com.sample.data.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class UserImportService {

    @Autowired
    UserDao userDao;
    @Autowired
    BatchUserService batchUserService;
    @Autowired
    ResourceLoader resourceLoader;

    public String batchInsertUsers() {
        CompletableFuture.runAsync(() -> {
            try {
                processUserData();
            } catch (IOException e) {
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

    private void processUserData() throws IOException {

        Resource[] path = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).
                getResources("classpath*:**/user/**/*.csv");
        InputStream inputStream = path[0].getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        List<User> userList = parseCSV(bufferedReader);
        log.info("Total users in the CSV: " + userList.size());

        // Batch Insert the user data into the database
        batchUserService.processBulkUser(userList);

    }

    private List<User> parseCSV(BufferedReader bufferedReader) {
        List<User> userList = new ArrayList<>();
        CSVParser parser = null;
        try {
            parser = new CSVParser(bufferedReader, CSVFormat.DEFAULT.withHeader());
            for (CSVRecord record : parser) {
                User user = new User();
                user.setFirstName(record.get("FIRST_NAME"));
                user.setLastName(record.get("LAST_NAME"));
                user.setCountry(record.get("COUNTRY"));
                user.setStreet(record.get("STREET"));
                user.setCity(record.get("CITY"));
                user.setState(record.get("STATE"));
                user.setZipcode(record.get("ZIP_CODE"));
                user.setCreatedBy("System");
                user.setModifiedBy("System");
                userList.add(user);
            }
            parser.close();
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException("IOException is parsing the CSV: " + e.getMessage(), e);
        }
        return userList;
    }

}
