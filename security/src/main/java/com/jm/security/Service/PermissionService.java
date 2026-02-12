package com.jm.security.Service;

import com.jm.security.Entity.Permission;
import com.jm.security.Repository.IPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private IPermissionRepository permissionRepository;

    public List<Permission> findAll(){
        return permissionRepository.findAll();
    }

    public Optional<Permission> findById(Long id){
        return  permissionRepository.findById(id);
    }

    public Permission save(Permission p){
        return permissionRepository.save(p);
    }

    public void update(Permission p){
        permissionRepository.save(p);
    }

    public void deleteById(Long id){
        permissionRepository.deleteById(id);
    }
}
