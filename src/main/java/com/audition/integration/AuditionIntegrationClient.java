package com.audition.integration;

import static com.audition.common.AppConstants.NOT_FOUND_MSG;
import static com.audition.common.AppConstants.POST_NOT_FOUND_MSG;
import static com.audition.common.AppConstants.UNKNOWN_ERROR_MSG;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class AuditionIntegrationClient {

    @Autowired
    private transient RestTemplate restTemplate;

    public List<AuditionPost> getPosts() {
        // TODO make RestTemplate call to get Posts from https://jsonplaceholder.typicode.com/posts
        final ResponseEntity<List<AuditionPost>> posts = callGet(
            "https://jsonplaceholder.typicode.com/posts", null,
            new ParameterizedTypeReference<List<AuditionPost>>() {
            });
        return posts.getBody();
    }

    public AuditionPost getPostById(final String id) {
        // TODO get post by post ID call from https://jsonplaceholder.typicode.com/posts/
        try {
            final ResponseEntity<AuditionPost> auditionPost = callGet(
                "https://jsonplaceholder.typicode.com/posts/{id}", id,
                new ParameterizedTypeReference<AuditionPost>() {
                });
            return auditionPost.getBody();
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException(POST_NOT_FOUND_MSG + id, NOT_FOUND_MSG, 404);
            } else {
                // TODO Find a better way to handle the exception so that the original error message is not lost. Feel free to change this function.
                throw new SystemException(UNKNOWN_ERROR_MSG + e.getMessage(), e);
            }
        }
    }

    // TODO Write a method GET comments for a post from https://jsonplaceholder.typicode.com/posts/{postId}/comments - the comments must be returned as part of the post.

    public AuditionPost getCommentsByPostId(final String postId) {
        final AuditionPost post = getPostById(postId);
        if (post != null) {
            try {
                final ResponseEntity<List<AuditionComment>> comments = callGet(
                    "https://jsonplaceholder.typicode.com/posts/{postId}/comments", postId,
                    new ParameterizedTypeReference<List<AuditionComment>>() {
                    });
                if (comments != null) {
                    post.setComments(comments.getBody());
                }
                return post;
            } catch (final HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new SystemException(POST_NOT_FOUND_MSG + postId, NOT_FOUND_MSG, 404);
                } else {
                    throw new SystemException(UNKNOWN_ERROR_MSG + e.getMessage(), e);
                }
            }
        } else {
            throw new SystemException(POST_NOT_FOUND_MSG + postId, NOT_FOUND_MSG, 404);
        }
    }

    // TODO write a method. GET comments for a particular Post from https://jsonplaceholder.typicode.com/comments?postId={postId}.
    // The comments are a separate list that needs to be returned to the API consumers. Hint: this is not part of the AuditionPost pojo.
    public List<AuditionComment> getAllCommentByPostId(final String postId) {
        try {
            final ResponseEntity<List<AuditionComment>> comments = callGet(
                "https://jsonplaceholder.typicode.com/comments?postId={postId}", postId,
                new ParameterizedTypeReference<List<AuditionComment>>() {
                });
            return comments.getBody();
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException(POST_NOT_FOUND_MSG + postId, NOT_FOUND_MSG, 404);
            } else {
                throw new SystemException(UNKNOWN_ERROR_MSG, e);
            }
        }
    }

    public <T> ResponseEntity<T> callGet(final String url, final String postId,
        final ParameterizedTypeReference<T> responseType) {
        if (postId != null) {
            return restTemplate.exchange(url, HttpMethod.GET, null, responseType, postId);
        } else {
            return restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        }

    }
}
