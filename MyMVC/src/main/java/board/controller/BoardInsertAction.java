package board.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import board.model.BoardDAO;
import board.model.BoardVO;
import common.controller.AbstractAction;

public class BoardInsertAction extends AbstractAction {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 0. 파일 업로드 처리
		// [1] 업로드할 디렉토리의 절대경로 얻기
		ServletContext application = req.getServletContext();
		String upDir = application.getRealPath("/upload");
		System.out.println("upDir : " + upDir);
		// [2] cos.jar의 MultipartRequest 객체를 생성 -> 업로드 처리를 해줌
		MultipartRequest mreq = null;
		try {
			// 동일한 파일명일때, 파일명에 인덱스 번호를 붙임 -> 덮어쓰기 방지
			DefaultFileRenamePolicy df = new DefaultFileRenamePolicy();
			
			mreq = new MultipartRequest(req, upDir, 100*1024*100, "utf-8", df);	// 업로드 최대 용량 : 100mb
		} catch (IOException e) {
			System.out.println("파일 업로드 실패 : " + e);
//			throw new ServletException(e);
			return;
		}
		
		// 1. 사용자가 입력한 값 받기 -> multipartRequest를 이용해 파라미터값 추출
		String name = mreq.getParameter("name");
		String passwd = mreq.getParameter("passwd");
		String title = mreq.getParameter("title");
		String content = mreq.getParameter("content");
		System.out.println("name : " + name);

		// 첨부파일명 -> getFilesystemName("파라미터명")으로 얻는다. getParameter x
		String fileName = mreq.getFilesystemName("fileName");
		File file = mreq.getFile("fileName");
		long fileSize = 0;
		if(file!=null) {
			fileSize = file.length(); // 파일 크기
		}
		
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
