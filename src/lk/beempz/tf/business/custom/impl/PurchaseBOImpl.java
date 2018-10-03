/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.beempz.tf.business.custom.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.beempz.tf.business.BOFactory;
import lk.beempz.tf.business.custom.DebitBO;
import lk.beempz.tf.business.custom.MonthlyRateBO;
import lk.beempz.tf.business.custom.PurchaseBO;
import lk.beempz.tf.business.custom.SupplierBO;
import lk.beempz.tf.dao.custom.PurchaseDAO;
import lk.beempz.tf.db.DBConnection;
import lk.beempz.tf.dto.*;
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
public class PurchaseBOImpl implements PurchaseBO {

    @Autowired
    private PurchaseDAO purchaseDAO;
    @Autowired
    private SupplierBO supplierBO;
    @Autowired
    private DebitBO debitBO;
    /*@Autowired
    private SessionFactory sessionFactory;*/
    

    public PurchaseBOImpl() {
    }

    @Override
    public boolean addPurchase(PurchaseDTO purchaseDTO) throws Exception {
        MonthlyRateBO monthlyRateBO = (MonthlyRateBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.MONTHLY_RATE);
        try{
            MonthlyRateDTO rates = monthlyRateBO.getRates(new UnprocessedDebitDTO(purchaseDTO.getDate(), purchaseDTO.getSupplierid(), null, purchaseDTO.getaKg(), purchaseDTO.getbKg()));
            BigDecimal payforA = rates.getaGrade().multiply(purchaseDTO.getaKg());
            BigDecimal payforB = rates.getbGrade().multiply(purchaseDTO.getbKg());
            BigDecimal totalSize = purchaseDTO.getaKg().add(purchaseDTO.getbKg());
            BigDecimal payforTravel = rates.getTravelling().multiply(totalSize);
            BigDecimal totalAmount = payforA.add(payforB.subtract(payforTravel));
            SupplierDTO supplierDTO = supplierBO.findSupplier(purchaseDTO.getSupplierid());
            Supplier supplier = new Supplier(supplierDTO.getSupplierid(), supplierDTO.getName(), new Route(supplierDTO.getRouteid(), supplierDTO.getRoute()), supplierDTO.getContact(), supplierDTO.getAddress());
            Purchase purchase = purchaseDAO.saveAndGetGenerated(new Purchase(purchaseDTO.getDate(), supplier, purchaseDTO.getaKg(), purchaseDTO.getbKg()));
            if (purchase == null) {
                return false;
            }
            boolean result = debitBO.insertDebit(new DebitDTO(-1, purchaseDTO.getDate(), purchase.getPurchase_id(), purchaseDTO.getSupplierid(), purchaseDTO.getSuppliername(), totalAmount));
            if (result) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public boolean deletePurchase(int pid, Date date) throws Exception {

        try{
            Purchase result = purchaseDAO.findById(pid);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(result.getPurchase_date());
            Calendar c2 = Calendar.getInstance();
            c2.setTime(new Date());
            long diff = (c2.getTimeInMillis() - c1.getTimeInMillis()) / (60000 * 60 * 24);
            if (diff > 1) {
                return false;
            }
            
            boolean res = debitBO.deleteByPurchase(pid);
            if(!res){
                return false;
            }
            purchaseDAO.delete(pid);
            if(!res){
                return false;
            }
            return true;
                
        } catch (Exception e) {
//            DBConnection.getInstance().getConnection().rollback();
            throw e;
        }
    }

    @Override
    public ArrayList<PurchaseDTO> getAll() throws Exception {
        ArrayList<PurchaseDTO> purchaseDTOs = new ArrayList<>();
        List<Purchase> all = null;
        try{
            all = purchaseDAO.getAll();
        }catch (HibernateException e1){
            return null;
        }
        for (Purchase purchase : all) {
            purchaseDTOs.add(new PurchaseDTO(purchase.getPurchase_id(), purchase.getPurchase_date(), purchase.getSupplier().getSupplierno(), supplierBO.findSupplier(purchase.getSupplier().getSupplierno()).getName(), purchase.getAkg(), purchase.getBkg()));
        }
        return purchaseDTOs;
    }

    @Override
    public ArrayList<PurchaseDTO> getAllByMonth(Date date) throws Exception {
        ArrayList<PurchaseDTO> purchaseDTOs = new ArrayList<>();
        List<Purchase> purchases = null;
        try{
            purchases = purchaseDAO.getAllByMonth(date);
        }catch (HibernateException e1){
            return null;
        }
        for (Purchase purchase : purchases) {
            purchaseDTOs.add(new PurchaseDTO(purchase.getPurchase_id(), purchase.getPurchase_date(), purchase.getSupplier().getSupplierno(), supplierBO.findSupplier(purchase.getSupplier().getSupplierno()).getName(), purchase.getAkg(), purchase.getBkg()));
        }
        return purchaseDTOs;
    }

   

    

    

}
