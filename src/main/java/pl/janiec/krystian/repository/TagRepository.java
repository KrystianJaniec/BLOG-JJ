package pl.janiec.krystian.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.janiec.krystian.models.Tag;

public interface TagRepository extends JpaRepository<Tag,Long> {

    Tag findByTagName(String tagName);
}
