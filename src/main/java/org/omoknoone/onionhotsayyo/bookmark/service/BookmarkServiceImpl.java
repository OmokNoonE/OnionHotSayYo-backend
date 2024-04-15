package org.omoknoone.onionhotsayyo.bookmark.service;

import org.modelmapper.ModelMapper;
import org.omoknoone.onionhotsayyo.bookmark.aggregate.Bookmark;
import org.omoknoone.onionhotsayyo.bookmark.dto.BookmarkDTO;
import org.omoknoone.onionhotsayyo.bookmark.repository.BookmarkRepository;
import org.omoknoone.onionhotsayyo.exceptions.BookmarkNotFoundException;
import org.omoknoone.onionhotsayyo.follow.aggregate.Follow;
import org.omoknoone.onionhotsayyo.follow.dto.FollowDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.util.List;

@Service
public class BookmarkServiceImpl implements BookmarkService{

	private static final Logger log = LoggerFactory.getLogger(BookmarkServiceImpl.class);
	private final BookmarkRepository bookmarkRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public BookmarkServiceImpl(BookmarkRepository bookmarkRepository, ModelMapper modelMapper) {
		this.bookmarkRepository = bookmarkRepository;
		this.modelMapper = modelMapper;
	}

	@Transactional
	@Override
	public void addBookmark(BookmarkDTO bookmarkDTO) {
		if(!existsBookmark(bookmarkDTO)) {
		log.info("북마크 추가 시작, 회원 ID {}", bookmarkDTO.getMemberId());
		Bookmark bookmark = modelMapper.map(bookmarkDTO, Bookmark.class);
		bookmarkRepository.save(bookmark);
		} else {
			throw new RuntimeException("이미 북마크 한 게시글입니다.");
		}
	}

	@Transactional
	@Override
	public void removeBookmark(BookmarkDTO bookmarkDTO) {
		if(existsBookmark(bookmarkDTO)){
		Bookmark bookmark = modelMapper.map(bookmarkDTO, Bookmark.class);
		bookmarkRepository.delete(bookmark);
		} else {
			throw new RuntimeException("삭제할 북마크가 없습니다.");
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<Bookmark> findBookmarkSByMemberId(String memberId) {
		log.info("회원 ID {}에 대한 북마크 목록 조회 시작", memberId);

		List<Bookmark> bookmarks = bookmarkRepository.findByMemberId(memberId);
		if (bookmarks.isEmpty()) {
			log.warn("회원 ID {}에 대한 북마크 목록이 존재하지 않습니다.", memberId);
			throw new BookmarkNotFoundException("북마크 목록을 찾을 수 없습니다.");
		}
		log.info("회원 ID {}에 대한 북마크 목록 조회 완료: {}건 발견", memberId, bookmarks.size());

		return bookmarks;
	}


	@Override
	public boolean existsBookmark(BookmarkDTO bookmarkDTO) {
		List<Bookmark> bookmarkList = bookmarkRepository.findAll();

		for (Bookmark bookmark : bookmarkList) {
			if(bookmark.getMemberId().equals(bookmarkDTO.getMemberId()) &&
				bookmark.getPostId().equals(bookmarkDTO.getPostId())) {
				return true;
			}
		}
		return false;
	}
}
