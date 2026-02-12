package com.jm.security.Service;

import com.jm.security.Entity.UserSec;
import com.jm.security.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserSec userSec = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("The User: "+ username + "was not found"));

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        userSec.getRolList()
                .forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));


        userSec.getRolList().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> grantedAuthorities.add(new SimpleGrantedAuthority(permission.getPermissionName())));


        return new User(userSec.getUsername(),
                userSec.getPassword(),
                userSec.isEnable(),
                userSec.isAccountNotExpired(),
                userSec.isAccountNonLocked(),
                userSec.isCredentialsNonExpired(),
                grantedAuthorities);
    }
}
