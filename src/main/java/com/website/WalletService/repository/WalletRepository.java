package com.website.WalletService.repository;

import com.website.WalletService.repository.dto.ThorWalletRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class WalletRepository {

    private final JdbcTemplate jdbcTemplate;

    final String INSERT_THOR_WALLET_RECORD_SQL = "" +
            "INSERT INTO thor_wallet_record (" +
            " id, " +
            " thor_wallet_address," +
            " terra_wallet_address)" +
            "VALUES (?, ?, ?)";


    final String SELECT_ALL_THOR_WALLET_RECORDS_SQL = "SELECT * FROM student";

    final static String EXISTS_BY_TERRA_WALLET_ADDRESS_SQL = "SELECT EXISTS ( SELECT 1 FROM thor_wallet_record WHERE terra_wallet_address = ? )";

    final static String SELECT_ID_BY_TERRA_WALLET_ADDRESS_SQL = "SELECT id FROM thor_wallet_record WHERE terra_wallet_address = ?";

    final static String SELECT_UNUSED_THOR_ADDRESS_SQL = "SELECT thor_wallet_address FROM thor_wallet_record WHERE terra_wallet_address = null LIMIT 1";

    final String UPDATE_TERRA_WALLET_ADDRESS_BY_THOR_WALLET_ADDRESS_SQL = "" +
            "UPDATE thor_wallet_record " +
            "SET terra_wallet_address = ? " +
            "WHERE thor_wallet_address = ?";

    final static String DELETE_THOR_WALLET_RECORD_BY_ID_SQL = "DELETE FROM thor_wallet_record WHERE id = ?";

    public WalletRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //CREATE

    public int insertThorWalletRecord(ThorWalletRecord thorWalletRecord) {
        return jdbcTemplate.update(
                INSERT_THOR_WALLET_RECORD_SQL,
                thorWalletRecord.getId(),
                thorWalletRecord.getThorWalletAddress(),
                thorWalletRecord.getTerraWalletAddress()
        );
    }

    //READ

    public List<ThorWalletRecord> selectAllThorWalletRecords() {
        return jdbcTemplate.query(SELECT_ALL_THOR_WALLET_RECORDS_SQL, mapThorWalletRecordFromDb());
    }

    public Boolean existsThorWalletRecord(String terraWalletAddress) {
        return jdbcTemplate.queryForObject(
                EXISTS_BY_TERRA_WALLET_ADDRESS_SQL,
                (resultSet, columnIndex) -> resultSet.getBoolean(1),
                terraWalletAddress
        );
    }

    public UUID selectThorWalletRecordId(String terraWalletAddress) {
        return jdbcTemplate.queryForObject(SELECT_ID_BY_TERRA_WALLET_ADDRESS_SQL, mapIdFromDb(), terraWalletAddress);
    }

    public String selectUnusedThorWalletAddress() {
        return jdbcTemplate.queryForObject(SELECT_UNUSED_THOR_ADDRESS_SQL, mapThorWalletAddressFromDb());
    }

    //UPDATE

    public int updateThorWalletRecord(String unusedThorWalletAddress, String terraWalletAddress) {
        return jdbcTemplate.update(
                UPDATE_TERRA_WALLET_ADDRESS_BY_THOR_WALLET_ADDRESS_SQL,
                terraWalletAddress,
                unusedThorWalletAddress);
    }


    //ROWMAPPER
    RowMapper<ThorWalletRecord> mapThorWalletRecordFromDb() {
        return (resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            String thorWalletAddress = resultSet.getString("thor_wallet_address");
            String terraWalletAddress = resultSet.getString("terra_wallet_address");

            return new ThorWalletRecord(
                    id,
                    thorWalletAddress,
                    terraWalletAddress
            );
        };
    }

    RowMapper<UUID> mapIdFromDb() {
        return (resultSet, i) -> {
            return UUID.fromString(resultSet.getString("id"));
        };
    }

    RowMapper<String> mapThorWalletAddressFromDb() {
        return (resultSet, i) -> {
            return resultSet.getString("thor_wallet_address");
        };
    }

    public int deleteThorWalletRecord(String thorWalletRecordId) {
        return jdbcTemplate.update(DELETE_THOR_WALLET_RECORD_BY_ID_SQL, thorWalletRecordId);
    }

}