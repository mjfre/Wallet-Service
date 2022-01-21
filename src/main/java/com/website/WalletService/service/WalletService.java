package com.website.WalletService.service;

import com.website.WalletService.exception.ApiRequestException;
import com.website.WalletService.repository.WalletRepository;
import com.website.WalletService.repository.dto.ThorWalletRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    //CREATE

    public int addThorWalletAddress(String thorWalletAddress) {
        ThorWalletRecord thorWalletRecord = new ThorWalletRecord(thorWalletAddress);
        return walletRepository.insertThorWalletRecord(thorWalletRecord);
    }


    public int addThorWalletAddresses(List<String> thorWalletAddresses) {
        List<ThorWalletRecord> thorWalletRecords =
                thorWalletAddresses.stream().map(ThorWalletRecord::new).collect(Collectors.toList());

        int numInserted = 0;
        for (ThorWalletRecord thorWalletRecord : thorWalletRecords) {
            numInserted += walletRepository.insertThorWalletRecord(thorWalletRecord);
        }

        return numInserted;
    }

    //READ

    public List<ThorWalletRecord> getAllThorWalletRecords() {
        return walletRepository.selectAllThorWalletRecords();
    }


    public UUID assignTerraAddressToThorAddress(String terraWalletAddress) {
        //Check if terra wallet address was previously submitted
        if (walletRepository.existsThorWalletRecord(terraWalletAddress)) {
            return walletRepository.selectThorWalletRecordId(terraWalletAddress);
        }

        //Assign a thor wallet address to a new terra wallet address
        String unusedThorWalletAddress = walletRepository.selectUnusedThorWalletAddress();

        //If the terra wallet address is assigned to a thor wallet address
        if (walletRepository.updateThorWalletRecord(unusedThorWalletAddress, terraWalletAddress) == 1) {
            //Return the id for that record
            return walletRepository.selectThorWalletRecordId(terraWalletAddress);
        } else {
            throw new ApiRequestException("There are no Thor wallet address let to assign");
        }
    }

    public int deleteThorWalletRecord(String thorWalletRecordId) {
        return walletRepository.deleteThorWalletRecord(thorWalletRecordId);
    }

}
