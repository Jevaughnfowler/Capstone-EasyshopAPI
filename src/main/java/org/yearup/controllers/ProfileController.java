package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.security.SecurityUtils;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/profile")
@PreAuthorize("isAuthenticated()")
public class ProfileController
{
    private final ProfileDao profileDao;
    private final UserDao userDao;

    public ProfileController(ProfileDao profileDao, UserDao userDao)
    {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @GetMapping
    public Profile getProfile()
    {
        String username = SecurityUtils.getCurrentUsername().orElseThrow();
        User user = userDao.getByUserName(username);
        return profileDao.getByUserId(user.getId());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)  
    public void updateProfile(@RequestBody Profile profile)
    {
        String username = SecurityUtils.getCurrentUsername().orElseThrow();
        User user = userDao.getByUserName(username);
        profile.setUserId(user.getId());
        profileDao.update(profile);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile()
    {
        String username = SecurityUtils.getCurrentUsername().orElseThrow();
        User user = userDao.getByUserName(username);
        profileDao.delete(user.getId());
    }

}
