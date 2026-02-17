package com.jm.security.Controller;

import com.jm.security.Entity.Permission;
import com.jm.security.Service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Permission>> getAllPermissions() {
        List<Permission> listPermission = permissionService.findAll();
        return new  ResponseEntity<>(listPermission, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        Optional<Permission> permission = permissionService.findById(id);
        return permission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Permission> newPermission(@RequestBody Permission permission) {
        permission = permissionService.save(permission);
        return ResponseEntity.ok(permission);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission) {
        permission = permissionService.save(permission);
        return ResponseEntity.ok(permission);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePermissionById(@PathVariable Long id) {
        permissionService.deleteById(id);
        return new ResponseEntity<>("Permission has been deleted", HttpStatus.NO_CONTENT);

    }
}
