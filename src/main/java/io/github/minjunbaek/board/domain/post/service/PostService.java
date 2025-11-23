package io.github.minjunbaek.board.domain.post.service;

import io.github.minjunbaek.board.common.error.BoardErrorCode;
import io.github.minjunbaek.board.common.error.MemberErrorCode;
import io.github.minjunbaek.board.common.exception.ApiException;
import io.github.minjunbaek.board.domain.board.repository.BoardRepository;
import io.github.minjunbaek.board.domain.board.repository.entity.Board;
import io.github.minjunbaek.board.domain.member.repository.MemberRepository;
import io.github.minjunbaek.board.domain.member.repository.entity.Member;
import io.github.minjunbaek.board.domain.post.contoller.dto.PostListResponseDto;
import io.github.minjunbaek.board.domain.post.contoller.dto.PostRequestDto;
import io.github.minjunbaek.board.domain.post.contoller.dto.PostResponseDto;
import io.github.minjunbaek.board.domain.post.repository.PostRepository;
import io.github.minjunbaek.board.domain.post.repository.entity.Post;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

  private final MemberRepository memberRepository;
  private final BoardRepository boardRepository;
  private final PostRepository postRepository;

  // 게시글 생성
  public void createPost(Long memberId, PostRequestDto requestDto) {
    Member member = findMember(memberId);
    Board board = findBoard(requestDto.getBoardId());
    Post post = Post.create(requestDto.getTitle(), requestDto.getContent(), member, board);
    postRepository.save(post);
  }

  // 게시글 조회(단일 조회)
  public PostResponseDto readPost(Long postId) {
    Post post = findPost(postId);
    post.increaseViewCount();
    PostResponseDto responseDto = PostResponseDto.of(
        post.getId(), post.getTitle(), post.getContent(), post.getLikeCount(), post.getViewCount());
    return responseDto;
  }

  // 게시글 조회(사용자 글 다수 조회)
  @Transactional(readOnly = true)
  public List<PostListResponseDto> readAllMemberPost(Long memberId) {
    List<PostListResponseDto> postResponseDtoList = postRepository.findAllByMemberId(memberId).stream()
        .map(post -> PostListResponseDto.of(
            post.getId(), post.getTitle(), post.getLikeCount(), post.getViewCount())).toList();
    return postResponseDtoList;
  }

  // 게시글 조회(게시판 글 다수 조회)
  @Transactional(readOnly = true)
  public List<PostListResponseDto> readAllPost(Long boardId) {
    List<PostListResponseDto> postResponseDtoList = postRepository.findAllByBoardId(boardId).stream()
        .map(post -> PostListResponseDto.of(
            post.getId(), post.getTitle(), post.getLikeCount(), post.getViewCount())).toList();
    return postResponseDtoList;
  }

  // 게시글 수정
  public PostResponseDto editPost(Long postId, PostRequestDto postRequestDto, Long memberId) {
    Post post = findPost(postId);
    if (!post.getMember().getId().equals(memberId)) {
      throw new ApiException(BoardErrorCode.POST_NO_PERMISSION);
    }
    Board board = findBoard(postRequestDto.getBoardId());
    post.changeBoard(board);
    post.changeTitle(postRequestDto.getTitle());
    post.changeContent(postRequestDto.getContent());

    PostResponseDto postResponseDto = PostResponseDto.of(post.getId(), post.getTitle(), post.getContent(),
        post.getLikeCount(), post.getViewCount());
    return postResponseDto;
  }

  // 게시글 삭제
  public void deletePost(Long postId, Long memberId) {
    Post post = findPost(postId);
    if (!post.getMember().getId().equals(memberId)) {
      throw new ApiException(BoardErrorCode.POST_NO_PERMISSION);
    }
    postRepository.delete(post);
  }

  private Member findMember(Long memberId) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new ApiException(MemberErrorCode.MEMBER_NOT_FOUND));
    return member;
  }

  private Board findBoard(Long boardId) {
    Board board = boardRepository.findById(boardId)
        .orElseThrow(() -> new ApiException(BoardErrorCode.BOARD_NOT_FOUND));
    return board;
  }

  private Post findPost(Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new ApiException(BoardErrorCode.POST_NOT_FOUND));
    return post;
  }
}
