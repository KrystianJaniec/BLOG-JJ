package pl.janiec.krystian.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.janiec.krystian.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
