package com.jm.security.Controller;

import com.jm.security.Entity.Permission;
import com.jm.security.Entity.Role;
import com.jm.security.Service.PermissionService;
import com.jm.security.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;


    @GetMapping("")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Role>>  getRoles()
    {
        List<Role> listRoles = roleService.findAll();
        return new ResponseEntity<>(listRoles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Role>  getRoleById(@PathVariable Long id)
    {
        Optional<Role> role = roleService.findById(id);
        return role.map(ResponseEntity::ok).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> createRole(@RequestBody Role role)
    {
        Set<Permission> permissionsList = new HashSet<>();
        Permission readPermission;

        //recuperar la permission por su ID
        for(Permission p : role.getPermissions()){
            readPermission = permissionService.findById(p.getId()).orElse(null);
            if(readPermission != null){
                permissionsList.add(readPermission);
            }
        }
        role.setPermissions(permissionsList);
        Role newRole = roleService.save(role);
        return new ResponseEntity<>(newRole, HttpStatus.OK);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role>  updateRole(@RequestBody Role role)
    {
        Role updatedRole = roleService.findById(role.getId()).orElse(null);
        roleService.update(updatedRole);
        return new ResponseEntity<>(updatedRole, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteById(@PathVariable Long id)
    {
        roleService.deleteById(id);
        return new ResponseEntity<>("Role has been deleted", HttpStatus.NO_CONTENT);
    }
}
