package board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.BoardDAO;
import board.model.BoardVO;
import common.controller.AbstractAction;

public class BoardInsertAction extends AbstractAction {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 1. 사용자가 입력한 값 받기
		String name = req.getParameter("name");
		String passwd = req.getParameter("passwd");
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		// 첨부파일명, 파일크기 나중에
		String fileName = null;
		long fileSize = 0;
		
		// 2. 유효성 체크
		if(name==null || passwd==null || title==null || name.trim().isBlank() || passwd.trim().isBlank() || title.trim().isBlank()) {
			this.setRedirect(true);
			this.setViewName("input.do"); // input.do로 redirect 이동. 프론트컨트롤러가
			return;
		}
		
		// 3. 1번에서 받은 값 BoardVO 객체에 담기
		BoardVO vo = new BoardVO(0, name, passwd, title, content, null, 0, fileName, fileSize);
		
		// 4. BoardDAO 생성 후 insertBoard() 호출
		BoardDAO dao = new BoardDAO();
		int n = dao.insertBoard(vo);
		
		// 5. 그 결과 메세지
		String msg = (n>0) ? "글 등록 완료" : "글 등록 실패";
		String loc = (n>0) ? "list.do" : "javascript:history.back()";
		
		req.setAttribute("msg", msg);
		req.setAttribute("loc", loc);
		
		// 뷰페이지 지정
		this.setViewName("/board/message.jsp");

		// 이동 방식 지정
		this.setRedirect(false);
	}

}
