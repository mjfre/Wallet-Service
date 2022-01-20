package com.website.WalletService.repository.dto;

import com.website.WalletService.security.ApplicationUserRole;
import com.website.WalletService.security.PasswordConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@ApiModel("Application User")
@Data
@NoArgsConstructor
public class ApplicationUser implements UserDetails {


    private static final PasswordConfig passwordConfig = new PasswordConfig();

    @ApiModelProperty(value = "Uniquely identifies a user")
    String username;

    @ApiModelProperty(value = "User's password")
    @NonFinal
    String password;

    @ApiModelProperty(value = "User's role within the application")
    ApplicationUserRole applicationUserRole;

    @ApiModelProperty(value = "Authorities granted to the user")
    Set<? extends GrantedAuthority> grantedAuthorities;

    //below are implemented from UserDetails class
    @ApiModelProperty(value = "Is account expired")
    boolean isAccountNonExpired;

    @ApiModelProperty(value = "Is account locked")
    boolean isAccountNonLocked;

    @ApiModelProperty(value = "Is credentials expired")
    boolean isCredentialsNonExpired;

    @ApiModelProperty(value = "Is the user enabled")
    boolean isEnabled;

    public ApplicationUser(String username,
                           String password,
                           ApplicationUserRole applicationUserRole
    ) {
        this.password = password;
        this.username = username;
        this.applicationUserRole = applicationUserRole;
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
        this.grantedAuthorities = applicationUserRole.getGrantedAuthorities();
    }

    public ApplicationUser(String username,
                           String password,
                           ApplicationUserRole applicationUserRole,
                           boolean isAccountNonExpired,
                           boolean isAccountNonLocked,
                           boolean isCredentialsNonExpired,
                           boolean isEnabled) {
        this.username = username;
        this.password = password;
        this.applicationUserRole = applicationUserRole;
        this.grantedAuthorities = applicationUserRole.getGrantedAuthorities();
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    public void encodePassword() {
        this.password = passwordConfig.passwordEncoder().encode(this.password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }
}
