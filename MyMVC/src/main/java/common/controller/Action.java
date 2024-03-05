package common.controller;

import javax.servlet.http.*;

public interface Action {
	// 추상메서드
	void execute(HttpServletRequest req, HttpServletResponse res) throws Exception;
}
