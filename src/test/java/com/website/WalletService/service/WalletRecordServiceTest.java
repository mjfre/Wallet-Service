package com.website.WalletService.service;

import com.website.WalletService.presentation.ApplicationUserController;
import com.website.WalletService.repository.WalletRecordRepository;
import com.website.WalletService.repository.dto.ThorWalletRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class WalletRecordServiceTest {

    private WalletRecordService walletRecordService;

    @Mock
    WalletRecordRepository walletRecordRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        walletRecordService = new WalletRecordService(walletRecordRepository);
    }

    @Test
    void whenAddThorWalletAddress_thenReturnNumRowsChanges() {
        //given
        String thorWalletAddress = "THOR_WALLET_ADDRESS";
        int expectedNumRowsChanged = 1;

        when(walletRecordRepository.insertThorWalletRecord(any()))
                .thenReturn(expectedNumRowsChanged);

        //when
        int actualNumRowsChanged = walletRecordService.addThorWalletAddress(thorWalletAddress);

        //then
        assertEquals(expectedNumRowsChanged, actualNumRowsChanged);
    }

    @Test
    void whenAddThorWalletAddresses_thenReturnNumRowsChanged() {
        //given
        List<String> thorWalletAddresses = List.of("THOR_WALLET_ADDRESS1","THOR_WALLET_ADDRESS1");
        int expectedNumRowsChanged = 2;

        when(walletRecordRepository.insertThorWalletRecord(any()))
                .thenReturn(1);

        //when
        int actualNumRowsChanged = walletRecordService.addThorWalletAddresses(thorWalletAddresses);

        //then
        assertEquals(expectedNumRowsChanged, actualNumRowsChanged);
    }

    @Test
    void whenGetAllThorWalletRecords_thenReturnThorWalletRecords() {
        //given
        List<ThorWalletRecord> expectedThorWalletRecords =
                List.of(
                        new ThorWalletRecord(),
                        new ThorWalletRecord()
                );

        when(walletRecordRepository.selectAllThorWalletRecords())
                .thenReturn(expectedThorWalletRecords);

        //when
        List<ThorWalletRecord> actualThorWalletRecords = walletRecordService.getAllThorWalletRecords();

        //then
        assertEquals(expectedThorWalletRecords, actualThorWalletRecords);
    }

    @Test
    void whenAssignTerraAddressToThorAddress_thenReturnId() {
        //given
        String terraWalletAddress = "TERRA_WALLET_ADDRESS";
        String unusedThorWalletAddress = "THOR_WALLET_ADDRESS";
        UUID expectedId = UUID.randomUUID();

        when(walletRecordRepository.existsThorWalletRecord(terraWalletAddress))
                .thenReturn(false);
        when(walletRecordRepository.selectUnusedThorWalletAddress())
                .thenReturn(unusedThorWalletAddress);
        when(walletRecordRepository.updateThorWalletRecord(unusedThorWalletAddress, terraWalletAddress))
                .thenReturn(1);
        when(walletRecordRepository.selectThorWalletRecordId(terraWalletAddress))
                .thenReturn(expectedId);

        //when
        UUID actualId = walletRecordService.assignTerraAddressToThorAddress(terraWalletAddress);

        //then
        assertEquals(expectedId, actualId);
    }

    @Test
    void givenTerraAddressAlreadyAssigned_whenAssignTerraAddressToThorAddress_thenReturnPreviouslyAssignedId() {
        //given
        String terraWalletAddress = "TERRA_WALLET_ADDRESS";
        UUID expectedId = UUID.randomUUID();

        when(walletRecordRepository.existsThorWalletRecord(terraWalletAddress))
                .thenReturn(true);
        when(walletRecordRepository.selectThorWalletRecordId(terraWalletAddress))
                .thenReturn(expectedId);

        //when
        UUID actualId = walletRecordService.assignTerraAddressToThorAddress(terraWalletAddress);

        //then
        assertEquals(expectedId, actualId);
    }

    @Test
    void whenDeleteThorWalletRecord_thenReturn() {
        //given
        UUID thorWalletRecordId = UUID.randomUUID();
        int expectedNumRowsChanged = 1;

        when(walletRecordRepository.deleteThorWalletRecord(thorWalletRecordId))
                .thenReturn(expectedNumRowsChanged);
        //when
        int actualNumRowsChanged = walletRecordService.deleteThorWalletRecord(thorWalletRecordId);

        //then
        assertEquals(expectedNumRowsChanged, actualNumRowsChanged);
    }
}
