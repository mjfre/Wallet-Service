package com.website.WalletService.presentation;

import com.website.WalletService.exception.ApiRequestException;
import com.website.WalletService.repository.dto.ThorWalletRecord;
import com.website.WalletService.service.WalletRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Api(value = "/wallet-record", consumes = "application/json", produces = "application/json", tags = {"Thor Wallet Records"})
@RestController
@RequestMapping("wallet-record")
@CrossOrigin
public class WalletRecordController {

    private final WalletRecordService walletRecordService;

    public WalletRecordController(WalletRecordService walletRecordService) {
        this.walletRecordService = walletRecordService;
    }

    //CREATE

    //ADD A RECORD
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ApiOperation(value = "Add thor wallet address", notes = "Permitted user roles: ADMIN")
    @PostMapping
    public int addThorWalletAddress(@RequestBody String thorWalletAddress) {
        return walletRecordService.addThorWalletAddress(thorWalletAddress);
    }

    //ADD A LIST OF RECORDS
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ApiOperation(value = "Add list of thor wallet addresses", notes = "Permitted user roles: ADMIN")
    @PostMapping(path = "/list")
    public int addThorWalletAddresses(@RequestBody List<String> thorWalletAddresses) {
        return walletRecordService.addThorWalletAddresses(thorWalletAddresses);
    }

    //READ

    //GET ALL THOR WALLET RECORDS
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ApiOperation(value = "Get all Thor wallet records", response = ThorWalletRecord.class, responseContainer = "List", notes = "Permitted user roles: ADMIN")
    @GetMapping
    public List<ThorWalletRecord> getAllThorWalletRecords() {
        return walletRecordService.getAllThorWalletRecords();
    }

    //GET THOR WALLET ADDRESS BY RECORD ID
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Get all a Thor wallet address by record id", response = String.class, notes = "Permitted user roles: Permit All")
    @GetMapping("/{thorWalletRecordId}")
    public String getThorWalletAddress(@PathVariable UUID thorWalletRecordId) {
        System.out.println(thorWalletRecordId);
        return walletRecordService.getThorWalletAddress(thorWalletRecordId);
    }

    //UPDATE

    //ASSIGN A TERRA WALLET TO A THOR WALLET
    //returns extant id if terra address already used
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Assign Terra address to Thor address - returns extant record if terra address has previously been provided", response = ThorWalletRecord.class, notes = "Permitted user roles: Permit All")
    @PutMapping
    public UUID assignTerraAddressToThorAddress(@RequestBody String terraWalletAddress) {
        return walletRecordService.assignTerraAddressToThorAddress(terraWalletAddress);
    }

    //DELETE

    //DELETE THOR WALLET RECORD
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ApiOperation(value = "Delete a Thor wallet record", notes = "Permitted user roles: ADMIN - This endpoint has been disabled in the code. It can be re-enabled in WalletRecordController.java")
    @DeleteMapping(path = "/{thorWalletRecordId}")
    public int deleteThorWalletRecord(@PathVariable UUID thorWalletRecordId) {
        throw new ApiRequestException("This endpoint has been disabled");
        //return walletRecordService.deleteThorWalletRecord(thorWalletRecordId);
    }

}
