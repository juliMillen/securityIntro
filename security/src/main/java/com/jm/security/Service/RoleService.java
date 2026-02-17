package com.jm.security.Service;

import com.jm.security.Entity.Role;
import com.jm.security.Repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private IRoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    public Role update(Role role) {
        if (roleRepository.findById(role.getId()).isPresent()) {
            Role newRole = new  Role();
            newRole.setId(role.getId());
            newRole.setRole(role.getRole());
            newRole.setPermissions(role.getPermissions());
            return roleRepository.save(newRole);
        }
        return null;
    }
}
