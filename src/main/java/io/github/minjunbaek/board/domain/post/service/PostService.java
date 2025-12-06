package io.github.minjunbaek.board.domain.post.service;

import io.github.minjunbaek.board.common.error.BoardErrorCode;
import io.github.minjunbaek.board.common.exception.ApiException;
import io.github.minjunbaek.board.domain.board.repository.entity.Board;
import io.github.minjunbaek.board.domain.board.service.BoardService;
import io.github.minjunbaek.board.domain.member.repository.entity.Member;
import io.github.minjunbaek.board.domain.member.service.MemberService;
import io.github.minjunbaek.board.domain.post.controller.dto.PostListResponseDto;
import io.github.minjunbaek.board.domain.post.controller.dto.PostRequestDto;
import io.github.minjunbaek.board.domain.post.controller.dto.PostResponseDto;
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

  private final MemberService memberService;
  private final BoardService boardService;
  private final PostRepository postRepository;

  // 게시글 생성
  public Long createPost(Long memberId, PostRequestDto requestDto) {
    Member member = memberService.findMember(memberId);
    Board board = boardService.findBoard(requestDto.getBoardId());
    Post post = Post.create(requestDto.getTitle(), requestDto.getContent(), member, board);
    postRepository.save(post);
    return post.getId();
  }

  // 게시글 조회(단일 조회)
  public PostResponseDto readPost(Long postId) {
    Post post = findPost(postId);
    post.increaseViewCount(); // 조회수(1) 증가
    PostResponseDto responseDto = PostResponseDto.of(
        post.getId(), post.getTitle(), post.getContent(), post.getLikeCount(), post.getViewCount(),
        post.getMember().getId(), post.getMember().getName(), post.getBoard().getId(), post.getCreatedAt());
    return responseDto;
  }

  // 특정 사용자의 게시글 다수 조회
  @Transactional(readOnly = true)
  public List<PostListResponseDto> readAllMemberPost(Long memberId) {
    List<PostListResponseDto> postResponseDtoList = postRepository.findAllWithMemberByMemberId(memberId).stream()
        .map(post -> PostListResponseDto.of(
            post.getId(), post.getTitle(), post.getLikeCount(), post.getViewCount(), post.getMember().getId(),
            post.getMember().getName())).toList();
    return postResponseDtoList;
  }

  // 특정 게시판의 게시글 다수 조회
  @Transactional(readOnly = true)
  public List<PostListResponseDto> readAllPost(Long boardId) {
    boardService.findBoard(boardId);

    List<Post> postList = postRepository.findAllWithMemberByBoardId(boardId);

    List<PostListResponseDto> postResponseDtoList = postList.stream()
        .map(post -> PostListResponseDto.of(
            post.getId(), post.getTitle(), post.getLikeCount(), post.getViewCount(), post.getMember().getId(),
            post.getMember().getName())).toList();
    return postResponseDtoList;
  }

  // 전체 게시글 다수 조회
  @Transactional(readOnly = true)
  public List<PostListResponseDto> readAllPost() {
    return postRepository.findAllWithMember().stream().map(post -> PostListResponseDto.of(post.getId(), post.getTitle(),
        post.getLikeCount(), post.getViewCount(), post.getMember().getId(), post.getMember().getName())).toList();
  }

  // 게시글 수정용 조회(단일 조회)
  @Transactional(readOnly = true)
  public PostResponseDto editReadPost(Long postId) {
    Post post = findPost(postId);
    PostResponseDto responseDto = PostResponseDto.of(
        post.getId(), post.getTitle(), post.getContent(), post.getLikeCount(), post.getViewCount(),
        post.getMember().getId(), post.getMember().getName(), post.getBoard().getId(), post.getCreatedAt());
    return responseDto;
  }

  // 게시글 수정
  public PostResponseDto editPost(Long postId, PostRequestDto postRequestDto, Long memberId) {

    Post post = findPost(postId);

    postPermissionCheck(post.getMember().getId(), memberId);

    Board board = boardService.findBoard(postRequestDto.getBoardId());
    post.changeBoard(board);
    post.changeTitle(postRequestDto.getTitle());
    post.changeContent(postRequestDto.getContent());

    PostResponseDto postResponseDto = PostResponseDto.of(
        post.getId(), post.getTitle(), post.getContent(), post.getLikeCount(), post.getViewCount(),
        post.getMember().getId(), post.getMember().getName(), post.getBoard().getId(), post.getCreatedAt());
    return postResponseDto;
  }

  // 게시글 삭제
  public Long deletePost(Long postId, Long memberId) {
    Post post = findPost(postId);
    postPermissionCheck(post.getMember().getId(), memberId);
    Long boardId = post.getBoard().getId();
    postRepository.delete(post);
    return boardId;
  }

  // 해당 게시글 찾기
  public Post findPost(Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new ApiException(BoardErrorCode.POST_NOT_FOUND));
    return post;
  }

  // 수정 권한 확인
  private void postPermissionCheck(Long postMemberId, Long memberId){
    if (!postMemberId.equals(memberId)) {
      throw new ApiException(BoardErrorCode.POST_NO_PERMISSION);
    }
  }
}
