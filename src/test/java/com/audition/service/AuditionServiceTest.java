package com.audition.service;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

import com.audition.TestUtils;
import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class AuditionServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private AuditionIntegrationClient auditionIntegrationClient;

    @Test
    public void testGetPosts() {
        List<AuditionPost> posts = TestUtils.buildPosts();
        ResponseEntity<List<AuditionPost>> responseEntity = new ResponseEntity<>(posts, HttpStatus.OK);
        when(restTemplate.exchange("https://jsonplaceholder.typicode.com/posts", HttpMethod.GET, null,
            new ParameterizedTypeReference<List<AuditionPost>>() {
            })).thenReturn(responseEntity);
        when(auditionIntegrationClient.callGet("https://jsonplaceholder.typicode.com/posts", null,
            new ParameterizedTypeReference<List<AuditionPost>>() {
            })).thenReturn(responseEntity);
        List<AuditionPost> actualPosts = auditionIntegrationClient.getPosts();
        assertNotNull("Posts not found", actualPosts);
        assertEquals("Incorrect Posts List", posts.size(), actualPosts.size());
    }

    @Test
    public void testGetPostById() {
        AuditionPost post = TestUtils.buildPosts().get(0);

        ResponseEntity<AuditionPost> responseEntity = new ResponseEntity<>(post, HttpStatus.OK);
        when(restTemplate.exchange("https://jsonplaceholder.typicode.com/posts/{id}", HttpMethod.GET, null,
            new ParameterizedTypeReference<AuditionPost>() {
            }, "1")).thenReturn(responseEntity);
        when(auditionIntegrationClient.callGet("https://jsonplaceholder.typicode.com/posts/{id}", "1",
            new ParameterizedTypeReference<AuditionPost>() {
            })).thenReturn(responseEntity);
        AuditionPost actualPost = auditionIntegrationClient.getPostById("1");
        assertNotNull("Posts not found", actualPost);
        assertEquals("Incorrect Post", post.getId(), actualPost.getId());
    }

}
