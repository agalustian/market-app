package ru.market.shopfront.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import ru.market.shopfront.models.Image;

@Repository
public interface ImagesRepository extends R2dbcRepository<Image, Integer> {
}
