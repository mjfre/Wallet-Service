package com.website.WalletService.service;

import com.website.WalletService.repository.WalletRecordRepository;
import com.website.WalletService.repository.dto.ThorWalletRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WalletRecordService {

    private final WalletRecordRepository walletRecordRepository;

    public WalletRecordService(WalletRecordRepository walletRecordRepository) {
        this.walletRecordRepository = walletRecordRepository;
    }

    //CREATE

    public int addThorWalletAddress(String thorWalletAddress) {
        ThorWalletRecord thorWalletRecord = new ThorWalletRecord(thorWalletAddress);
        return walletRecordRepository.insertThorWalletRecord(thorWalletRecord);
    }


    public int addThorWalletAddresses(List<String> thorWalletAddresses) {
        List<ThorWalletRecord> thorWalletRecords =
                thorWalletAddresses.stream().map(ThorWalletRecord::new).collect(Collectors.toList());

        int numInserted = 0;
        for (ThorWalletRecord thorWalletRecord : thorWalletRecords) {
            numInserted += walletRecordRepository.insertThorWalletRecord(thorWalletRecord);
        }

        return numInserted;
    }

    //READ

    public List<ThorWalletRecord> getAllThorWalletRecords() {
        return walletRecordRepository.selectAllThorWalletRecords();
    }

    public String getThorWalletAddress(UUID thorWalletRecordId) {
        return walletRecordRepository.selectThorWalletAddress(thorWalletRecordId);
    }

    //UPDATE

    public UUID assignTerraAddressToThorAddress(String terraWalletAddress) {
        //Check if terra wallet address was previously submitted
        if (walletRecordRepository.existsThorWalletRecord(terraWalletAddress)) {
            return walletRecordRepository.selectThorWalletRecordId(terraWalletAddress);
        }

        //Assign a thor wallet address to a new terra wallet address
        String unusedThorWalletAddress = walletRecordRepository.selectUnusedThorWalletAddress();
        System.out.println(unusedThorWalletAddress);
        //If the terra wallet address is assigned to a thor wallet address
        walletRecordRepository.updateThorWalletRecord(unusedThorWalletAddress, terraWalletAddress);
        //Return the id for that record
        return walletRecordRepository.selectThorWalletRecordId(terraWalletAddress);
    }

    //DELETE

    public int deleteThorWalletRecord(UUID thorWalletRecordId) {
        return walletRecordRepository.deleteThorWalletRecord(thorWalletRecordId);
    }

}
