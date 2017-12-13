package pl.janiec.krystian.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.janiec.krystian.models.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
