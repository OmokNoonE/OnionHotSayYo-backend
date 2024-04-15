package org.omoknoone.onionhotsayyo.post.dto;

import lombok.*;

import java.time.LocalDateTime;

// 카테고리별 게시글 목록 조회 시
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PostListByCategoryDTO {

    private Integer postId;
    private String title;
    private int hits;
    private String memberId;
    private LocalDateTime postedDate;
}
