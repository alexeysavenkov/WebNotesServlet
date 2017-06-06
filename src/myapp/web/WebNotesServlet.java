package myapp.web;


import http.server.servlet.AbstractServlet;
import http.server.HttpRequest;
import http.server.HttpResponse;
import http.server.Request;
import http.server.Response;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

public class WebNotesServlet extends AbstractServlet {

	private DBCollection notesCollection;

    @Override
    public void init() {
        System.out.println("---from Servlet init---");
        
        Mongo mongo = new Mongo();
        DB db = mongo.getDB("notesDB");
        notesCollection = db.getCollection("notes");
        if (notesCollection == null) {
            notesCollection = db.createCollection("notes", null);
        }
    }

    @Override
    public void service(Request request, Response response) throws IOException {
        System.out.println("---from Servlet service---");
        
        HttpRequest httpRequest = (HttpRequest)request;
        if(httpRequest.getMethod().equals("POST")) {
        	String noteText = httpRequest.getParameter("note");
        	if(noteText != null && noteText.length() > 0) {
        		notesCollection.insert(new BasicDBObject("text", noteText));
        	}
        }
        
        StringBuilder builder = new StringBuilder();
        
        
        List<String> notes = new ArrayList<>();
        DBCursor cursor = notesCollection.find();
        while (cursor.hasNext()) {
            DBObject dbo = cursor.next();
            notes.add((String)dbo.get("text"));
        }
        
        
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
