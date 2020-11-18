package edu.volkov.restmanager.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "likes", uniqueConstraints = {@UniqueConstraint(name = "likes_unique_idx", columnNames = {"user_id", "like_date"})})
public class Like extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "like_date", nullable = false, columnDefinition = "date")
    @NotNull
    private LocalDate likeDate = LocalDate.now();

    public Like(Integer id, User user, Restaurant restaurant, LocalDate likeDate) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
        this.likeDate = likeDate;
    }

    public Like(Like like) {
        this(like.getId(), like.getUser(), like.getRestaurant(), like.getLikeDate());
    }
}
