/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.beempz.tf.business.custom.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.beempz.tf.business.custom.DebitBO;
import lk.beempz.tf.business.custom.SupplierBO;
import lk.beempz.tf.dao.custom.DebitDAO;
import lk.beempz.tf.dao.custom.PurchaseDAO;
import lk.beempz.tf.dao.custom.SupplierDAO;
import lk.beempz.tf.dto.DebitDTO;
import lk.beempz.tf.dto.SupplierDTO;
import lk.beempz.tf.entity.Debit;
import lk.beempz.tf.entity.Purchase;
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
public class DebitBOImpl implements DebitBO {

    @Autowired
    private DebitDAO debitDAO;
    @Autowired
    private SupplierBO supplierBO;
    /*@Autowired
    private MonthlyRateBO monthlyRateBO;*/
    /*@Autowired
    private SessionFactory sessionFactory;*/
    @Autowired
    private PurchaseDAO purchaseDAO;
    @Autowired
    private SupplierDAO supplierDAO;

    public DebitBOImpl() {
    }
    @Override
    public ArrayList<DebitDTO> getDebitList(Date from, Date to) throws Exception {
        ArrayList<DebitDTO> debitDTOs = new ArrayList<>();
        List<Debit> debits = null;

        try {
            if(from == null && to == null){
                debits = debitDAO.getAll();
            }else{
//                debits = debitDAO.getSortAndFiltered(from, to);
            }
        }catch (HibernateException e1){
            System.out.println(e1.getMessage());
        }

        for (Debit debit : debits) {
            debitDTOs.add(new DebitDTO(debit.getDebitid(), debit.getDebitdate(), debit.getPurchase().getPurchase_id(), debit.getSupplier().getSupplierno(), supplierBO.findSupplier(debit.getSupplier().getSupplierno()).getName(), debit.getAmount()));
        }
        return debitDTOs;
    }

    

    @Override
    public boolean updateDebit(DebitDTO debitDTO){
        try {
            Purchase purchase = purchaseDAO.findById(debitDTO.getPurchaseid());
            Supplier supplier = supplierDAO.findById(debitDTO.getSupplierid());
            Debit debit = debitDAO.findById(debitDTO.getDebitid());
            debit.setDebitdate(debitDTO.getDate());
            debit.setPurchase(purchase);
            debit.setAmount(debitDTO.getAmount());
            debit.setSupplier(supplier);
            debitDAO.update(debit);
            return true;
        }
        catch (Exception e1){
            return false;
        }
    }

    @Override
    public boolean deleteDebit(int debitId){
        try {
            debitDAO.delete(debitId);
            return true;
        }
        catch (Exception e1){
            return false;
        }
    }

    @Override
    public boolean insertDebit(DebitDTO debitDTO)throws Exception{
        Debit debit = null;
        try {
            SupplierDTO supplierDTO = supplierBO.findSupplier(debitDTO.getSupplierid());
            Supplier supplier = new Supplier(supplierDTO.getSupplierid(), supplierDTO.getName(), new Route(supplierDTO.getRouteid(), supplierDTO.getRoute()), supplierDTO.getContact(), supplierDTO.getAddress());
            Purchase purchase = purchaseDAO.findById(debitDTO.getPurchaseid());
            debit = debitDAO.saveAndGetGenerated(new Debit( debitDTO.getDate(), debitDTO.getAmount(),purchase, supplier));
        }catch (HibernateException e1){
            return false;
        }
        return debit != null;
    }

    @Override
    public boolean deleteByPurchase(int purchaseid) {
        try{
            debitDAO.deleteByPurchaseid(purchaseid);
            return true;
        }
        catch (Exception e1){
            return false;
        }
    }

    @Override
    public DebitDTO getAllByPurchaseID(int purchaseid) throws Exception{
        Debit debit = null;

        try{
            debit = debitDAO.selectByPurchaseID(purchaseid);
        }catch (HibernateException e1){
            return null;
        }
        if(debit == null)
            return null;
        return new DebitDTO(debit.getDebitid(), debit.getDebitdate(), debit.getPurchase().getPurchase_id(), debit.getSupplier().getSupplierno(), supplierBO.findSupplier(debit.getSupplier().getSupplierno()).getName(), debit.getAmount());
    }

    

    
    
}
