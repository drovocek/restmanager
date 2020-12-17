package edu.volkov.restmanager.web.rest.vote;

import edu.volkov.restmanager.service.VoteService;
import edu.volkov.restmanager.web.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = UserVoteController.REST_URL)
public class UserVoteController {

    static final String REST_URL = "/rest/profile/votes";

    private final VoteService voteService;

    public UserVoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PatchMapping("/{restId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@PathVariable int restId) {
        int userId = SecurityUtil.authUserId();
        voteService.vote(userId, restId);
    }
}
