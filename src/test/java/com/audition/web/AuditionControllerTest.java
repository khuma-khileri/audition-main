package com.audition.web;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

import com.audition.TestUtils;
import com.audition.config.TestConfig;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import com.audition.service.AuditionService;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@WebMvcTest(AuditionController.class)
@Import(TestConfig.class)
public class AuditionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AuditionService auditionService;

    @SneakyThrows
    @Test
    public void testGetPostsWithTitle() {
        List<AuditionPost> posts = TestUtils.buildPosts();
        when(auditionService.getPosts()).thenReturn(posts);

        // execute
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/posts").param("title", "post1 title").accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);

        // verify that service method was called once
        verify(auditionService).getPosts();

        TypeToken<List<AuditionPost>> token = new TypeToken<List<AuditionPost>>() {
        };
        @SuppressWarnings("unchecked")
        List<AuditionPost> postsResult = TestUtils.jsonToList(result.getResponse().getContentAsString(), token);

        assertNotNull("Posts not found", postsResult);
        assertEquals("Incorrect Posts List", 1, postsResult.size());

    }

    @SneakyThrows
    @Test
    public void testGetPostsWithoutTitle() {
        List<AuditionPost> posts = TestUtils.buildPosts();
        when(auditionService.getPosts()).thenReturn(posts);

        // execute
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/posts")
                    .accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);

        // verify that service method was called once
        verify(auditionService).getPosts();

        TypeToken<List<AuditionPost>> token = new TypeToken<List<AuditionPost>>() {
        };
        @SuppressWarnings("unchecked")
        List<AuditionPost> postsResult = TestUtils.jsonToList(result.getResponse().getContentAsString(), token);

        assertNotNull("Posts not found", postsResult);
        assertEquals("Incorrect Posts List", posts.size(), postsResult.size());
    }

    @SneakyThrows
    @Test
    public void testGetPostById() {
        List<AuditionPost> posts = TestUtils.buildPosts();
        AuditionPost auditionPost = posts.get(0);
        when(auditionService.getPostById("1")).thenReturn(auditionPost);

        // execute
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/posts/{id}", "1")
                    .accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);

        // verify that service method was called once
        verify(auditionService).getPostById("1");

        @SuppressWarnings("unchecked")
        AuditionPost postResult = TestUtils.jsonToObject(result.getResponse().getContentAsString(), AuditionPost.class);

        assertNotNull("Posts not found", postResult);
        assertEquals("Incorrect Post id", auditionPost.getId(), postResult.getId());
    }

    @SneakyThrows
    @Test
    public void testGetCommentsByPostId() {
        AuditionPost auditionPost = TestUtils.buildPostWithComments();
        when(auditionService.getCommentsByPostId("1")).thenReturn(auditionPost);

        // execute
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/posts/{id}/comments", "1")
                    .accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);

        // verify that service method was called once
        verify(auditionService).getCommentsByPostId("1");

        @SuppressWarnings("unchecked")
        AuditionPost postResult = TestUtils.jsonToObject(result.getResponse().getContentAsString(), AuditionPost.class);

        assertNotNull("Posts not found", postResult);
        assertEquals("Incorrect Comment List", auditionPost.getComments().size(), postResult.getComments().size());
    }

    @SneakyThrows
    @Test
    public void testGetAllCommentByPostId() {
        List<AuditionComment> auditionComments = TestUtils.buildComments();
        when(auditionService.getAllCommentByPostId("1")).thenReturn(auditionComments);

        // execute
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/comments")
                    .param("postId", "1")
                    .accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);

        // verify that service method was called once
        verify(auditionService).getAllCommentByPostId("1");

        TypeToken<List<AuditionComment>> token = new TypeToken<List<AuditionComment>>() {
        };
        @SuppressWarnings("unchecked")
        List<AuditionComment> commentsResult = TestUtils.jsonToList(result.getResponse().getContentAsString(), token);

        assertNotNull("Posts not found", commentsResult);
        assertEquals("Incorrect Comment List", auditionComments.size(), commentsResult.size());
    }
}
