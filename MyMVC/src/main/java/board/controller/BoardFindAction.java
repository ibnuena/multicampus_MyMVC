package board.controller;

import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.BoardDAO;
import board.model.BoardVO;
import common.controller.AbstractAction;

public class BoardFindAction extends AbstractAction {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 0. pageNum 파라미터값 받기
		String pageNumStr = req.getParameter("pageNum");
		if(pageNumStr==null) {
			pageNumStr = "1"; // 디폴트 페이지 1페이지로 지정
		}
		
		int pageNum = Integer.parseInt(pageNumStr.trim());
		if(pageNum<1) {
			pageNum=1;
		}
		
		// 검색 유형과 검색어 받기
		String findType = req.getParameter("findType");
		String findKeyword = req.getParameter("findKeyword");
		
		if(findType.equals("1")) {
			findType = "title";
		}
		else if(findType.equals("2")) {
			findType = "name";
		}
		else if(findType.equals("3")) {
			findType = "content";
		}
		else if(findType.equals("0")) {
			req.setAttribute("msg", "검색 유형을 선택하세요");
			req.setAttribute("loc", "javascript:history.back()");
			this.setViewName("/board/message.jsp");
			this.setRedirect(false);
			return;
		}
		// BoardDAO 생성 listBoard() 호출
		BoardDAO dao = new BoardDAO();
		
		// 1. 총 게시글 수 가져오기
		int totalCount = dao.getFindTotalCount(findType, findKeyword);
		
		// 2. 한 페이지당 보여줄 목록 개수 정하기
		int oneRecordPage = 7;
		
		// 3. 총 페이지 수 구하기
		int pageCount = (totalCount - 1) / oneRecordPage + 1;
		// 4. jsp에서 페이지 네비게이션 출력 -> 링크 -> pageNum 파라미터 전달
		if(pageNum>pageCount) {
			pageNum = pageCount; // 마지막 페이지로 지정
		}
		
		// 5. pageNum을 이용해서 DB에서 끊어올 범위 정하기
		int end = pageNum * oneRecordPage;
		int start = end - (oneRecordPage-1);
		
		// 6. 게시글 목록 가져오기
		List<BoardVO> boardList = dao.findBoard(start, end, findType, findKeyword);
		
		
		// 반환하는 List<BoardVO> 객체를 req에 저장
		req.setAttribute("boardAll", boardList);
		req.setAttribute("totalCount", totalCount);
		req.setAttribute("pageCount", pageCount);
		req.setAttribute("pageNum", pageNum);
		req.setAttribute("findKeyword", findKeyword);
		req.setAttribute("findType", findType);

		// 뷰페이지 지정
		this.setViewName("find.jsp");
				
		// 이동 방식 지정
		this.setRedirect(false);
	}

}
