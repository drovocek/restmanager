package edu.volkov.restmanager.repository.like;

import org.springframework.stereotype.Repository;

@Repository
public class LikeRepository {

    private final CrudLikeRepository crudRepository;

    public LikeRepository(CrudLikeRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

}
