package com.website.WalletService.repository;

import com.website.WalletService.exception.ApiRequestException;
import com.website.WalletService.repository.dto.ApplicationUser;
import com.website.WalletService.security.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApplicationUserRepository {

    private final JdbcTemplate jdbcTemplate;

    static final String INSERT_APPLICATION_USER_SQL =
            "INSERT INTO application_user (" +
                    " username," +
                    " password," +
                    " user_role," +
                    " is_account_non_expired, " +
                    " is_account_non_locked, " +
                    " is_credentials_non_expired, " +
                    " is_enabled " +
                    ") " +
                    "VALUES (?, ?, ?::user_role, ?, ?, ?, ?)";

    static final String SELECT_ALL_APPLICATION_USERS_SQL = "SELECT * FROM application_user";

    static final String SELECT_APPLICATION_USER_BY_USERNAME_SQL =
            "   SELECT * " +
                    "   FROM application_user " +
                    "   WHERE username = ?" +
                    "   LIMIT 1";

    static final String EXISTS_USER_BY_USERNAME_SQL =
            "SELECT EXISTS ( " +
                    "   SELECT 1 " +
                    "   FROM application_user " +
                    "   WHERE username = ? " +
                    ")";

    static final String DELETE_USER_BY_USERNAME_SQL =
            "DELETE FROM application_user " +
                    "WHERE username = ?";

    @Autowired
    public ApplicationUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    //CREATE
    public int insertApplicationUser(ApplicationUser user) {
        return jdbcTemplate.update(
                INSERT_APPLICATION_USER_SQL,
                user.getUsername(),
                user.getPassword(),
                user.getApplicationUserRole().name().toUpperCase(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.isEnabled()
        );
    }

    //RETRIEVE
    public List<ApplicationUser> selectAllApplicationUsers() {
        return jdbcTemplate.query(SELECT_ALL_APPLICATION_USERS_SQL, mapApplicationUserFromDb());
    }

    public ApplicationUser selectApplicationUserByUsername(String username) {
        List<ApplicationUser> applicationUsers = jdbcTemplate.query(SELECT_APPLICATION_USER_BY_USERNAME_SQL, mapApplicationUserFromDb(), username);
        if (applicationUsers.size() > 0) {
            return applicationUsers.get(0);
        } else {
            throw new ApiRequestException("User \"" + username + "\" was not found!");
        }
    }

    public Boolean userExistsByUsername(String username) {
        return jdbcTemplate.queryForObject(
                EXISTS_USER_BY_USERNAME_SQL,
                mapExistsApplicationUserFromDb(),
                username
        );
    }

    //UPDATE

    //DELETE
    public int deleteUserByUsername(String username) {
        return jdbcTemplate.update(DELETE_USER_BY_USERNAME_SQL, username);
    }


    //ROWMAPPER
    RowMapper<ApplicationUser> mapApplicationUserFromDb() {
        return (resultSet, i) -> {

            String username = resultSet.getString("username");
            String password = resultSet.getString("password");

            String userRoleStr = resultSet.getString("user_role").toUpperCase();
            ApplicationUserRole userRole = ApplicationUserRole.valueOf(userRoleStr);

            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");

            return new ApplicationUser(
                    username,
                    password,
                    userRole,
                    isAccountNonExpired,
                    isAccountNonLocked,
                    isCredentialsNonExpired,
                    isEnabled
            );
        };

    }

    RowMapper<Boolean> mapExistsApplicationUserFromDb() {
        return (resultSet, columnIndex) -> resultSet.getBoolean(1);
    }

}
