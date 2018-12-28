package org.lz.boilerplate.springrest.repository;

import org.lz.boilerplate.springrest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
}
