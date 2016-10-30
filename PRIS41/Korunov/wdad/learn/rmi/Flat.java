/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PRIS41.Korunov.wdad.learn.rmi;

import java.util.List;

/**
 *
 * @author vkoru_000
 */
public class Flat {
    private int number;
    private int personsQuantity;
    private double area;
    private List<Registration> registrations;
    
    public Flat(int number, int personsQuantity, 
            double area, List<Registration> registrations){
        
        this.number = number;
        this.personsQuantity = personsQuantity;
        this.area = area;
        this.registrations = registrations;
    }
    
    public int getNumber(){
        return number;
    }
    
    public void setNumber(int number){
        this.number = number;
    }
    
    public int getPersonsQuantity(){
        return personsQuantity;
    }
    
    public void setPersonsQuantity(int personsQuantity){
        this.personsQuantity = personsQuantity;
    }
    
    public double getArea(){
        return area;
    }
    
    public void setArea(double area){
        this.area = area;;
    }
    
    public List<Registration> getRegistrations(){
        return registrations;
    }
    
    public void setRegistrations(List<Registration> registrations){
        this.registrations = registrations;
    }
}
