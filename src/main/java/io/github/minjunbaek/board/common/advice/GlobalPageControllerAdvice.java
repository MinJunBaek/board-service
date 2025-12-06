package io.github.minjunbaek.board.common.advice;

import io.github.minjunbaek.board.domain.board.controller.dto.BoardResponseDto;
import io.github.minjunbaek.board.domain.board.service.BoardService;
import io.github.minjunbaek.board.security.MemberPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequiredArgsConstructor
@ControllerAdvice(annotations = Controller.class)
public class GlobalPageControllerAdvice {

  private final BoardService boardService;

  /**
   * 모든 @Controller에서 공통으로 사용할 Model 속성을 추가한다. 네비게이션용 게시판 목록, 로그인 정보
   */
  @ModelAttribute
  public void addGlobalAttributes(@AuthenticationPrincipal MemberPrincipal memberPrincipal, Model model) {

    // 네비게이션용 - 게시판 목록 조회
    List<BoardResponseDto> boards = boardService.readAllBoard();
    model.addAttribute("boards", boards);

    // 네비게이션용 - 로그인 상태 정보
    if (memberPrincipal != null) {
      model.addAttribute("loggedIn", true);
      model.addAttribute("memberPrincipalId", memberPrincipal.getId());
      model.addAttribute("memberPrincipalName", memberPrincipal.getName());
    } else {
      model.addAttribute("loggedIn", false);
    }
  }
}
