/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.beempz.tf.business.custom.impl;

import java.util.ArrayList;
import java.util.List;

import lk.beempz.tf.business.custom.RouteBO;
import lk.beempz.tf.business.custom.SupplierBO;
import lk.beempz.tf.dao.custom.RouteDAO;
import lk.beempz.tf.dao.custom.SupplierDAO;
import lk.beempz.tf.dto.SupplierDTO;
import lk.beempz.tf.entity.Route;
import lk.beempz.tf.entity.Supplier;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class SupplierBOImpl implements SupplierBO {

    @Autowired
    private SupplierDAO supplierDAO;
    @Autowired
    private RouteBO routeBO;
    /*@Autowired
    private SessionFactory sessionFactory;*/
    @Autowired
    private RouteDAO routeDAO;

    public SupplierBOImpl() {
    }
    @Override
    public ArrayList<SupplierDTO> getSuppliers() throws Exception {
        List<Supplier> suppliers = null;
        try{
            suppliers = supplierDAO.getAll();
        }catch (HibernateException e1){
            return null;
        }
        ArrayList<SupplierDTO> supplierDTOs = new ArrayList<>();
        for (Supplier supplier : suppliers) {
//            RouteDTO route = routeBO.findRoute(supplier.getRoute());
            supplierDTOs.add(new SupplierDTO(supplier.getSupplierno(), supplier.getName(), supplier.getRoute().getRouteid(), supplier.getRoute().getRoutename(), supplier.getPhone(), supplier.getAddress()));
            
        }
        return supplierDTOs;
    }

    @Override
    public SupplierDTO findSupplier(int Id) throws Exception {
        Supplier supplier = null;
        try{
            supplier = supplierDAO.findById(Id);
        }catch (HibernateException e1){
            return null;
        }
        if(supplier == null){
            return null;
        }
        return new SupplierDTO(supplier.getSupplierno(), supplier.getName(), supplier.getRoute().getRouteid(), supplier.getRoute().getRoutename(), supplier.getPhone(), supplier.getAddress());
        
    }

    @Override
    public boolean addNewSupplier(SupplierDTO supplier) throws Exception {
        if (supplier.getRouteid() == -1) {
            supplier.setRouteid(routeBO.getRouteByName(supplier.getRoute()).getRouteid());
        }
        try{
            if (supplier.getSupplierid() == -1) {
                Supplier id = supplierDAO.saveAndGetGenerated(new Supplier(supplier.getName(),   supplier.getContact(), supplier.getAddress(),new Route(supplier.getRouteid(),supplier.getRoute())));
                if (id == null) {
                    return false;
                }
                return true;
            }
            supplierDAO.save(new Supplier(supplier.getSupplierid(), supplier.getName(), new Route(supplier.getRouteid(), supplier.getRoute()), supplier.getContact(), supplier.getAddress()));
            return true;
        }catch (HibernateException e1){
            return false;
        }
    }

    @Override
    public boolean updateSupplier(SupplierDTO supplier) throws Exception {
        if(supplier.getRouteid() == -1){
            supplier.setRouteid(routeBO.getRouteByName(supplier.getRoute()).getRouteid());
        }
        try{
            Supplier byId = supplierDAO.findById(supplier.getSupplierid());
            Route route = routeDAO.findById(supplier.getRouteid());
            byId.setRoute(route);
            byId.setName(supplier.getName());
            byId.setAddress(supplier.getAddress());
            byId.setPhone(supplier.getContact());
            supplierDAO.update(byId);
            return true;
        }catch (HibernateException e1){
            return false;
        }

    }

    @Override
    public boolean deleteSupplier(int id)throws Exception{
        try {
            supplierDAO.delete(id);
            return true;
        }catch (HibernateException e1){
            return false;
        }
    }

    @Override
    public int addAndReturnGenerated(SupplierDTO supplier) throws Exception{
        try {
            if (supplier.getRouteid() == -1) {
                supplier.setRouteid(routeBO.getRouteByName(supplier.getRoute()).getRouteid());
            }
            if (supplier.getSupplierid() == -1) {
                Supplier id = supplierDAO.saveAndGetGenerated(new Supplier(-1, supplier.getName(), new Route(supplier.getRouteid(), supplier.getRoute()), supplier.getContact(), supplier.getAddress()));
                if (id == null) {
                    return -1;
                }
                return id.getSupplierno();
            }
            return -1;
        }catch (HibernateException e1){
            return -1;
        }
    }
}
