package com.docappointment.iam.dao;

import java.util.List;
import java.util.Optional;

import com.docappointment.iam.entities.User;
import com.docappointment.iam.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {

    /**
     * Finds an user by his email
     *
     * @param email
     * @return an USER object if found, else returns null
     */
    Optional<User> findByEmail(String email);
    List<User> findAllByRole(Role role);
    Page<User> findByRole(Role role, Pageable pageable);
}
