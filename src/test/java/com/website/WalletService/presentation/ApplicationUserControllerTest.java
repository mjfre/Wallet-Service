package com.website.WalletService.presentation;

import com.website.WalletService.domain.ApplicationUserDO;
import com.website.WalletService.repository.dto.ApplicationUser;
import com.website.WalletService.security.ApplicationUserRole;
import com.website.WalletService.service.ApplicationUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ApplicationUserControllerTest {

    private ApplicationUserController applicationUserController;

    @Mock
    private ApplicationUserService applicationUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        applicationUserController = new ApplicationUserController(applicationUserService);
    }

    @Test
    void whenAddUser_thenReturnNumRowsChanged() {
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

        when(applicationUserService.addApplicationUser(applicationUser))
                .thenReturn(expectedRowsChanged);

        //when
        int actualRowsChanged = applicationUserController.addApplicationUser(applicationUser);

        //then
        assertEquals(expectedRowsChanged, actualRowsChanged);
    }

    @Test
    void whenGetUsers_thenReturnListOfApplicationUsers() {
        //given
        List<ApplicationUserDO> expectedApplicationUserDOs = List.of(
                new ApplicationUserDO(
                        new ApplicationUser(
                        "USERNAME",
                        "PASSWORD",
                        ApplicationUserRole.ADMIN,
                        true,
                        true,
                        true,
                        true
                        )
                )
        );

        when(applicationUserService.getApplicationUsers())
                .thenReturn(expectedApplicationUserDOs);

        //when
        List<ApplicationUserDO> actualApplicationUserDOs = applicationUserController.getApplicationUsers();

        //then
        assertEquals(expectedApplicationUserDOs, actualApplicationUserDOs);
    }

    @Test
    void whenDeleteUserByUserName_thenReturnNumRowsChanged() {
        //given
        String username = "USERNAME";
        int expectedRowsChanged = 1;

        when(applicationUserService.deleteApplicationUserByUsername(username))
                .thenReturn(expectedRowsChanged);

        //when
        int actualRowsChanged = applicationUserController.deleteApplicationUserByUserName(username);

        //then
        assertEquals(expectedRowsChanged, actualRowsChanged);
    }

}
