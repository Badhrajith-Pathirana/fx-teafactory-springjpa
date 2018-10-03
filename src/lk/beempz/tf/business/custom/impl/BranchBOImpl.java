/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.beempz.tf.business.custom.impl;

import java.util.ArrayList;
import java.util.List;

import lk.beempz.tf.business.custom.BankBO;
import lk.beempz.tf.business.custom.BranchBO;
import lk.beempz.tf.dao.custom.BankDAO;
import lk.beempz.tf.dao.custom.BranchDAO;
import lk.beempz.tf.dto.BankDTO;
import lk.beempz.tf.dto.BranchDTO;
import lk.beempz.tf.entity.Bank;
import lk.beempz.tf.entity.Branch;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class BranchBOImpl implements BranchBO {

    @Autowired
    private BranchDAO branchDAO;
    @Autowired
    private BankBO bankBO;
    /*@Autowired
    private SessionFactory sessionFactory;*/
    @Autowired
    private BankDAO bankDAO;

    public BranchBOImpl() {
    }
    @Override
    public boolean saveBranch(BranchDTO branch){
        try {
            BankDTO bank = bankBO.findBank(bankBO.getBankID(branch.getBankName()));
//            System.out.println(bank.getBankid());
            branchDAO.save(new Branch(new Bank(bank.getBankid(),bank.getBankName()),branch.getBranchName()));
            return true;
        }catch (HibernateException e1){
            return false;
        }


    }

    @Override
    public boolean updateBranch(BranchDTO branch){
        try{
            int id = bankDAO.getID(branch.getBankName());
            Bank bank = bankDAO.findById(id);
            Branch branchEnt = branchDAO.findById(branch.getBranchid());
            branchEnt.setBranchName(branch.getBranchName());
            branchEnt.setBank(bank);
            branchDAO.update(branchEnt);
            return true;
        }
        catch (HibernateException e1){
            return false;
        }


    }

    @Override
    public ArrayList<BranchDTO> getBranches(){
        List<Branch> branches = null;
        try{
            branches = branchDAO.getAll();
        }
        catch (HibernateException e1){
            return null;
        }
        ArrayList<BranchDTO> branchDTOs = new ArrayList<>();
        for (Branch branch : branches) {
            branchDTOs.add(new BranchDTO(branch.getBranchid(), branch.getBank().getBankid(), branch.getBranchName(),bankBO.findBankName(branch.getBank().getBankid())));
        }
        return branchDTOs;
    }

    @Override
    public int getBank(BranchDTO branch){
        if(branch.getBankid()==-1){
            return bankBO.getBankID(branch.getBankName());
        }
        return branch.getBankid();
    }

    @Override
    public boolean deleteBranch(int branchid){
        try{
            branchDAO.delete(branchid);
            return true;
        }
        catch (Exception e1){
            return false;
        }
    }

    
    
}
