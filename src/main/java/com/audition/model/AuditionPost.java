package com.audition.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuditionPost {

    private int userId;
    private int id;
    private String title;
    private String body;
    private List<AuditionComment> comments = new ArrayList<>();

    public void setComments(final List<AuditionComment> comments) {
        this.comments = new ArrayList<>(comments);
    }

    public List<AuditionComment> getComments() {
        return new ArrayList<>(comments);
    }

    public AuditionPost(final int userId, final int id, final String title, final String body, final List<AuditionComment> comments) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
        this.comments = new ArrayList<>(comments);
    }
}
