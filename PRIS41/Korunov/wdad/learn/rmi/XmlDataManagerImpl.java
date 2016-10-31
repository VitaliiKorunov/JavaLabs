/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PRIS41.Korunov.wdad.learn.rmi;

import PRIS41.Korunov.wdad.learn.xml.XmlTask;
import java.io.IOException;
import java.util.Calendar;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author vkoru_000
 */
class XmlDataManagerImpl implements XmlDataManager {
    private XmlTask xmlTask;
    
    public XmlDataManagerImpl(){
        try {
            xmlTask = new XmlTask();
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            ex.printStackTrace();
        }
    } 
    
    @Override
    public double getBill(Building building, int flatNumber) {
        return xmlTask.getBill(building.getStreet(), building.getNumber(), flatNumber);
    }

    @Override
    public Flat getFlat(Building building, int flatNumber) {
        return xmlTask.getFlat(building, flatNumber);
    }

    @Override
    public void setTariff(String tariffName, double newValue) {
        try {
            xmlTask.setTariff(tariffName, newValue);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
     public void addRegistration(Building building, int flatNumber, Registration registration) {
        try {
            xmlTask.addRegistration(building.getStreet(),building.getNumber(), flatNumber,
                    registration.getDate().get(Calendar.YEAR), registration.getDate().get(Calendar.MONTH),
                    registration.getColdwater(),registration.getHotwater(),
                    registration.getElectricity(),registration.getGas());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
     
}
