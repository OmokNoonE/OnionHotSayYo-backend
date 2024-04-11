package org.omoknoone.onionhotsayyo.post.command.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MyPostListDTO implements Serializable {

    private Integer postId;
    private String title;
    private LocalDateTime postedDate;
    private int hits;
    private String categoryId;
    private String location;
}