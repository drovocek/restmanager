package edu.volkov.restmanager.to;

import edu.volkov.restmanager.model.Vote;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteTo extends BaseTo implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer restId;

    @NotNull
    private LocalDate voteDate;

    public VoteTo(Integer id, @NotNull Integer userId, @NotNull Integer restId, @NotNull LocalDate voteDate) {
        super(id);
        this.userId = userId;
        this.restId = restId;
        this.voteDate = voteDate;
    }

    public VoteTo(Vote vote) {
        super(vote.getId());
        this.userId = vote.getUser().getId();
        this.restId = vote.getRestaurant().getId();
        this.voteDate = vote.getVoteDate();
    }
}
