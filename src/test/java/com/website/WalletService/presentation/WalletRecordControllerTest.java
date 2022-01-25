package com.website.WalletService.presentation;

import com.website.WalletService.domain.ApplicationUserDO;
import com.website.WalletService.repository.dto.ApplicationUser;
import com.website.WalletService.repository.dto.ThorWalletRecord;
import com.website.WalletService.security.ApplicationUserRole;
import com.website.WalletService.service.WalletRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class WalletRecordControllerTest {

    WalletRecordController walletRecordController;

    @Mock
    WalletRecordService walletRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        walletRecordController = new WalletRecordController(walletRecordService);
    }

    @Test
    void whenAddThorWalletAddress_thenReturnNumRowsInserted() {
        //given
        String thorWalletAddress = "THOR_WALLET_ADDRESS";
        int expectedRowsChanged = 1;

        when(walletRecordService.addThorWalletAddress(thorWalletAddress))
                .thenReturn(expectedRowsChanged);

        //when
        int actualRowsChanged = walletRecordController.addThorWalletAddress(thorWalletAddress);

        //then
        assertEquals(expectedRowsChanged, actualRowsChanged);
    }

    @Test
    void whenAddThorWalletAddresses_thenReturnNumRowsInserted() {
        //given
        List<String> thorWalletAddresses = List.of("THOR_WALLET_ADDRESS1", "THOR_WALLET_ADDRESS");
        int expectedRowsChanged = 2;

        when(walletRecordService.addThorWalletAddresses(thorWalletAddresses))
                .thenReturn(expectedRowsChanged);

        //when
        int actualRowsChanged = walletRecordController.addThorWalletAddresses(thorWalletAddresses);

        //then
        assertEquals(expectedRowsChanged, actualRowsChanged);
    }

    @Test
    void whenGetAllThorWalletRecords_thenReturnListOfRecords() {
        //given
        List<ThorWalletRecord> expectedThorWalletRecords = List.of(
                new ThorWalletRecord(
                        UUID.randomUUID(),
                        "THOR_WALLET_ADDRESS",
                        "TERRA_WALLET_ADDRESS"
                )
        );

        when(walletRecordService.getAllThorWalletRecords())
                .thenReturn(expectedThorWalletRecords);

        //when
        List<ThorWalletRecord> actualThorWalletRecords = walletRecordController.getAllThorWalletRecords();

        //then
        assertEquals(expectedThorWalletRecords, actualThorWalletRecords);
    }

    @Test
    void whenAssignTerraAddressToThorAddress_thenReturn() {
        //given
        String terraWalletAddress = "TERRA_WALLET_ADDRESS";
        UUID expectedThorWalletRecordId = UUID.randomUUID();

        when(walletRecordService.assignTerraAddressToThorAddress(terraWalletAddress))
                .thenReturn(expectedThorWalletRecordId);

        //when
        UUID actualThorWalletRecordId = walletRecordController.assignTerraAddressToThorAddress(terraWalletAddress);

        //then
        assertEquals(expectedThorWalletRecordId, actualThorWalletRecordId);
    }

    @Test
    void whenDeleteThorWalletRecord_thenReturnNumRowsChanged() {
//        //given
//        UUID thorWalletRecordId = UUID.randomUUID();
//        int expectedNumRowsChanged = 1;
//
//        when(walletRecordService.deleteThorWalletRecord(thorWalletRecordId))
//                .thenReturn(expectedNumRowsChanged);
//
//        //when
//        int actualNumRowsChanged = walletRecordController.deleteThorWalletRecord(thorWalletRecordId);
//
//        //then
//        assertEquals(expectedNumRowsChanged, actualNumRowsChanged);
    }
}
