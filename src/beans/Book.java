package beans;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import http.server.HttpRequest;
import myapp.web.WebNotesServlet;


public class Book {

    private String name;
    
    private String author;
    
    private String language;
    
    private int year;
    
    

    //геттеры и сеттеры для полей
    
    public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}

	public String getLanguage() {
		return language;
	}

	public int getYear() {
		return year;
	}

	public static Book fromHttpRequest(HttpRequest httpRequest) {
    	String name = httpRequest.getParameter("name");
		String year = httpRequest.getParameter("year");
		String language = httpRequest.getParameter("language");
		String author = httpRequest.getParameter("author");
		
		if(name.matches(".{1,20}") && year.matches("\\d{1,20}") && language.matches(".{1,20}") && author.matches(".{1,20}")) {
			Book book = new Book();
			book.name = name;
			book.author = author;
			book.language = language;
			book.year = Integer.parseInt(year);
			return book;
		}
		
		return null;
    }
    
    public BasicDBObject toDBObject() {
        BasicDBObject document = new BasicDBObject();
        
        document.put("name", name);
        document.put("year", year);
        document.put("language", language);
        document.put("author", author);
        
        return document;
    }
    
    public static Book fromDBObject(DBObject document) {
        Book b = new Book();
        
        b.name = (String) document.get("name");
        b.year = (Integer) document.get("year");
        b.language = (String) document.get("language");
        b.author = (String) document.get("author");
        
        return b;
    }
}