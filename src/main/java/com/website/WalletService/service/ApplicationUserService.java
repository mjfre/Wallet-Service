package com.website.WalletService.service;

import com.website.WalletService.domain.ApplicationUserDO;
import com.website.WalletService.exception.ApiRequestException;
import com.website.WalletService.repository.ApplicationUserRepository;
import com.website.WalletService.repository.dto.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;

    @Autowired
    public ApplicationUserService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    //CREATE
    public int addApplicationUser(ApplicationUser user) {
        //use bcrypt to hash password
        user.encodePassword();
        if (applicationUserRepository.insertApplicationUser(user) == 1) {
            return 1;
        }
        throw new ApiRequestException("User could not be added to the database");
    }



    //READ
    public List<ApplicationUserDO> getApplicationUsers() {
        List<ApplicationUser> users = applicationUserRepository.selectAllApplicationUsers();
        return applicationUserToApplicationUserDO(users);
    }

    //This method is required. See note below
    public ApplicationUser getApplicationUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserRepository.selectApplicationUserByUsername(username);
    }

    //UPDATE

    //DELETE
    public int deleteApplicationUserByUsername(String username) {
        if (username.equals("administrator")) {
            throw new ApiRequestException("User \"" + username + "\" cannot be deleted");
        }
        return applicationUserRepository.deleteUserByUsername(username);
    }

    public int deleteAllApplicationUsers() {
        //get all users
        List<ApplicationUser> allUsers = applicationUserRepository.selectAllApplicationUsers();
        //for each user
        int numRowsDeleted = 0;
        for (ApplicationUser user : allUsers) {
            String username = user.getUsername();
            if (!username.equals("administrator")) {
                //drop from table
                numRowsDeleted += applicationUserRepository.deleteUserByUsername(username);
            }
        }
        return numRowsDeleted;
    }

    List<ApplicationUserDO> applicationUserToApplicationUserDO(List<ApplicationUser> users) {
        List<ApplicationUserDO> userDOs = new ArrayList<>();

        for (ApplicationUser user : users) {
            userDOs.add(new ApplicationUserDO(user));
        }
        return userDOs;
    }

    /*
    This method is inherited from org.springframework.security.core.userdetails.UserDetailsService
    and overriding is required.  It breaks the normal naming conventions used in the application, so
    the methods are chained together.
    */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return getApplicationUserByUsername(s);
    }

}
