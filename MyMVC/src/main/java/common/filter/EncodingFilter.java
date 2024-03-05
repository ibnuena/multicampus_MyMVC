package common.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpFilter;

// [1] web.xml에 등록하는 방법
/*
 * <filter>
  	<filter-name>encodingFilter</filter-name>
  	<filter-class>common.filter.EncodingFilter</filter-class>
  	<init-param>
  		<param-name>encoding</param-name>
  		<param-value>UTF-8</param-value>
  	</init-param>
  </filter>
  <filter-mapping>
  	<filter-name>encodingFilter</filter-name>
  	<url-pattern>/*</url-pattern>
  </filter-mapping>
 * */


// [2]

@WebFilter(
		urlPatterns = { "/*" }, 
		initParams = { 
				@WebInitParam(name = "encoding", value = "UTF-8")
		})
public class EncodingFilter extends HttpFilter implements Filter {
    
	private String charset;
	
	public void init(FilterConfig fConfig) throws ServletException {
		charset=fConfig.getInitParameter("encoding");
//		System.out.println("EncodingFilter init()... charset : " + charset);
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		System.out.println("Encoding doFilter()");
		request.setCharacterEncoding(charset);
		chain.doFilter(request, response);
	}


	public void destroy() {
//		System.out.println("EncodingFilter destroy()");
	}

}
