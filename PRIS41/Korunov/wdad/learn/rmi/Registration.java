/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PRIS41.Korunov.wdad.learn.rmi;

import java.util.Calendar;


/**
 *
 * @author vkoru_000
 */
public class Registration {
    private Calendar date;
    private double coldwater;
    private double hotwater;
    private double electricity;
    private double gas;
    
    public Registration(Calendar date, double coldwater, 
            double hotwater, double electricity, double gas){
        this.date = date;
        this.coldwater = coldwater;
        this.hotwater = hotwater;
        this.electricity = electricity;
        this.gas = gas;
    }
    
    public Calendar getDate(){
        return date;
    }
    
    public void setDate(Calendar date){
        this.date = date;
    }
    
    public double getColdwater(){
        return coldwater;
    }
    
    public void setColdwater(double coldwater){
        this.coldwater = coldwater;
    }
    
    public double getHotwater(){
        return hotwater;
    }
    
    public void setHotwater(double hotwater){
        this.hotwater = hotwater;
    }
    
    public double getElectricity(){
        return electricity;
    }
    
    public void setElectricity(double electricity){
        this.electricity = electricity;
    }
    
    public double getGas(){
        return gas;
    }
    
    public void setGas(double gas){
        this.gas = gas;
    }
}
