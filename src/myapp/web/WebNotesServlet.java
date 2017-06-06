package myapp.web;


import http.server.servlet.AbstractServlet;
import http.server.HttpRequest;
import http.server.HttpResponse;
import http.server.Request;
import http.server.Response;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class WebNotesServlet extends AbstractServlet {

    List<String> notes = new ArrayList<>();

    @Override
    public void init() {
        System.out.println("---from Servlet init---");
    }

    @Override
    public void service(Request request, Response response) throws IOException {
        System.out.println("---from Servlet service---");
        
        HttpRequest httpRequest = (HttpRequest)request;
        if(httpRequest.getMethod().equals("POST")) {
        	String note = httpRequest.getParameter("note");
        	if(note != null && note.length() > 0) {
        		notes.add(note);
        	}
        }
        
        StringBuilder builder = new StringBuilder();
        
        if(notes.isEmpty()) {
        	builder.append("<h2>No saved notes yet</h2>");
        } else {
        	builder.append("<h2>Saved notes: </h2>");
        	builder.append("<ul>");
        	for(String note : notes) {
        		builder.append("<li>" + note + "</li>");
        	}
        	builder.append("</ul>");
        }
        
        builder.append("<h2>Add new note</h2>");
        
        builder.append("<form action='servlet/WebNotesServlet' method='post'>" +
			"Note text: <input type='text' name='note'><br>" + 
			"<input type='submit' value='Submit'>" +
			"</form>");

        ((HttpResponse)response).sendHTML(builder.toString());
    }
    
    

    @Override
    public void destroy() {
        System.out.println("---from Servlet destroy---");
    }

}
