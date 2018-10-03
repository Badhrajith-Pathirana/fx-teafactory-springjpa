/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.beempz.tf.business.custom.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.beempz.tf.business.custom.CreditBO;
import lk.beempz.tf.business.custom.CreditTypeBO;
import lk.beempz.tf.business.custom.SupplierBO;
import lk.beempz.tf.dao.custom.CreditDAO;
import lk.beempz.tf.dto.CreditDTO;
import lk.beempz.tf.dto.CreditTypeDTO;
import lk.beempz.tf.dto.SupplierDTO;
import lk.beempz.tf.entity.Credit;
import lk.beempz.tf.entity.Credit_Type;
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
public class CreditBOImpl implements CreditBO {

    @Autowired
    private CreditDAO creditDAO;
    @Autowired
    private SupplierBO supplierBO;
    @Autowired
    private CreditTypeBO creditTypeBO ;
   /* @Autowired
    private SessionFactory sessionFactory;*/

    public CreditBOImpl() {
    }
    @Override
    public boolean insertCredit(CreditDTO creditDTO)throws Exception{
        Credit credit = null;
        CreditTypeDTO creditTypeDTO = null;
        if(creditDTO.getCreditType()<0){
            int idByName = creditTypeBO.getIdByName(creditDTO.getCredit_typename());
            creditTypeDTO = creditTypeBO.getCreditType(idByName);
        }
        Credit_Type credit_type = new Credit_Type(creditTypeDTO.getCreditTypeid(), creditTypeDTO.getCreditType());
        SupplierDTO supplierDTO = supplierBO.findSupplier(creditDTO.getSupplierid());
        Supplier supplier = new Supplier(supplierDTO.getSupplierid(), supplierDTO.getName(), new Route(supplierDTO.getRouteid(), supplierDTO.getRoute()), supplierDTO.getContact(), supplierDTO.getAddress());
        try{
            credit = creditDAO.saveAndGetGenerated(new Credit( creditDTO.getDate(), creditDTO.getAmount(), supplier, credit_type));
        }
        catch (HibernateException e1){
            return false;
        }
        return (credit != null)? true:false;
    }

    @Override
    public ArrayList<CreditDTO> getAllCredits(Date from, Date to){
        ArrayList<CreditDTO> creditDTOs = new ArrayList<>();
        List<Credit> credits = null;
        try {
            if (from == null || to == null)
                credits = creditDAO.getAll();
            else
                credits = creditDAO.getFromTo(from, to);
        }catch (HibernateException e1){
            return null;
        }
        for (Credit credit : credits) {
            creditDTOs.add(new CreditDTO(credit.getCreditid(), credit.getSupplier().getSupplierno(), credit.getSupplier().getName(), credit.getCredit_type().getTypeid(), credit.getCredit_type().getType_name(), credit.getDate(), credit.getAmount()));
        }
        return creditDTOs;
    }

    @Override
    public boolean deleteCredit(CreditDTO creditDTO){
        try{
            creditDAO.delete(creditDTO.getCreditid());
            return true;
        }
        catch (Exception e1){
            return false;
        }
    }
    
}
