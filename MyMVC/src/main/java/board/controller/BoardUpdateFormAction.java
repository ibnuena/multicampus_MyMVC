package board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.BoardDAO;
import board.model.BoardVO;
import common.controller.AbstractAction;

public class BoardUpdateFormAction extends AbstractAction {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 1. 글번호, 비번 받기
		String numStr = req.getParameter("num");
		String passwd = req.getParameter("passwd");
		
		// 2. 유효성 체크
		if(numStr==null||passwd==null||numStr.trim().isBlank()||passwd.trim().isBlank()) {
			this.setRedirect(true);
			this.setViewName("list.do");
		}
		int num = Integer.parseInt(numStr.trim());
		
		// 3. 해당 글 db에서 가져오기
		BoardDAO dao = new BoardDAO();
		BoardVO tmp = dao.getBoard(num);
		
		// 4. 비밀번호 일치 체크
		// 비번 일치시 3번에서 가져온 vo객체 req에 저장
		if(tmp.getPasswd().equals(passwd)) {
			req.setAttribute("board", tmp);
			this.setViewName("/board/edit.jsp");
			this.setRedirect(false);
		}
		else {
			req.setAttribute("msg", "비밀번호가 일치하지 않아요.");
			req.setAttribute("loc", "javascript:history.back()");
			this.setViewName("/board/message.jsp");
			this.setRedirect(false);
		}
		this.setViewName("/board/edit.jsp");
		this.setRedirect(false);
		
	}

}
