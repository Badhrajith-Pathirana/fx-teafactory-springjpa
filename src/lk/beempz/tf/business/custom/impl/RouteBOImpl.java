/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.beempz.tf.business.custom.impl;

import java.util.ArrayList;
import java.util.List;

import lk.beempz.tf.business.custom.RouteBO;
import lk.beempz.tf.dao.custom.RouteDAO;
import lk.beempz.tf.dto.RouteDTO;
import lk.beempz.tf.entity.Route;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class RouteBOImpl implements RouteBO {

    @Autowired
    private RouteDAO routeDAO;
    /*@Autowired
    private SessionFactory sessionFactory;*/

    public RouteBOImpl() {
    }
    @Override
    public RouteDTO findRoute(int routeid)throws Exception{
        Route routeResult = null;
        try{
            routeResult = routeDAO.findById(routeid);
        }catch (HibernateException e1){
            return null;
        }
        if(routeResult == null){
            return null;
        }
        return new RouteDTO(routeResult.getRouteid(), routeResult.getRoutename());
    }

    @Override
    public ArrayList<RouteDTO> getRoutes() throws Exception {
        ArrayList<RouteDTO> routeDTOs = new ArrayList<>();
        List<Route> routes = null;
        try{

            routes = routeDAO.getAll();

        }catch (HibernateException e1){
            return null;
        }
        if(routes == null){
            return null;
        }
        for (Route route : routes) {
            routeDTOs.add(new RouteDTO(route.getRouteid(), route.getRoutename()));
        }
        return routeDTOs;
    }

    @Override
    public RouteDTO getRouteByName(String name) throws Exception {
        Route routeID = null;
        try{
            routeID = routeDAO.getRouteID(name);
        }catch (HibernateException e1){
            return null;
        }
        return new RouteDTO(routeID.getRouteid(), name);
    }

    @Override
    public boolean saveRoute(RouteDTO routeDTO) throws Exception {
        Route route = null;
        try{
            route = routeDAO.saveAndGetGenerated(new Route( routeDTO.getRoute()));
        }catch (HibernateException e1){
            return false;
        }
        return route != null;
    }

    @Override
    public boolean updateRoute(RouteDTO routeDTO) throws Exception {
        try{
            Route route = routeDAO.findById(routeDTO.getRouteid());
            route.setRoutename(routeDTO.getRoute());
            routeDAO.update(route);
            return true;
        }
        catch (Exception e1){
            throw e1;
        }

    }

    @Override
    public boolean deleteRoute(int routeid) throws Exception {
        try {
            routeDAO.delete(routeid);
            return true;
        }catch (HibernateException e1){
            return false;
        }
    }

    

    

    
}
