package com.website.WalletService.presentation;

import com.website.WalletService.repository.dto.ThorWalletRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "/wallet-record", consumes = "application/json", produces = "application/json", tags={"Wallet Records"})
@RestController
@RequestMapping("wallet-record")
@CrossOrigin
public class WalletRecordController {

//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
//    @ApiOperation(value = "Get all Thor wallet records", response = ThorWalletRecord.class, responseContainer="List", notes="Permitted user roles: ADMIN")
//    @GetMapping
//    public List<ThorWalletRecord> getAllStudents() {
//        return studentService.getAllStudents();
//    }

}
