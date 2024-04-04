package com.audition.web;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import com.audition.service.AuditionService;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.audition.common.AppConstants.POST_ID_NOT_VALID;
@RestController
public class AuditionController {

    @Autowired
    private transient AuditionService auditionService;

    @Autowired
    private transient AuditionLogger logger;

    private static final Logger LOG = LoggerFactory.getLogger(AuditionController.class);

    // TODO Add a query param that allows data filtering. The intent of the filter is at developers discretion.
    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> getPosts(
        @RequestParam(name = "title", required = false) final String title) {
        logger.info(LOG, "Inside AuditionController.getPosts() method, value of title param is : {}", title);
        // TODO Add logic that filters response data based on the query param
        final List<AuditionPost> posts = auditionService.getPosts();
        if (title != null && !title.isEmpty() && !CollectionUtils.isEmpty(posts)) {
            return posts.stream().filter(post -> post.getTitle().equalsIgnoreCase(title)).collect(Collectors.toList());
        } else {
            return posts;
        }
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getPostById(@PathVariable("id") final String postId) {
        if (postId != null && !postId.isEmpty()) {
            return auditionService.getPostById(postId);
        } else {
            logger.error(LOG, POST_ID_NOT_VALID);
            throw new SystemException(POST_ID_NOT_VALID);
        }

    }

    // TODO Add additional methods to return comments for each post. Hint: Check https://jsonplaceholder.typicode.com/
    @RequestMapping(value = "/posts/{id}/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getCommentsByPostId(@PathVariable("id") final String postId) {
        if (postId != null && !postId.isEmpty()) {
            return auditionService.getCommentsByPostId(postId);
        } else {
            logger.error(LOG, POST_ID_NOT_VALID);
            throw new SystemException(POST_ID_NOT_VALID);
        }
    }

    @RequestMapping(value = "/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionComment> getAllCommentByPostId(@RequestParam("postId") final String postId) {
        if (postId != null && !postId.isEmpty()) {
            return auditionService.getAllCommentByPostId(postId);
        } else {
            logger.error(LOG, POST_ID_NOT_VALID);
            throw new SystemException(POST_ID_NOT_VALID);
        }
    }

}
