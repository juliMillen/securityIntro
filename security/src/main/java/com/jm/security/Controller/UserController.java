package com.jm.security.Controller;

import com.jm.security.Entity.Role;
import com.jm.security.Entity.UserSec;
import com.jm.security.Service.RoleService;
import com.jm.security.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("")
    public ResponseEntity<List<UserSec>>  getAllUsers()
    {
        List<UserSec> listUsers = userService.findAll();
        return new ResponseEntity<>(listUsers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSec> getUserById(@PathVariable Long id)
    {
        Optional<UserSec> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<UserSec> createUser(@RequestBody UserSec userSec)
    {
        Set<Role> roles = new HashSet<>();
        Role readRole;

        //recuperar la permission por su ID
        for(Role role: userSec.getRolList()){
            readRole = roleService.findById(role.getId()).orElse(null);
            if(readRole != null){
                roles.add(readRole);
            }
        }
        if (!roles.isEmpty()){
            userSec.setRolList(roles);
            UserSec newUser = userService.save(userSec);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        }
        return null;
    }

    @PatchMapping("/update")
    public ResponseEntity<UserSec> updateUserById(@RequestBody UserSec userSec)
    {
        UserSec updateUser = userService.findById(userSec.getId()).orElse(null);
        if(updateUser != null){
            updateUser.setUsername(userSec.getUsername());
            updateUser.setPassword(userSec.getPassword());
            updateUser.setRolList(userSec.getRolList());
            userService.save(updateUser);
        }
        return new ResponseEntity<>(userSec, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id)
    {
        userService.deleteById(id);
        return new ResponseEntity<>("User has been deleted", HttpStatus.NO_CONTENT);
    }
}


