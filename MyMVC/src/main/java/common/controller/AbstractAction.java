package common.controller;

// 추상 클래스
public abstract class AbstractAction implements Action {
	
	private String viewName; // 보여줄 뷰 페이지 (jsp) 이름
	private boolean isRedirect = false; // true면 redirect 방식으로 이동, false면 forward 방식
	
	// execute() 추상 메서드를 가지고 있음
	
	
	// setter, getter
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public boolean isRedirect() {
		return isRedirect;
	}
	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}

}
