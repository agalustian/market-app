package ru.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.models.Image;

@Repository
public interface ImagesJpaRepository extends JpaRepository<Image, Integer> {

  Image getImageById(Integer itemId);

}
