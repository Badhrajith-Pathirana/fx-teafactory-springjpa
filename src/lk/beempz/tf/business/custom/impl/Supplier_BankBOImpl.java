/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.beempz.tf.business.custom.impl;

import lk.beempz.tf.business.custom.Supplier_BankBO;
import lk.beempz.tf.dao.custom.Supplier_BankDAO;
import lk.beempz.tf.dto.Supplier_BankDTO;
import lk.beempz.tf.entity.Supplier_Bank;
import lk.beempz.tf.entity.Supplier_Bank_PK;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class Supplier_BankBOImpl implements Supplier_BankBO {

    @Autowired
    private Supplier_BankDAO supplier_BankDAO;
    /*@Autowired
    private SessionFactory sessionFactory;*/

    public Supplier_BankBOImpl() {
    }
    
    
    @Override
    public boolean addSuplierBank(Supplier_BankDTO supplier_BankDTO) {
        try{
            supplier_BankDAO.save(new Supplier_Bank(new Supplier_Bank_PK(supplier_BankDTO.getBranchid(), supplier_BankDTO.getSupplierid()), supplier_BankDTO.getAcc_no()));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
