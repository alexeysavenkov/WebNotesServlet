package myapp;

import http.server.servlet.AbstractServletsMap;
import myapp.web.WebNotesServlet;

/**
 *
 * @author andrii
 */
public class ServletsMap extends AbstractServletsMap {


    public ServletsMap() {
        servlets.put("/WebNotesServlet", new WebNotesServlet());

    }

}
