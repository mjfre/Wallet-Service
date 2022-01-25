package com.website.WalletService.repository;

import com.website.WalletService.exception.ApiRequestException;
import com.website.WalletService.repository.dto.ApplicationUser;
import com.website.WalletService.repository.dto.ThorWalletRecord;
import com.website.WalletService.security.ApplicationUserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class WalletRecordRepositoryTest {

    private WalletRecordRepository walletRecordRepository;

    @Mock
    JdbcTemplate jdbcTemplate;

    @Mock
    ResultSet resultSet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        walletRecordRepository = new WalletRecordRepository(jdbcTemplate);
    }

    @Test
    void whenInsertThorWalletRecord_thenReturnNumRowsChanged() {
        //given
        ThorWalletRecord thorWalletRecord = new ThorWalletRecord(
                UUID.randomUUID(),
                "THOR_WALLET_ADDRESS",
                "TERRA_WALLET_ADDRESS"
        );
        int expectedRowsChanged = 1;

        when(jdbcTemplate.update(WalletRecordRepository.INSERT_THOR_WALLET_RECORD_SQL,
                thorWalletRecord.getId(),
                thorWalletRecord.getThorWalletAddress(),
                thorWalletRecord.getTerraWalletAddress()))
                .thenReturn(expectedRowsChanged);

        //when
        int actualRowsChanged = walletRecordRepository.insertThorWalletRecord(thorWalletRecord);

        //then
        assertEquals(expectedRowsChanged, actualRowsChanged);
    }

    @Test
    void givenAddressAlreadyExistsInDb_whenInsertThorWalletRecord_thenReturnNumRowsChanged() {
        //given
        ThorWalletRecord thorWalletRecord = new ThorWalletRecord(
                UUID.randomUUID(),
                "THOR_WALLET_ADDRESS",
                "TERRA_WALLET_ADDRESS"
        );

        when(jdbcTemplate.update(WalletRecordRepository.INSERT_THOR_WALLET_RECORD_SQL,
                thorWalletRecord.getId(),
                thorWalletRecord.getThorWalletAddress(),
                thorWalletRecord.getTerraWalletAddress()))
                .thenThrow(new RuntimeException("Wallet address already exists in database"));

        //when
        //then
        Throwable exceptionThrown = assertThrows(RuntimeException.class, () ->  walletRecordRepository.insertThorWalletRecord(thorWalletRecord));
        assertEquals("Wallet address already exists in database", exceptionThrown.getMessage());
    }

    @Test
    void whenSelectAllThorWalletRecords_thenReturnThorWalletRecords() {
        //given
        List<ThorWalletRecord> expectedThorWalletRecords =
                List.of(
                        new ThorWalletRecord(
                                UUID.randomUUID(),
                                "THOR_WALLET_ADDRESS1",
                                "TERRA_WALLET_ADDRESS2"
                        ),
                        new ThorWalletRecord(
                                UUID.randomUUID(),
                                "THOR_WALLET_ADDRESS2",
                                "TERRA_WALLET_ADDRESS2"
                        )
                );

        when(jdbcTemplate.query(WalletRecordRepository.SELECT_ALL_THOR_WALLET_RECORDS_SQL, walletRecordRepository.mapThorWalletRecordFromDb()))
                .thenReturn(expectedThorWalletRecords);

        //when
        List<ThorWalletRecord> actualThorWalletRecords = walletRecordRepository.selectAllThorWalletRecords();

        //then
        assertEquals(expectedThorWalletRecords, actualThorWalletRecords);
    }

    @Test
    void whenExistsThorWalletRecord_thenReturnTrue() {
        //given
        String terraWalletAddress = "TERRA_WALLET_ADDRESS";
        Boolean expectedExists = true;

        when(jdbcTemplate.queryForObject(
                WalletRecordRepository.EXISTS_BY_TERRA_WALLET_ADDRESS_SQL,
                walletRecordRepository.mapBooleanFromDb(),
                terraWalletAddress
        ))
                .thenReturn(expectedExists);

        //when
        Boolean actualExists = walletRecordRepository.existsThorWalletRecord(terraWalletAddress);

        //then
        assertEquals(expectedExists, actualExists);
    }

    @Test
    void whenSelectThorWalletRecordId_thenReturnId() {
        //given
        String terraWalletAddress = "TERRA_WALLET_ADDRESS";
        UUID expectedId = UUID.randomUUID();

        when(jdbcTemplate.queryForObject(WalletRecordRepository.SELECT_ID_BY_TERRA_WALLET_ADDRESS_SQL, walletRecordRepository.mapIdFromDb(), terraWalletAddress))
                .thenReturn(expectedId);

        //when
        UUID actualId = walletRecordRepository.selectThorWalletRecordId(terraWalletAddress);

        //then
        assertEquals(expectedId, actualId);
    }

    @Test
    void whenSelectUnusedThorWalletAddress_thenReturnUnusedThorWalletAddress() {
        //given
        String expectedUnusedThorWalletAddress = "THOR_WALLET_ADDRESS";

        when(jdbcTemplate.queryForObject(WalletRecordRepository.SELECT_UNUSED_THOR_ADDRESS_SQL, walletRecordRepository.mapThorWalletAddressFromDb()))
                .thenReturn(expectedUnusedThorWalletAddress);

        //when
        String actualUnusedThorWalletAddress = walletRecordRepository.selectUnusedThorWalletAddress();

        //then
        assertEquals(expectedUnusedThorWalletAddress, actualUnusedThorWalletAddress);
    }

    @Test
    void givenNoAvailableThorAddresses_whenSelectUnusedThorWalletAddress_thenThrowException() {
        //given
        String expectedUnusedThorWalletAddress = "THOR_WALLET_ADDRESS";

        when(jdbcTemplate.queryForObject(WalletRecordRepository.SELECT_UNUSED_THOR_ADDRESS_SQL, walletRecordRepository.mapThorWalletAddressFromDb()))
                .thenThrow(new EmptyResultDataAccessException(1));

        //when
        //then
        Throwable exceptionThrown = assertThrows(ApiRequestException.class, () -> walletRecordRepository.selectUnusedThorWalletAddress());
        assertEquals(exceptionThrown.getMessage(), "There are no unassigned thor wallet addresses remaining");
    }

    @Test
    void whenUpdateThorWalletRecord_thenReturnNumRowsChanged() {
        //given
        String thorWalletAddress = "THOR_WALLET_ADDRESS";
        String terraWalletAddress = "TERRA_WALLET_ADDRESS";
        int expectedNumRowsChanged = 1;

        when(jdbcTemplate.update(
                WalletRecordRepository.UPDATE_TERRA_WALLET_ADDRESS_BY_THOR_WALLET_ADDRESS_SQL,
                terraWalletAddress,
                thorWalletAddress))
                .thenReturn(expectedNumRowsChanged);

        //when
        int actualNumRowsChanged = walletRecordRepository.updateThorWalletRecord(thorWalletAddress, terraWalletAddress);

        //then
        assertEquals(expectedNumRowsChanged, actualNumRowsChanged);
    }

    @Test
    void whenDeleteThorWalletRecord_thenReturn() {
        //given
        UUID thorWalletRecordId = UUID.randomUUID();
        int expectedNumRowsChanged = 1;

        when(jdbcTemplate.update(WalletRecordRepository.DELETE_THOR_WALLET_RECORD_BY_ID_SQL, thorWalletRecordId))
                .thenReturn(expectedNumRowsChanged);

        //when
        int actualNumRowsChanged = walletRecordRepository.deleteThorWalletRecord(thorWalletRecordId);

        //then
        assertEquals(expectedNumRowsChanged, actualNumRowsChanged);
    }

    @Test
    void whenMapThorWalletRecordFromDb_thenReturn() throws SQLException {
        //given
        String[] keys = {"id", "thor_wallet_address", "terra_wallet_address",};
        String[] values = {"503f7354-7a86-11ec-90d6-0242ac120003", "THOR_WALLET_ADDRESS", "TERRA_WALLET_ADDRESS"};

        ThorWalletRecord expectedThorWalletRecord = new ThorWalletRecord(
                UUID.fromString(values[0]),
                values[1],
                values[2]
        );

        for (int i = 0; i < keys.length; i++) {
            when(resultSet.getString(keys[i])).thenReturn(values[i]);
            when(resultSet.getBoolean(keys[i])).thenReturn(Boolean.valueOf(values[i]));
        }

        //when
        RowMapper<ThorWalletRecord> mapper = walletRecordRepository.mapThorWalletRecordFromDb();
        ThorWalletRecord actualThrowWalletRecord = mapper.mapRow(resultSet, 0);

        //then
        assertEquals(expectedThorWalletRecord, actualThrowWalletRecord);
    }

    @Test
    void whenMapIdFromDb_thenReturnId() throws SQLException {
        //given
        String expectedId = "503f7354-7a86-11ec-90d6-0242ac120003";

        when(resultSet.getString("id")).thenReturn(expectedId);

        //when
        RowMapper<UUID> mapper = walletRecordRepository.mapIdFromDb();
        UUID actualId = mapper.mapRow(resultSet, 0);

        //then
        assertEquals(UUID.fromString(expectedId), actualId);
    }

    @Test
    void whenMapBooleanFromDb_thenReturnBoolean() throws SQLException {
        //given
        Boolean expected = true;

        when(resultSet.getBoolean(1)).thenReturn(expected);

        //when
        RowMapper<Boolean> mapper = walletRecordRepository.mapBooleanFromDb();
        Boolean actual = mapper.mapRow(resultSet, 0);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void whenMapThorWalletAddressFromDb_thenReturnThorWalletAddress() throws SQLException {
        //given
        String expectedThorWalletAddress = "THOR_WALLET_ADDRESS";

        when(resultSet.getString("thor_wallet_address")).thenReturn(expectedThorWalletAddress);

        //when
        RowMapper<String> mapper = walletRecordRepository.mapThorWalletAddressFromDb();
        String actualThorWalletAddress = mapper.mapRow(resultSet, 0);

        //then
        assertEquals(expectedThorWalletAddress, actualThorWalletAddress);
    }

}
