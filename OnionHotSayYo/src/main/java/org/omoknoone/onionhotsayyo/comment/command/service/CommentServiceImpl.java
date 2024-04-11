package org.omoknoone.onionhotsayyo.comment.command.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.omoknoone.onionhotsayyo.comment.command.aggregate.Comment;
import org.omoknoone.onionhotsayyo.comment.command.dto.CommentDTO;
import org.omoknoone.onionhotsayyo.comment.command.dto.CommentReplyDTO;
import org.omoknoone.onionhotsayyo.comment.command.repository.CommentRepository;
import org.omoknoone.onionhotsayyo.member.aggregate.Member;
import org.omoknoone.onionhotsayyo.member.dto.MemberDTO;
import org.omoknoone.onionhotsayyo.member.service.MemberService;
import org.omoknoone.onionhotsayyo.reply.command.dto.ReplyDTO;
import org.omoknoone.onionhotsayyo.reply.command.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {
	private final ModelMapper modelMapper;
	private final CommentRepository commentRepository;
	private final ReplyService replyService;
	private final MemberService memberService;

	@Autowired
	public CommentServiceImpl(ModelMapper modelMapper, CommentRepository commentRepository, ReplyService replyService, MemberService memberService) {
		this.modelMapper = modelMapper;
		this.commentRepository = commentRepository;
        this.replyService = replyService;
        this.memberService = memberService;
    }

	@Transactional
	@Override
	public void createComment(CommentDTO commentDTO) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Comment comment = modelMapper.map(commentDTO, Comment.class);
		commentRepository.save(comment);

	}

	@Transactional
	@Override
	public void modifyComment(CommentDTO commentDTO) {
		Comment findcomment = commentRepository.findById(commentDTO.getCommentId())
			.orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

		if(findcomment != null) {
		findcomment.setContent(commentDTO.getContent());
		findcomment.setLastModifiedDate(LocalDateTime.now());

		commentRepository.save(modelMapper.map(findcomment, Comment.class));
		}
	}

	@Transactional
	@Override
	public void removeComment(int commentId) {
		Comment findcomment = commentRepository.findById(commentId)
			.orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
		findcomment.setDeleted(true);
	}

	@Override
	public List<CommentReplyDTO> viewCommentListByMe(String memberId) {

		MemberDTO memberDTO = memberService.getMemberDetailsByMemberId(memberId);

		List<Comment> commentList = commentRepository.findAllByMemberId(memberId);
		List<CommentDTO> commentDTOList = modelMapper.map(commentList, new TypeToken<List<CommentDTO>>() {}.getType());

		List<ReplyDTO> replyDTOList = replyService.viewReplyListByMemberId(memberId);

		List<CommentReplyDTO> commentReplyDTOList = new ArrayList<>();
		for (CommentDTO commentDTO : commentDTOList) {
			CommentReplyDTO commentReplyDTO = new CommentReplyDTO(
					commentDTO.getMemberId(),
					memberDTO.getNickname(),
					commentDTO.getContent(),
					commentDTO.getPostedDate(),
					commentDTO.getLastModifiedDate(),
					commentDTO.isDeleted());
			commentReplyDTOList.add(commentReplyDTO);
		}

		for (ReplyDTO replyDTO : replyDTOList) {
			CommentReplyDTO commentReplyDTO = new CommentReplyDTO(
					replyDTO.getMemberId(),
					memberDTO.getNickname(),
					replyDTO.getContent(),
					replyDTO.getPostedDate(),
					replyDTO.getLastModifiedDate(),
					replyDTO.isDeleted());
			commentReplyDTOList.add(commentReplyDTO);
		}

		return commentReplyDTOList;
	}
}
