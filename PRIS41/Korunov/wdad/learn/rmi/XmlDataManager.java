/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PRIS41.Korunov.wdad.learn.rmi;

import java.io.Serializable;
import java.rmi.Remote;

/**
 *
 * @author vkoru_000
 */
public interface XmlDataManager extends Remote, Serializable {
    double getBill(Building building, int flatNumber);
    Flat getFlat(Building building, int flatNumber);
    void setTariff(String tariffName, double newValue);
    void addRegistration (Building building, int flatNumber, Registration registration);
}
