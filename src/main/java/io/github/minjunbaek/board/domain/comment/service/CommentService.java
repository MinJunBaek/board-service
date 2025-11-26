package io.github.minjunbaek.board.domain.comment.service;

import io.github.minjunbaek.board.common.error.BoardErrorCode;
import io.github.minjunbaek.board.common.exception.ApiException;
import io.github.minjunbaek.board.domain.comment.controller.dto.CommentRequestDto;
import io.github.minjunbaek.board.domain.comment.controller.dto.CommentResponseDto;
import io.github.minjunbaek.board.domain.comment.repository.CommentRepository;
import io.github.minjunbaek.board.domain.comment.repository.entity.Comment;
import io.github.minjunbaek.board.domain.member.repository.entity.Member;
import io.github.minjunbaek.board.domain.member.service.MemberService;
import io.github.minjunbaek.board.domain.post.repository.entity.Post;
import io.github.minjunbaek.board.domain.post.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostService postService; // 조회 전용 클래스를 만들어서 책임을 넘길까?
  private final MemberService memberService;

  // 댓글 조회 (다수)
  @Transactional(readOnly = true)
  public List<CommentResponseDto> viewAllComment(Long postId) {
    List<Comment> commentList = commentRepository.findAllByPostId(postId);
    List<CommentResponseDto> commentResponseDtoList = commentList.stream().map(comment -> CommentResponseDto.of(
        comment.getId(),
        comment.getContent(),
        comment.getMember().getId(),
        comment.getMember().getName(),
        comment.getUpdatedAt()
        )).toList();
    return commentResponseDtoList;
  }

  // 댓글 등록
  public void createComment(Long postId, Long memberId, CommentRequestDto commentRequestDto) {
    Member member = memberService.findMember(memberId);
    Post post = postService.findPost(postId);
    Comment comment = Comment.create(commentRequestDto.getContent(), member, post);
    commentRepository.save(comment);
  }

  // 권한 필요: 댓글의 수정 삭제 기능은 해당 유저만 권한을 갖는다.

  // 댓글 수정 (권한 필요)
  public CommentResponseDto editComment(Long postId, Long commentId, Long memberId, CommentRequestDto commentRequestDto) {
    Comment comment = findComment(commentId);

    // 권한체크
    permissionCheck(comment, memberId);

    // 댓글 수정 및 저장
    comment.changeContent(commentRequestDto.getContent());
    commentRepository.save(comment);
    return CommentResponseDto.of(comment.getId(), comment.getContent(), comment.getMember().getId(),
        comment.getMember().getName(), comment.getUpdatedAt());
  }

  // 댓글 삭제 (권한 필요)
  public void deleteComment(Long commentId, Long memberId) {
    Comment comment = findComment(commentId);

    // 권한 체크
    permissionCheck(comment, memberId);

    commentRepository.delete(comment);
  }

  public Comment findComment(Long commentId) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new ApiException(BoardErrorCode.COMMENT_NOT_FOUND));
    return comment;
  }

  private void permissionCheck(Comment comment, Long memberId) {
    if (!comment.getMember().getId().equals(memberId)) {
      throw new ApiException(BoardErrorCode.COMMENT_NO_PERMISSION);
    }
  }
}
