package com.jm.security.Repository;

import com.jm.security.Entity.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserSec, Long> {
    //Crea una sentencia en base al nombre en ingles del metodo
    // y tambien se puede hacer mediante @Query pero en este caso no es necesario
    Optional<UserSec> findUserByUsername(String username);
}
