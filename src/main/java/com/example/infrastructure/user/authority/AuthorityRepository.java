package com.example.infrastructure.user.authority;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domain.user.authority.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
