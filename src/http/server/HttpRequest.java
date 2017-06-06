package http.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpRequest implements Request {

    //private final InputStream input;
    private final String requestHeader;
    private final String uri;
    private Map<String, String> parameterMap = new HashMap<>();

    public HttpRequest(InputStream input) {
        //this.input = input;
    	
    	BufferedReader br = new BufferedReader(
                new InputStreamReader(input));
    	
    	this.requestHeader = getRequestHeader(br);
    	
    	int contentLengthIndex = requestHeader.indexOf("Content-Length");
    	if(contentLengthIndex > -1) {
	    	try {
        		String contentLengthStr = requestHeader.substring(contentLengthIndex).split("\\s+")[1];
        		int contentLength = Integer.parseInt(contentLengthStr);
        		char[] content = new char[contentLength];
        		br.read(content);
        		
        		parameterMap = parseParameterMap(new String(content));
	    	} catch(IOException e) {
	    		e.printStackTrace();
	    	}
    	}
    	
        this.uri = parseUri(requestHeader);
    }

    private String getRequestHeader(BufferedReader br) {
    	
        StringBuilder requestHeader = new StringBuilder();

        try {
            while(true) {
                String line = br.readLine();
                
                if(line == null || line.isEmpty()) {
                	break;
                }

                requestHeader.append(line);
                requestHeader.append("\r\n");                
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        return requestHeader.toString();
    }
    
    private String parseUri(String requestString) {

        if (requestString.isEmpty()) {
            return "";
        }

        int index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            int index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1) {
                return requestString.substring(index1 + 1, index2);
            }
        }

        return "";
    }

    private Map<String, String> parseParameterMap(String request) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        
        String[] paramPairs = request.split("&");
        for(String paramPair : paramPairs) {
        	String[] tokens = paramPair.split("=");
        	parameterMap.put(tokens[0], tokens[1]);
        }
        
        return parameterMap;
    }

    @Override
    public String getURI() {
        return uri;
    }

    @Override
    public String getParameter(String name) {
        return parameterMap.get(name);
    }

    @Override
    public Set<String> getParameterNames() {
        return parameterMap.keySet();
    }

    @Override
    public Collection<String> getParameterValues() {
        return parameterMap.values();
    }

    @Override
    public String getRequestAsText() {
        return requestHeader;
    }
    
    public String getMethod() {
    	return requestHeader.substring(0, requestHeader.indexOf(' '));
    }

}
