/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.beempz.tf.business.custom.impl;

import java.util.ArrayList;
import java.util.List;

import lk.beempz.tf.business.custom.MonthlyRateBO;
import lk.beempz.tf.dao.custom.RateDAO;
import lk.beempz.tf.dto.MonthlyRateDTO;
import lk.beempz.tf.dto.UnprocessedDebitDTO;
import lk.beempz.tf.entity.Rate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class MonthlyRateBOImpl implements MonthlyRateBO {

    @Autowired
    private RateDAO rateDAO;
    /*@Autowired
    private SessionFactory sessionFactory;*/
   // DebitBO debitBO;
    
    public MonthlyRateBOImpl() {

    }

    

    

    @Override
    public boolean updateMonthlyRates(MonthlyRateDTO monthlyRateDTO){
        try{
            Rate byId = rateDAO.findById(monthlyRateDTO.getDate());
            System.out.println(byId);
            byId.setAkgper(monthlyRateDTO.getaGrade());
            byId.setBkgper(monthlyRateDTO.getbGrade());
            byId.setTravelling(monthlyRateDTO.getTravelling());
//            rateDAO.update(new Rate(monthlyRateDTO.getDate(), monthlyRateDTO.getaGrade(), monthlyRateDTO.getbGrade(), monthlyRateDTO.getTravelling()));

            return true;
        }
        catch (Exception e1){
            return false;
        }
    }

    @Override
    public MonthlyRateDTO getRates(UnprocessedDebitDTO debitDTO){
        Rate rate = null;

        try {
            rate = rateDAO.findById(debitDTO.getDate());
        }catch (HibernateException e1){
            return null;
        }
        return new MonthlyRateDTO(debitDTO.getDate(), rate.getAkgper(), rate.getBkgper(), rate.getTravelling());
    }

    @Override
    public ArrayList<MonthlyRateDTO> getAllRates(){
        ArrayList<MonthlyRateDTO> monthlyRateDTOs = new ArrayList<>();
        List<Rate> all = null;
        try{
            all = rateDAO.getAll();
        }catch (HibernateException e1){
            return null;
        }
        if(all == null)
            return null;
        for (Rate rate : all) {
            monthlyRateDTOs.add(new MonthlyRateDTO(rate.getRateMonth(), rate.getAkgper(), rate.getBkgper(), rate.getTravelling()));
        }
        return monthlyRateDTOs;
    }

    
}
