package filters;

import com.google.inject.Inject;

import dao.UserDao;
import ninja.Context;
import ninja.Filter;
import ninja.FilterChain;
import ninja.Result;
import ninja.Results;
import ninja.session.Session;
import ninja.utils.NinjaConstant;

public class AdminFilter implements Filter {

	@Inject
	UserDao dao;

    public final String USERNAME = "username";

    @Override
    public Result filter(FilterChain chain, Context context) {

    	Session session = context.getSession();
    	
    	if (session != null) {
    		
    		String username = session.get(USERNAME);
    		
    		if (username != null) {
    			
    			if (dao.isAdmin(username)) {
    				return chain.next(context);
    			}
    			
    		}

    	}

		return Results.forbidden().html().template(NinjaConstant.LOCATION_VIEW_FTL_HTML_FORBIDDEN);
    	

    }

}
