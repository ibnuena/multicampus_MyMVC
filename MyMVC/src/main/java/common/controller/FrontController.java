package common.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*FrontController : *.do 패턴의 모든 요청을 받아들인다.
 * - command.properties 파일에 있는 매핑 정보를 읽어들여 해당 요청uri와 매핑되어 있는
 *   SubController(XXXAction)을 찾아 객체화 한 뒤 해당 객체의 메소드(execute)를 호출한다.
 * - 서브 컨트롤러는 해당 작업을 수행한 뒤에 다시 FrontController로 돌아와 보여줘야 할 View
 *   페이지(JSP) 정보를 넘긴다.
 * - FrontController는 해당 뷰페이지로 이동시킨다. (forward방식 이동 or redirect방식 이동)    
 * */

@WebServlet(
		urlPatterns = { "*.do" }, 
		initParams = { 
				@WebInitParam(name = "config", 
						value = "C:\\Users\\admin\\eclipse-workspace\\MyMVC\\src\\main\\webapp\\WEB-INF\\command.properties")
		})
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private HashMap<String, Object> cmdMap = new HashMap<>();
	// command.properties 파일에 있는 값들을 해시맵에 옮긴다
	
	private AbstractAction action = null;
	
	public void init(ServletConfig conf) throws ServletException {
		System.out.println("init()...");
		String propsPath = conf.getInitParameter("config");
		System.out.println("prosPath: " + propsPath);
		
		Properties pr = new Properties();
		
		try {
			FileInputStream fis = new FileInputStream(propsPath);
			pr.load(fis);
			
			if(fis!=null) fis.close();
//			System.out.println(pr.getProperty("/index.do")); // 클래스명을 문자열로 반환
			
			//pr에서 key값들을 먼저 추출
			Set<Object> set = pr.keySet();
			if(set!=null) {
				for(Object key : set) {
					String cmd = key.toString(); // "/index.do"
					String className = pr.getProperty(cmd); // "common.controller.IndexAction"
					if(className!=null) {
						className = className.trim(); // 앞 뒤 공백 제거
						
					}
					System.out.println(cmd + ": " + className);
					
					// className을 실제 객체로 인스턴스화
					Class<?> cls = Class.forName(className);
					Object cmdInstance = cls.getDeclaredConstructor().newInstance();
					// 해당 클래스의 객체를 생성해줌
					/////////////////////////////
					cmdMap.put(cmd, cmdInstance);
					/////////////////////////////
				}
				System.out.println("cmpMap 저장 완료 : cmdMap.size() -> " + cmdMap.size());
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	} // init ----------------------------------------------------------

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);

	}

	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 클라이언트의 요청 URI를 분석
		// 컨텍스트명 이후 경로 얻기
		String cmd = request.getServletPath(); // 컨텍스트명을 제외한 나머지 경로 추출
		System.out.println("cmd : " + cmd);
		Object instance = cmdMap.get(cmd);
		
		if(instance==null) {
			System.out.println("Action이 null");
			throw new ServletException("Action이 null입니다.");
		}
		System.out.println("instance===" + instance);
		/////////////////////////////////////////////////
		if(instance instanceof AbstractAction) {
			action = (AbstractAction)instance;
		}
		/////////////////////////////////////////////////
		
		try {
			action.execute(request, response);
			// execute()에서는 로직을 수행한 후
			// viewPage를 setting, isRedirect값도 setting
			String viewPage = action.getViewName();
			boolean isRedirect = action.isRedirect();
			if(isRedirect) {
				// redirect 방식으로 이동
				response.sendRedirect(viewPage);
				return;
			}else {
				// forward 방식으로 이동
				RequestDispatcher disp = request.getRequestDispatcher(viewPage);
				disp.forward(request, response);
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

}
