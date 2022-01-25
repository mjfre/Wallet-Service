package com.website.WalletService.service;

import com.website.WalletService.domain.ApplicationUserDO;
import com.website.WalletService.exception.ApiRequestException;
import com.website.WalletService.repository.ApplicationUserRepository;
import com.website.WalletService.repository.dto.ApplicationUser;
import com.website.WalletService.security.ApplicationUserRole;
import com.website.WalletService.security.PasswordConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationUserServiceTest {

    private ApplicationUserService applicationUserService;

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        applicationUserService = new ApplicationUserService(
                applicationUserRepository
        );
    }

    @Test
    void whenAddApplicationUser_thenReturnNumRowsChangedAndSendWelcomeEmailAndInsertPasswordResetToken() {
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

        when(applicationUserRepository.insertApplicationUser(applicationUser))
                .thenReturn(expectedRowsChanged);

        //when
        int actualRowsChanged = applicationUserService.addApplicationUser(applicationUser);

        //then
        assertEquals(expectedRowsChanged, actualRowsChanged);
    }

    @Test
    void givenUserNotAddedToDatabase_whenAddApplicationUser_thenThrowApiRequestException() {
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
        int expectedRowsChanged = 0;

        when(applicationUserRepository.insertApplicationUser(applicationUser))
                .thenReturn(expectedRowsChanged);

        //when
        //then
        Throwable exceptionThrown = assertThrows(ApiRequestException.class, () -> applicationUserService.addApplicationUser(applicationUser));
        assertEquals(exceptionThrown.getMessage(), "User could not be added to the database");
    }

    @Test
    void whenGetApplicationUsers_thenReturnApplicationUsers() {
        //given
        ApplicationUser applicationUser1 = new ApplicationUser(
                "USERNAME",
                "PASSWORD",
                ApplicationUserRole.ADMIN,
                true,
                true,
                true,
                true
        );

        ApplicationUser applicationUser2 = new ApplicationUser(
                "USERNAME2",
                "PASSWORD",
                ApplicationUserRole.ADMIN,
                true,
                true,
                true,
                true
        );
        List<ApplicationUser> applicationUsers = List.of(
                applicationUser1,
                applicationUser2
        );
        List<ApplicationUserDO> expectedApplicationUserDOs = applicationUserService.applicationUserToApplicationUserDO(applicationUsers);

        when(applicationUserRepository.selectAllApplicationUsers())
                .thenReturn(applicationUsers);

        //when
        List<ApplicationUserDO> actualApplicationUserDOs = applicationUserService.getApplicationUsers();

        //then
        assertEquals(expectedApplicationUserDOs, actualApplicationUserDOs);
    }

    @Test
    void whenDeleteApplicationUserByUsername_thenDeleteAnyPasswordResetTokenAndReturnNumRowsChanged() {
        //given
        String username = "USERNAME";
        int expectedNumRowsChanged = 1;

        when(applicationUserRepository.deleteUserByUsername(username))
                .thenReturn(expectedNumRowsChanged);

        //when
        int actualNumRowsChanged = applicationUserService.deleteApplicationUserByUsername(username);

        //then
        assertEquals(expectedNumRowsChanged, actualNumRowsChanged);
    }


    @Test
    void whenLoadUserByUsername_ThenReturnUserDetails() {
        //given
        String username = "USERNAME";
        ApplicationUser expectedApplicationUser = new ApplicationUser(
                "USERNAME",
                "PASSWORD",
                ApplicationUserRole.ADMIN,
                true,
                true,
                true,
                true
        );

        when(applicationUserRepository.selectApplicationUserByUsername(username))
                .thenReturn(expectedApplicationUser);

        //when
        UserDetails actualUserDetails = applicationUserService.loadUserByUsername(username);

        //then
        assertEquals(expectedApplicationUser, actualUserDetails);
    }
}
