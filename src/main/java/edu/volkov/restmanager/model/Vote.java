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
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(name = "votes_unique_idx", columnNames = {"user_id", "vote_date"})})
public class Vote extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "vote_date", nullable = false, columnDefinition = "date")
    @NotNull
    private LocalDate voteDate = LocalDate.now();

    public Vote(Integer id, User user, Restaurant restaurant, LocalDate voteDate) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
        this.voteDate = voteDate;
    }

    public Vote(Vote vote) {
        this(vote.getId(), vote.getUser(), vote.getRestaurant(), vote.getVoteDate());
    }

    @Override
    public String toString() {
        return "Vote{" +
                " id=" + id +
                ", userId=" + user.getId() +
                ", restaurantId=" + restaurant.getId() +
                ", voteDate=" + voteDate +
                '}';
    }
}
