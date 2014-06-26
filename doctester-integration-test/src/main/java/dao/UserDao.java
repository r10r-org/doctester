/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import models.User;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

public class UserDao {
    
    @Inject
    Provider<EntityManager> entityManagerProvider;
    
    @Transactional
    public boolean isUserAndPasswordValid(String username, String password) {
        
        if (username != null && password != null) {
            
            EntityManager entityManager = entityManagerProvider.get();
            
            Query q = entityManager.createQuery("SELECT x FROM User x WHERE username = :usernameParam");
            User user = (User) q.setParameter("usernameParam", username).getSingleResult();   

            
            if (user != null) {
                
                if (user.password.equals(password)) {

                    return true;
                }
                
            }

        }
        
        return false;
 
    }

    @Transactional
    public boolean isAdmin(String username) {

    	if (username != null) {

    		EntityManager entityManager = this.entityManagerProvider.get();
    		
    		Query query = entityManager.createQuery("SELECT COUNT(x) FROM User x WHERE username = :usernameParam AND isAdmin = true");
    		
    		Long count = (Long) query.setParameter("usernameParam", username).getSingleResult();
    		
    		return count == 1;
    		
    	}
    	
    	return false;
    	
    }

}
