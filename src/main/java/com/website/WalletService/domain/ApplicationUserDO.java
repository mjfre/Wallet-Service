package com.website.WalletService.domain;

import com.website.WalletService.repository.dto.ApplicationUser;
import com.website.WalletService.security.ApplicationUserRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

import java.util.UUID;

@Value
public class ApplicationUserDO {

    @ApiModelProperty(value = "Uniquely identifies a user")
    String username;

    @ApiModelProperty(value = "User's role within the application")
    ApplicationUserRole applicationUserRole;

    public ApplicationUserDO(ApplicationUser user) {
        this.username = user.getUsername();
        this.applicationUserRole = user.getApplicationUserRole();
    }

}
