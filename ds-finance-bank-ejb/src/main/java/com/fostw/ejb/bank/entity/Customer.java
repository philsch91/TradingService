/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fostw.ejb.bank.entity;

import com.fostw.dsfinance.bank.common.interfaces.IUser;
import com.fostw.dsfinance.bank.common.models.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author A4938
 */
@Entity

@Table(name="User")
@Access(value=AccessType.FIELD)
public class Customer extends UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String address;
    
    @OneToMany(mappedBy="customer")
    private List<Depot> depotList = new ArrayList<Depot>();

    public List<Depot> getDepotList() {
        return depotList;
    }

    public void setDepotList(List<Depot> depotList) {
        this.depotList = depotList;
    }
    
    public Boolean addDepot(Depot depot){
        this.depotList.add(depot);
        return true;
    }
    
    public Boolean removeDepot(Depot depot){
        int i=0;
        for(Depot d : this.depotList){
            if(d.getId() == d.getId()){
                this.depotList.remove(i);
                return true;
            }
            i++;
        }
        return false;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fostw.ejb.bank.entity.Customer[ id=" + id + " ]";
    }
}
