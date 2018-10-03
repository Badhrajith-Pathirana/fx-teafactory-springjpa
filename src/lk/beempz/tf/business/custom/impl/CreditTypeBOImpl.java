/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.beempz.tf.business.custom.impl;

import java.util.ArrayList;
import java.util.List;

import lk.beempz.tf.business.custom.CreditTypeBO;
import lk.beempz.tf.dao.custom.Credit_TypeDAO;
import lk.beempz.tf.dto.CreditTypeDTO;
import lk.beempz.tf.entity.Credit_Type;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class CreditTypeBOImpl implements CreditTypeBO {

    @Autowired
    private Credit_TypeDAO credit_TypeDAO;
    /*@Autowired
    private SessionFactory sessionFactory;*/

    public CreditTypeBOImpl() {
    }
    @Override
    public CreditTypeDTO getCreditType(int id){
        Credit_Type ctype = null;
        try{
            ctype = credit_TypeDAO.findById(id);
        }catch (HibernateException e1){
            return null;
        }
        if(ctype == null)
            return null;
        return new CreditTypeDTO(ctype.getTypeid(), ctype.getType_name());
    }

    @Override
    public ArrayList<CreditTypeDTO> getCredits(){
        ArrayList<CreditTypeDTO> creditTypeDTOs = new ArrayList<>();
        List<Credit_Type> all = null;
        try{
            all=credit_TypeDAO.getAll();
        }catch (HibernateException e1){
            return null;
        }
        for (Credit_Type credit_Type : all) {
            creditTypeDTOs.add(new CreditTypeDTO(credit_Type.getTypeid(), credit_Type.getType_name()));
        }
        return creditTypeDTOs;
    }

    @Override
    public int getIdByName(String type_Name) {
        int creditTypeid = -1;
        try {
            creditTypeid = credit_TypeDAO.getCreditTypeid(type_Name);
        }catch (HibernateException e1){
            return -1;
        }
        return creditTypeid;

    }
    
}
