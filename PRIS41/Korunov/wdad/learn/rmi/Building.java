/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PRIS41.Korunov.wdad.learn.rmi;

/**
 *
 * @author vkoru_000
 */
public class Building {
    private String street;
    private int number;
    
    public Building(String street, int number){
        this.street = street;
        this.number = number;
    }
    
    public void setStreet(String street){
        this.street = street;    
    }
    
    public String getStreet(){
        return street;    
    }
    
    public void getNumber(int number){
        this.number = number;    
    }
    
    public int getNumber(){
        return number;    
    }
}
