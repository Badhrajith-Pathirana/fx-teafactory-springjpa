/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.beempz.tf.dao.custom.impl;

import lk.beempz.tf.dao.CrudDAOImpl;
import lk.beempz.tf.dao.custom.DebitDAO;
import lk.beempz.tf.entity.Debit;
import org.springframework.stereotype.Component;

@Component
public class DebitDAOImpl extends CrudDAOImpl<Debit,Integer> implements DebitDAO {

    @Override
    public void deleteByPurchaseid(int purchaseid) {
        Debit debit = entityManager.createQuery("FROM Debit WHERE purchaseid = ?1", Debit.class).setParameter(1, purchaseid).getSingleResult();
        entityManager.remove(debit);
    }

    @Override
    public Debit selectByPurchaseID(int purchaseid) {
        return entityManager.createQuery("FROM Debit WHERE purchaseid = ?1",Debit.class).setParameter(1,purchaseid).getSingleResult();
    }
}
