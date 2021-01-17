package edu.volkov.restmanager.web.rest.vote;

import edu.volkov.restmanager.model.Vote;
import edu.volkov.restmanager.service.VoteService;
import edu.volkov.restmanager.web.SecurityUtil;
import lombok.RequiredArgsConstructor;
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
public class UserVoteController {

    static final String REST_URL = "/rest/profile/votes";

    private final VoteService service;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> create(@RequestParam int restId) {
        int userId = SecurityUtil.authUserId();
        Vote created = service.vote(userId, restId);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restId, created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
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
    public Vote get(@PathVariable int id) {
        return service.get(id);
    }
}
