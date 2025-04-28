package com.sample.data.user.resource;

import com.sample.data.user.service.UserExportService;
import com.sample.data.user.service.UserImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Tag(name = "[Data] User", description = "User REST Service(s)")
@RestController
@RequestMapping("/com/sample/data/user")
public class UserResource {

    @Autowired
    private UserImportService createUserService;
    @Autowired
    private UserExportService uploadUserService;

    @Operation(summary = "Batch Insert Bulk Users from CSV file")
    @RequestMapping(method = RequestMethod.POST, path = "/import", produces = APPLICATION_JSON_VALUE)
    public String batchInsertUsers() {
        return createUserService.batchInsertUsers();
    }

    @Operation(summary = "Upload Bulk Users From DB to CSV file")
    @RequestMapping(method = RequestMethod.POST, path = "/export", produces = APPLICATION_JSON_VALUE)
    public String batchUploadUsers() {
        return uploadUserService.batchDownloadUsers();
    }

}
