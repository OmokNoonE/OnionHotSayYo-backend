package org.omoknoone.onionhotsayyo.star.service;

import org.omoknoone.onionhotsayyo.star.dto.MyStarPostListDTO;
import org.omoknoone.onionhotsayyo.star.dto.StarDTO;

import java.util.List;

public interface StarService {
	void addStar(StarDTO starDTO);

	void removeStar(StarDTO starDTO);

	// 내가 좋아요한 게시글 목록 조회
	List<MyStarPostListDTO> findLikedPostsByMemberId(String memberId);

	// 좋아요 or 싫어요 여부 조회
	Integer existsStar(StarDTO starDTO);

}
