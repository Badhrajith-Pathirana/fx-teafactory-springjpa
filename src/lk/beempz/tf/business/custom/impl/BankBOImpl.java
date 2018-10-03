/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.beempz.tf.business.custom.impl;

import java.util.ArrayList;
import java.util.List;

import lk.beempz.tf.business.custom.BankBO;
import lk.beempz.tf.dao.custom.BankDAO;
import lk.beempz.tf.dto.BankDTO;
import lk.beempz.tf.entity.Bank;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class BankBOImpl implements BankBO {
    @Autowired
    private BankDAO bankDAO;
   /* @Autowired
    private SessionFactory sessionFactory;*/

    public BankBOImpl() {
    }

    @Override
    public BankDTO saveBank(BankDTO bank) {
        Bank bankent = null;
        try {
            bankent =  bankDAO.saveAndGetGenerated(new Bank(bank.getBankName()));
        }catch (HibernateException e1){
            return null;
        }
        if(bankent == null)
            return null;
        return new BankDTO(bankent.getBankid(), bankent.getBankName());
    }

    @Override
    public boolean updateBank(BankDTO bank) {
        try{

            Bank byId = bankDAO.findById(bank.getBankid());
            byId.setBankname(bank.getBankName());
            bankDAO.update(byId);
            return true;
        }catch (HibernateException e1){
            return false;
        }

    }

    @Override
    public boolean deleteBank(int bankid){
        try{
            bankDAO.delete(bankid);
            return true;
        }
        catch (Exception e1){
            return  false;
        }
    }

    @Override
    public ArrayList<BankDTO> getAllBanks(){
        ArrayList<BankDTO> bankDTOs = new ArrayList<>();
        List<Bank> banks = null;
        try{
            banks = bankDAO.getAll();
        }catch (HibernateException e1){
            return null;
        }
        if(banks == null){
            return null;
        }
        for (Bank bank : banks) {
            bankDTOs.add(new BankDTO(bank.getBankid(), bank.getBankName()));
        }
        return bankDTOs;
    }

    @Override
    public int getBankID(String bankName){
        int id = -1;
        try{
            id = bankDAO.getID(bankName);
        }catch (HibernateException e1){
            return -1;
        }
        return id;
    }

    @Override
    public String findBankName(int bankid){
        String bankName = null;
        try{
            bankName = bankDAO.findById(bankid).getBankName();
        }catch (HibernateException e1){
            return null;
        }
        return bankName;
    }

    @Override
    public BankDTO findBank(int bankID) {
        Bank bank = null;
        try{
            bank = bankDAO.findById(bankID);
        }catch (HibernateException e1){
            return null;
        }
        if(bank == null){
            return null;
        }
        return new BankDTO(bank.getBankid(),bank.getBankName());
    }
}
