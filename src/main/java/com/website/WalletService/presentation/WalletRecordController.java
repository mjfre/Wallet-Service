package com.website.WalletService.presentation;

import com.website.WalletService.repository.dto.ThorWalletRecord;
import com.website.WalletService.service.WalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Api(value = "/wallet-record", consumes = "application/json", produces = "application/json", tags={"Thor Wallet Records"})
@RestController
@RequestMapping("wallet-record")
@CrossOrigin
public class WalletRecordController {

    private final WalletService walletService;

    public WalletRecordController(WalletService walletService) {
        this.walletService = walletService;
    }

    //CREATE

    //ADD A RECORD
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ApiOperation(value = "Add thor wallet address", notes="Permitted user roles: ADMIN")
    @PostMapping
    public int addApplicationUser(@RequestBody String thorWalletAddress){
        return walletService.addThorWalletAddress(thorWalletAddress);
    }

    //ADD A LIST OF RECORDS
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ApiOperation(value = "Add list of thor wallet addresses", notes="Permitted user roles: ADMIN")
    @PostMapping(path = "/from/teachers")
    public int addTeachersAsApplicationUsers(@RequestBody List<String> thorWalletAddresses){
        return walletService.addThorWalletAddresses(thorWalletAddresses);
    }

    //READ

    //GET ALL THOR WALLET RECORDS
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ApiOperation(value = "Get all Thor wallet records", response = ThorWalletRecord.class, responseContainer="List", notes="Permitted user roles: ADMIN")
    @GetMapping
    public List<ThorWalletRecord> getAllThorWalletRecords() {
        return walletService.getAllThorWalletRecords();
    }

    //UPDATE

    //ASSIGN A TERRA WALLET TO A THOR WALLET
    //returns extant id if terra address already used
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Assign Terra address to Thor address - returns extant record if terra address has previously been provided", response = ThorWalletRecord.class, notes="Permitted user roles: Permit All")
    @GetMapping
    public UUID assignTerraAddressToThorAddress(@RequestBody String terraWalletAddress) {
        return walletService.assignTerraAddressToThorAddress(terraWalletAddress);
    }

    //DELETE

    //DELETE THOR WALLET RECORD
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Delete a Thor wallet record", notes="Permitted user roles: ADMIN, SUPERUSER")
    @DeleteMapping(path = "/{thorWalletRecordId}")
    public int deleteSurveyEvent(@PathVariable String thorWalletRecordId) {
        return walletService.deleteThorWalletRecord(thorWalletRecordId);
    }

}
