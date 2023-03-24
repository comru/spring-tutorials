package com.sbu.dj.repository;

import com.sbu.dj.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}