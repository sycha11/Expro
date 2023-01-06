package com.spring.view.board;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.spring.biz.board.BoardService;
import com.spring.biz.board.BoardVO;
import com.spring.biz.board.impl.BoardDAO;


@Controller
@SessionAttributes("board")
public class BoardController {
	@Autowired
	private BoardService boardService;
//	Logger logger = Logger.getLogger(this.getClass());

	// 글 등록
	@RequestMapping(value="insertBoard.do")
	public String insertBoard(BoardVO vo, BoardDAO boardDAO) throws IOException {

		System.out.println("글 등록 처리");
//		logger.debug("[LOG] 글 등록 처리");

		// 파일 업로드 처리
		MultipartFile uploadFile = vo.getUploadFile();
		if (!uploadFile.isEmpty()) {
			String filename = uploadFile.getOriginalFilename();
			uploadFile.transferTo(new File("C:/" + filename));
		}
		
		boardService.insertBoard(vo);
		
		return "getBoardList.do";
	}

	// 글 수정
	@RequestMapping(value="updateBoard.do")
	public String updateBoard(@ModelAttribute("board") BoardVO vo, BoardDAO boardDAO) {

		System.out.println("글 수정 처리");
//		logger.debug("[LOG] 글 수정 처리");

		System.out.println("번호 : " + vo.getSeq());
		System.out.println("제목 : " + vo.getTitle());
		System.out.println("작성자 : " + vo.getWriter());
		System.out.println("내용 : " + vo.getContent());
		System.out.println("등록일 : " + vo.getRegDate());
		System.out.println("조회수 : " + vo.getCnt());
		boardService.updateBoard(vo);
		
		return "getBoardList.do";
	}

	// 글 삭제
	@RequestMapping(value="deleteBoard.do")
	public String deleteBoard(BoardVO vo, BoardDAO boardDAO) {

		System.out.println("글 삭제 처리");
//		logger.debug("[LOG] 글 삭제  처리");
		boardService.deleteBoard(vo);
		
		return "getBoardList.do";
	}
	
	// 검색 조건 목록 설정
	@ModelAttribute("conditionMap")
	public Map<String, String> searchConditionMap() {
		Map<String, String> conditionMap = new HashMap<String, String>();
		conditionMap.put("제목", "TITLE");
		conditionMap.put("내용", "CONTENT");
		return conditionMap;
	}

	// 글 상세 조회
	@RequestMapping(value="/getBoard.do")
	public String getBoard(BoardVO vo, BoardDAO boardDAO, Model model) {

		System.out.println("글 상세 조회 처리");
//		logger.debug("[LOG] 글 상세 조회 처리");
	
		// 검색 결과를 세션에 저장하고 목록 화면으로 이동한다.
		model.addAttribute("board", boardService.getBoard(vo));
		
		return "getBoard.jsp";
	}

	// 글 목록 검색
	@RequestMapping(value="getBoardList.do")
//	public String getBoardList(@RequestParam(value="searchCondition", 
//			defaultValue="TITLE", required=false) String condition,
//			@RequestParam(value="searchKeyword", defaultValue="", required=false) String keyword,
//			BoardVO vo, BoardDAO boardDAO, Model model) {
	public String getBoardList(BoardVO vo, BoardDAO boardDAO, Model model) {
		
//		System.out.println("검색 조건 : " + condition);
		System.out.println("검색 단어 : " + vo.getSearchKeyword());
//		logger.debug("[LOG] 글 목록 검색");
		
		// NULL Check
		if (vo.getSearchCondition() == null) vo.setSearchCondition("TITLE");
		if (vo.getSearchKeyword() == null) vo.setSearchKeyword("");
		
		// Model 정보 저장
		model.addAttribute("boardList", boardService.getBoardList(vo));	// Model 정보 저장
		return "getBoardList.jsp";
	}

}
