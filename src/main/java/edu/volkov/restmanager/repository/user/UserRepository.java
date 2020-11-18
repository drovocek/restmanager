package edu.volkov.restmanager.repository.user;

import edu.volkov.restmanager.model.Like;
import edu.volkov.restmanager.model.User;
import edu.volkov.restmanager.repository.like.CrudLikeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class UserRepository {

    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final CrudUserRepository crudUserRepository;
    private final CrudLikeRepository crudLikeRepository;

    public UserRepository(CrudUserRepository crudUserRepository, CrudLikeRepository crudLikeRepository) {
        this.crudUserRepository = crudUserRepository;
        this.crudLikeRepository = crudLikeRepository;
    }

    public User save(User user) {
        return crudUserRepository.save(user);
    }

    public boolean delete(Integer id) {
        return crudUserRepository.delete(id) != 0;
    }

    public User get(Integer id) {
        return crudUserRepository.findById(id).orElse(null);
    }

    public User getByEmail(String email) {
        return crudUserRepository.getByEmail(email);
    }

    public List<User> getAll() {
        return crudUserRepository.findAll(SORT_NAME_EMAIL);
    }

    public Like addLike(Like like) {
        return crudLikeRepository.save(like);
    }

    public boolean deleteLike(Integer userId, LocalDate likeDate) {
        return crudLikeRepository.delete(userId, likeDate) != 0;
    }
}
