package pl.janiec.krystian.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.janiec.krystian.models.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByRole(String role);
}
