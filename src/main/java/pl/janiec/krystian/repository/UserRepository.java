package pl.janiec.krystian.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.janiec.krystian.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
