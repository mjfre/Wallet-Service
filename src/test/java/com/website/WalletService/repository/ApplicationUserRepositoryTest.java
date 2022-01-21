package com.website.WalletService.repository;

import com.website.WalletService.exception.ApiRequestException;
import com.website.WalletService.repository.dto.ApplicationUser;
import com.website.WalletService.security.ApplicationUserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ApplicationUserRepositoryTest {

    private ApplicationUserRepository applicationUserRepository;

    @Mock
    JdbcTemplate jdbcTemplate;

    @Mock
    ResultSet resultSet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        applicationUserRepository = new ApplicationUserRepository(jdbcTemplate);
    }

    @Test
    void whenInsertApplicationUser_thenInsertRow() {
        //given
        ApplicationUser applicationUser = new ApplicationUser(
                "USERNAME",
                "PASSWORD",
                ApplicationUserRole.ADMIN,
                true,
                true,
                true,
                true
        );
        int expectedRowsChanged = 1;

        when(jdbcTemplate.update(ApplicationUserRepository.INSERT_APPLICATION_USER_SQL,
                applicationUser.getUsername(),
                applicationUser.getPassword(),
                applicationUser.getApplicationUserRole().name().toUpperCase(),
                applicationUser.isAccountNonExpired(),
                applicationUser.isAccountNonLocked(),
                applicationUser.isCredentialsNonExpired(),
                applicationUser.isEnabled()))
                .thenReturn(expectedRowsChanged);

        //when
        int actualRowsChanged = applicationUserRepository.insertApplicationUser(applicationUser);

        //then
        assertEquals(expectedRowsChanged, actualRowsChanged);
    }


    @Test
    void whenSelectAllApplicationUsers_thenReturnApplicationUsers() {
        //given
        ApplicationUser applicationUser = new ApplicationUser(
                "USERNAME",
                "PASSWORD",
                ApplicationUserRole.ADMIN,
                true,
                true,
                true,
                true
        );
        List<ApplicationUser> expectedApplicationUsers = List.of(applicationUser);

        when(jdbcTemplate.query(
                ApplicationUserRepository.SELECT_ALL_APPLICATION_USERS_SQL,
                applicationUserRepository.mapApplicationUserFromDb()))
                .thenReturn(expectedApplicationUsers);

        //when
        List<ApplicationUser> actualApplicationUsers = applicationUserRepository.selectAllApplicationUsers();

        //then
        assertEquals(expectedApplicationUsers, actualApplicationUsers);
    }

    @Test
    void whenSelectApplicationUserByUsername_thenReturnApplicationUser() {
        //given
        String username = "USERNAME";
        ApplicationUser expectedApplicationUser = new ApplicationUser(
                username,
                "PASSWORD",
                ApplicationUserRole.ADMIN,
                true,
                true,
                true,
                true
        );

        when(jdbcTemplate.query(
                ApplicationUserRepository.SELECT_APPLICATION_USER_BY_USERNAME_SQL,
                applicationUserRepository.mapApplicationUserFromDb(),
                username))
                .thenReturn(List.of(expectedApplicationUser));
        //when
        ApplicationUser actualApplicationUser = applicationUserRepository.selectApplicationUserByUsername(username);

        //then
        assertEquals(expectedApplicationUser, actualApplicationUser);
    }

    @Test
    void givenUserNotFound_whenSelectApplicationUserByUsername_thenThrowsException() {
        //given
        String username = "USERNAME";

        when(jdbcTemplate.query(
                ApplicationUserRepository.SELECT_APPLICATION_USER_BY_USERNAME_SQL,
                applicationUserRepository.mapApplicationUserFromDb(),
                username))
                .thenReturn(new ArrayList<>());

        //when
        //then
        Throwable exceptionThrown = assertThrows(ApiRequestException.class, () -> applicationUserRepository.selectApplicationUserByUsername(username));
        assertEquals(exceptionThrown.getMessage(), "User \"" + username + "\" was not found!");
    }

    @Test
    void whenUserExistsByUsername_thenReturnTrue() {
        //given
        String username = "USERNAME";
        boolean expectedUserExists = true;

        when(jdbcTemplate.queryForObject(
                ApplicationUserRepository.EXISTS_USER_BY_USERNAME_SQL,
                applicationUserRepository.mapExistsApplicationUserFromDb(),
                username))
                .thenReturn(expectedUserExists);

        //when
        boolean actualUserExists = applicationUserRepository.userExistsByUsername(username);

        //then
        assertEquals(expectedUserExists, actualUserExists);
    }

    @Test
    void whenDeleteUserByUsername_thenReturnNumRowsChanged() {
        //given
        String username = "USERNAME";
        int expectedNumRowsChanged = 1;
        when(jdbcTemplate.update(ApplicationUserRepository.DELETE_USER_BY_USERNAME_SQL, username))
                .thenReturn(expectedNumRowsChanged);

        //when
        int actualNumRowsChanged = applicationUserRepository.deleteUserByUsername(username);

        //then
        assertEquals(expectedNumRowsChanged, actualNumRowsChanged);
    }


    @Test
    void itShouldMapApplicationUserFromDb() throws SQLException {
        //Given
        UUID applicationUserId = UUID.randomUUID();
        String[] keys = {"username", "password", "user_role", "is_account_non_expired", "is_account_non_locked", "is_credentials_non_expired", "is_enabled"};
        String[] values = {"USERNAME", "PASSWORD", "ADMIN", "true", "true", "true", "true"};

        ApplicationUser expectedApplicationUser = new ApplicationUser(
                values[0],
                values[1],
                ApplicationUserRole.valueOf(values[2]),
                Boolean.parseBoolean(values[3]),
                Boolean.parseBoolean(values[4]),
                Boolean.parseBoolean(values[5]),
                Boolean.parseBoolean(values[6])
        );

        when(resultSet.getString("teacher_id")).thenReturn(applicationUserId.toString());
        for (int i = 0; i < keys.length; i++) {
            when(resultSet.getString(keys[i])).thenReturn(values[i]);
            when(resultSet.getBoolean(keys[i])).thenReturn(Boolean.valueOf(values[i]));
        }

        //When
        RowMapper<ApplicationUser> mapper = applicationUserRepository.mapApplicationUserFromDb();
        ApplicationUser actualApplicationUser = mapper.mapRow(resultSet, 0);

        //Then
        assertEquals(expectedApplicationUser, actualApplicationUser);
    }


}
