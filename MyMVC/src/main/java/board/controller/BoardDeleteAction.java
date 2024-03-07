package board.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.BoardDAO;
import board.model.BoardVO;
import common.controller.AbstractAction;

public class BoardDeleteAction extends AbstractAction {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 1. 글번호, 비밀번호 값 받기
		String numStr = req.getParameter("num");
		String passwd = req.getParameter("passwd");
		
		// 2. 유효성 체크 list.do로 이동
		if(numStr==null||passwd==null||numStr.trim().isBlank()||passwd.trim().isBlank()) {
			this.setRedirect(true);
			this.setViewName("list.do");
		}
		int num = Integer.parseInt(numStr.trim());
		
		// 3. 글번호로 해당글을 DB에서 가져오기 getBoard(num)
		BoardDAO dao = new BoardDAO();
		BoardVO tmp = dao.getBoard(num);
		
		// 4.1 해당글의 비밀번호와 사용자가 입력한 비밀번호가 일치하면 삭제 -> BoardDAO의 deleteBoard(num) -> msg, loc 처리  
		// 4.2 일치하지 않으면 history.back();
		String msg = "", loc = "";
		if(!passwd.equals(tmp.getPasswd())) {
			msg = "비밀번호가 일치하지 않아요";
			loc = "javascript:history.back()";
		}
		else {
			int n = dao.deleteBoard(num);
			msg = (n>0) ? "삭제 성공" : "삭제 실패";
			loc = (n>0) ? "list.do" : "javascript:history.back()";
			// 5. 서버에 업로드했던 파일이 있다면 서버에서 삭제 처리
			String fileName = tmp.getFileName(); // 첨부파일명
			if(fileName!=null) {
				String upDir = req.getServletContext().getRealPath("/upload");
				File delFile = new File(upDir, fileName);
				
				if(delFile.exists()) { // 파일이 있다면
					boolean b = delFile.delete();
					System.out.println("파일 삭제 여부 : " + b);
				}// if --------------
			}// if-----------------
		}// else----------------
		
		
		req.setAttribute("msg", msg);
		req.setAttribute("loc", loc);
		this.setViewName("/board/message.jsp");
		this.setRedirect(false);
	}

}
