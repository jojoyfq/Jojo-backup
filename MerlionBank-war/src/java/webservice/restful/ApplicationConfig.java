/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author liyanmeng
 */
@javax.ws.rs.ApplicationPath("PayMeRestfulWebServices")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(webservice.restful.PayMeResources.class);
    }

}
