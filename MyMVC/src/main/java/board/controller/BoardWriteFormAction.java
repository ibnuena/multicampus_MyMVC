package board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractAction;

public class BoardWriteFormAction extends AbstractAction {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("BoardWriteFormAction");
		
		// 뷰페이지 지정
		this.setViewName("/board/input.jsp");
		
		// 이동 방식 지정
		this.setRedirect(false);
		
	}

}
