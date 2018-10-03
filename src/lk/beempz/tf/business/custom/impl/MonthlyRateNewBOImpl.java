/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.beempz.tf.business.custom.impl;

import java.util.Date;
import lk.beempz.tf.business.BOFactory;
import lk.beempz.tf.business.custom.MonthlyRateNewBO;
import lk.beempz.tf.business.custom.PurchaseBO;
import lk.beempz.tf.dao.custom.RateDAO;
import lk.beempz.tf.dto.MonthlyRateDTO;
import lk.beempz.tf.entity.Rate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class MonthlyRateNewBOImpl implements MonthlyRateNewBO {
    @Autowired
    private RateDAO rateDAO;
    /*@Autowired
    private SessionFactory sessionFactory;*/

    public MonthlyRateNewBOImpl() {
    }

    @Override
    public boolean insertMonthlyRates(MonthlyRateDTO debitDTO) {
        Rate byId = rateDAO.findById(debitDTO.getDate());
//        System.out.println(byId);
        byId.setAkgper(debitDTO.getaGrade());
        byId.setBkgper(debitDTO.getbGrade());
        byId.setTravelling(debitDTO.getTravelling());
        rateDAO.update(byId);
        return true;

    }
    public MonthlyRateDTO findByID(Date date){
        Rate findById = null;
        try{
            findById = rateDAO.findById(date);
        }catch (HibernateException e1){
            return null;
        }
        if(findById == null)
            return null;
        return new MonthlyRateDTO(findById.getRateMonth(), findById.getAkgper(), findById.getBkgper(), findById.getTravelling());
    }
    
}
