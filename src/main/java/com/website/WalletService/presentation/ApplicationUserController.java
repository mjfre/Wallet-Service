package com.website.WalletService.presentation;

import com.website.WalletService.domain.ApplicationUserDO;
import com.website.WalletService.repository.dto.ApplicationUser;
import com.website.WalletService.security.ApplicationUserRole;
import com.website.WalletService.service.ApplicationUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("user")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@Api(value = "/user", consumes = "application/json", produces = "application/json")
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;

    @Autowired
    public ApplicationUserController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    //CREATE

    @ApiOperation(value = "Add user", notes="Permitted user roles: ADMIN")
    @PostMapping
    public int addApplicationUser(@RequestBody ApplicationUser user){
        return applicationUserService.addApplicationUser(user);
    }

    //READ

    @ApiOperation(value = "Get all users", notes="Permitted user roles: ADMIN")
    @GetMapping
    public List<ApplicationUserDO> getApplicationUsers(){
        return applicationUserService.getApplicationUsers();
    }

    //UPDATE

    //DELETE

    @ApiOperation(value = "Delete user, by their username", notes="Permitted user roles: ADMIN")
    @DeleteMapping(path = "/{username}")
    public int deleteApplicationUserByUserName(@PathVariable("username") String username){
        return applicationUserService.deleteApplicationUserByUsername(username);
    }

    @ApiOperation(value = "Delete all users", notes="Permitted user roles: ADMIN")
    @DeleteMapping(path = "/all")
    public int deleteAllApplicationUsers(){
        return applicationUserService.deleteAllApplicationUsers();
    }

}
