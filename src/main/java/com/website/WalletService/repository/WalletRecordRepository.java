package com.website.WalletService.repository;

import com.website.WalletService.exception.ApiRequestException;
import com.website.WalletService.repository.dto.ThorWalletRecord;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class WalletRecordRepository {

    private final JdbcTemplate jdbcTemplate;

    static final String INSERT_THOR_WALLET_RECORD_SQL = "" +
            "INSERT INTO thor_wallet_record (" +
            " id, " +
            " thor_wallet_address," +
            " terra_wallet_address)" +
            "VALUES (?, ?, ?)";


    static final String SELECT_ALL_THOR_WALLET_RECORDS_SQL = "SELECT * FROM thor_wallet_record";

    static final String EXISTS_BY_TERRA_WALLET_ADDRESS_SQL = "SELECT EXISTS ( SELECT 1 FROM thor_wallet_record WHERE terra_wallet_address = ? )";

    static final String SELECT_ID_BY_TERRA_WALLET_ADDRESS_SQL = "SELECT id FROM thor_wallet_record WHERE terra_wallet_address = ?";

    static final String SELECT_UNUSED_THOR_ADDRESS_SQL = "SELECT thor_wallet_address FROM thor_wallet_record WHERE terra_wallet_address = null LIMIT 1";

    static final String UPDATE_TERRA_WALLET_ADDRESS_BY_THOR_WALLET_ADDRESS_SQL = "" +
            "UPDATE thor_wallet_record " +
            "SET terra_wallet_address = ? " +
            "WHERE thor_wallet_address = ?";

    static final String DELETE_THOR_WALLET_RECORD_BY_ID_SQL = "DELETE FROM thor_wallet_record WHERE id = ?";

    public WalletRecordRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //CREATE

    public int insertThorWalletRecord(ThorWalletRecord thorWalletRecord) {
        try {
            return jdbcTemplate.update(
                    INSERT_THOR_WALLET_RECORD_SQL,
                    thorWalletRecord.getId(),
                    thorWalletRecord.getThorWalletAddress(),
                    thorWalletRecord.getTerraWalletAddress()
            );
        }
        catch(Exception e){
            throw new ApiRequestException("Wallet address already exists in database");
        }
    }

    //READ

    public List<ThorWalletRecord> selectAllThorWalletRecords() {
        return jdbcTemplate.query(SELECT_ALL_THOR_WALLET_RECORDS_SQL, mapThorWalletRecordFromDb());
    }

    public Boolean existsThorWalletRecord(String terraWalletAddress) {
        return jdbcTemplate.queryForObject(
                EXISTS_BY_TERRA_WALLET_ADDRESS_SQL,
                mapBooleanFromDb(),
                terraWalletAddress
        );
    }

    public UUID selectThorWalletRecordId(String terraWalletAddress) {
        return jdbcTemplate.queryForObject(SELECT_ID_BY_TERRA_WALLET_ADDRESS_SQL, mapIdFromDb(), terraWalletAddress);
    }

    public String selectUnusedThorWalletAddress() {
        try {
            return jdbcTemplate.queryForObject(SELECT_UNUSED_THOR_ADDRESS_SQL, mapThorWalletAddressFromDb());
        }
        catch(EmptyResultDataAccessException e){
            throw new ApiRequestException("There are no unassigned thor wallet addresses remaining");
        }
    }

    //UPDATE

    public int updateThorWalletRecord(String unusedThorWalletAddress, String terraWalletAddress) {
        return jdbcTemplate.update(
                UPDATE_TERRA_WALLET_ADDRESS_BY_THOR_WALLET_ADDRESS_SQL,
                terraWalletAddress,
                unusedThorWalletAddress);
    }

    //DELETE

    public int deleteThorWalletRecord(UUID thorWalletRecordId) {
        return jdbcTemplate.update(DELETE_THOR_WALLET_RECORD_BY_ID_SQL, thorWalletRecordId);
    }

    //ROWMAPPER
    public RowMapper<ThorWalletRecord> mapThorWalletRecordFromDb() {
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

    RowMapper<Boolean> mapBooleanFromDb() {
        return (resultSet, columnIndex) -> resultSet.getBoolean(1);
    }

}
