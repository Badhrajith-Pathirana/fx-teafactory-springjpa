/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.beempz.tf.dao.custom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.beempz.tf.dao.CrudDAO;
import lk.beempz.tf.entity.Credit;

/**
 *
 * @author badhr
 */
public interface CreditDAO extends CrudDAO<Credit, Integer>{
    public List<Credit> getFromTo(Date from, Date to);
}
