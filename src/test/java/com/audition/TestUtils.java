package com.audition;

import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestUtils {
    @SuppressWarnings("rawtypes")
    public static List jsonToList(String json, TypeToken token) {
        Gson gson = new Gson();
        return gson.fromJson(json, token.getType());
    }

    public static String objectToJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static <T> T jsonToObject(String json, Class<T> classOf) {
        Gson gson = new Gson();
        return gson.fromJson(json, classOf);
    }

    public static List<AuditionPost> buildPosts() {
        AuditionPost post1 = new AuditionPost(1, 1, "post1 title", "post1 body", new ArrayList<>());
        AuditionPost post2 = new AuditionPost(1, 2, "post2 title", "post2 body", new ArrayList<>());
        List<AuditionPost> posts = Arrays.asList(post1, post2);
        return posts;
    }

    public static List<AuditionComment> buildComments() {
        AuditionComment comment1 = AuditionComment.builder().postId(1).id(1).body("comment1 body").name("comment1 name")
            .email("comment1 email").build();
        AuditionComment comment2 = AuditionComment.builder().postId(1).id(2).body("comment2 body").name("comment2 name")
            .email("comment2 email").build();
        List<AuditionComment> comments = Arrays.asList(comment1, comment2);
        return comments;
    }

    public static AuditionPost buildPostWithComments() {
        AuditionPost auditionPost = buildPosts().get(0);
        auditionPost.setComments(buildComments());
        return auditionPost;
    }
}