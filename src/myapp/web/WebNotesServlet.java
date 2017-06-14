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

import beans.Book;

public class WebNotesServlet extends AbstractServlet {

	public static DBCollection booksCollection;

    @Override
    public void init() {
        System.out.println("---from Servlet init---");
        
        Mongo mongo = new Mongo();
        DB db = mongo.getDB("notesDB");
        booksCollection = db.getCollection("books");
        if (booksCollection == null) {
            booksCollection = db.createCollection("books", null);
        }
    }

    @Override
    public void service(Request request, Response response) throws IOException {
        System.out.println("---from Servlet service---");
        
        HttpRequest httpRequest = (HttpRequest)request;
        if(httpRequest.getMethod().equals("POST")) {
        	switch(httpRequest.getParameter("action")) {
        	case "add":
	        	Book book = Book.fromHttpRequest(httpRequest);
	        	if(book != null) {
	        		booksCollection.insert(book.toDBObject());
	        	}
	        	break;
        	case "delete":
        		booksCollection.remove(new BasicDBObject("name", httpRequest.getParameter("name")));
        		break;
        	case "modify":
        		book = Book.fromDBObject(booksCollection.find(new BasicDBObject("name", httpRequest.getParameter("name"))).next());
        		((HttpResponse)response).sendHTML(("<form action='/servlet/WebNotesServlet' method='post'>" +
                		"<input type='hidden' name='action' value='modify_approve'>" + 
        				"<input type='hidden' name='old_name' value='" + book.getName() + "'>" +
        			"Name: <input type='text' name='name' value='" + book.getName() + "'><br>" + 
        			"Year: <input type='text' name='year' value='" + book.getYear() + "'><br>" + 
        			"Language: <input type='text' name='language' value='" + book.getLanguage() + "'><br>" + 
        			"Author: <input type='text' name='author' value='" + book.getAuthor() + "'><br>" + 
        			"<input type='submit' value='Submit'>" +
        			"</form>"));
        		return;
        	case "modify_approve":
        		book = Book.fromHttpRequest(httpRequest);
        		if(book != null) {
        			booksCollection.remove(new BasicDBObject("name", httpRequest.getParameter("old_name")));
	        		booksCollection.insert(book.toDBObject());
	        	}
        		break;
        	}
        }
        
        StringBuilder builder = new StringBuilder();
        
        
        List<Book> books = new ArrayList<>();
        DBCursor cursor = booksCollection.find();
        while (cursor.hasNext()) {
            DBObject dbo = cursor.next();
            Book book = Book.fromDBObject(dbo);
            books.add(book);
        }
        
        
        if(books.isEmpty()) {
        	builder.append("<h2>No books yet</h2>");
        } else {
        	builder.append("<h2>Books: </h2>");
        	builder.append("<table>");
        	builder.append("<tr><th>Name</th><th>Year</th><th>Language</th><th>Author</th><th></th><th></th></tr>");
        	for(Book book : books) {
        		builder.append("<tr>");
        		builder.append("<td>" + book.getName() + "</td>");
        		builder.append("<td>" + book.getYear() + "</td>");
        		builder.append("<td>" + book.getLanguage() + "</td>");
        		builder.append("<td>" + book.getAuthor() + "</td>");
        		builder.append("<td><form action='/servlet/WebNotesServlet' method='post'>"+
        					"<input type='hidden' name='action' value='delete'>" +
        					"<input type='hidden' name='name' value='" + book.getName() + "'>" +
        					"<input type='submit' value='delete'></form>");
        		builder.append("<td><form action='/servlet/WebNotesServlet' method='post'>"+
    					"<input type='hidden' name='action' value='modify'>" +
    					"<input type='hidden' name='name' value='" + book.getName() + "'>" +
    					"<input type='submit' value='modify'></form>");
        		builder.append("</tr>");
        	}
        	builder.append("</table>");
        }
        
        builder.append("<h2>Add new note</h2>");
        
        builder.append("<form action='/servlet/WebNotesServlet' method='post'>" +
        		"<input type='hidden' name='action' value='add'>" + 
			"Name: <input type='text' name='name'><br>" + 
			"Year: <input type='text' name='year'><br>" + 
			"Language: <input type='text' name='language'><br>" + 
			"Author: <input type='text' name='author'><br>" + 
			"<input type='submit' value='Submit'>" +
			"</form>");

        ((HttpResponse)response).sendHTML(builder.toString());
    }
    
    

    @Override
    public void destroy() {
        System.out.println("---from Servlet destroy---");
    }

}
