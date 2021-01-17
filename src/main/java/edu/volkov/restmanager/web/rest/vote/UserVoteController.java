package edu.volkov.restmanager.web.rest.vote;

import edu.volkov.restmanager.model.Vote;
import edu.volkov.restmanager.service.VoteService;
import edu.volkov.restmanager.to.VoteTo;
import edu.volkov.restmanager.web.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UserVoteController.REST_URL)
@Slf4j
public class UserVoteController {

    static final String REST_URL = "/rest/profile/votes";

    private final VoteService service;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> create(@RequestParam int restId, @RequestParam Vote emptyVote) {
        int userId = SecurityUtil.authUserId();
        log.info("create vote for rest:{}, by user:{}", restId, userId);

        Vote created = service.vote(userId, restId);
        VoteTo createdTo = new VoteTo(created.getId(),
                created.getUser().getId(),
                created.getRestaurant().getId(),
                created.getVoteDate());

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restId, createdTo.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(createdTo);
    }

    @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateWithMenuItems(@RequestParam int restId) throws BindException {
        int userId = SecurityUtil.authUserId();
        service.vote(userId, restId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public VoteTo get(@PathVariable int id) {
        Vote voteFromDb = service.get(id);

        return new VoteTo(voteFromDb.getId(),
                voteFromDb.getUser().getId(),
                voteFromDb.getRestaurant().getId(),
                voteFromDb.getVoteDate());
    }
}
