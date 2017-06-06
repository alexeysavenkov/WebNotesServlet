import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class PrimitiveServlet implements Servlet {

  public void init(ServletConfig config) throws ServletException {
    System.out.println("---from Servlet init---");
  }

  public void service(ServletRequest request, ServletResponse response)
    throws ServletException, IOException {
	  
    System.out.println("---from Servlet service---");
    PrintWriter out = response.getWriter();
    
    String successHeader = "HTTP/1.1 200 OK\r\n"
            + "Content-Type: text/html\r\n"
            + "Content-Length: " + 2 + "\r\n"
            + "\r\n";
    out.print(successHeader.getBytes());
    out.println("OK");
    
    
//    out.println("<b>Hello. Roses are red.</b>");
//    out.print("<i>Violets are blue.</i>");
  }

  public void destroy() {
    System.out.println("---from Servlet destroy---");
  }

  public String getServletInfo() {
    return null;
  }
  public ServletConfig getServletConfig() {
    return null;
  }

}
