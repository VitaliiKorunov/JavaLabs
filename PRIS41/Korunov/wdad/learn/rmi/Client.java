/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PRIS41.Korunov.wdad.learn.rmi;

import PRIS41.Korunov.wdad.utils.PreferencesConstantManager;
import PRIS41.Korunov.wdad.data.managers.PreferencesManager;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Calendar;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author vkoru_000
 */
public class Client {
    
    private static PreferencesManager preferencesManager;
    private static final String XML_DATA_MANAGER = "XmlDataManager";
    
    public static void main(String[] args) throws NotBoundException{
    
        try {
            preferencesManager = PreferencesManager.getInstance();
        } catch (ParserConfigurationException | IOException | SAXException ex) {
            ex.printStackTrace();
        }
        
        System.setProperty("java.rmi.server.codebase", PreferencesConstantManager.CLASS_PROVIDER);
        System.setProperty("java.rmi.server.useCodebaseOnly", preferencesManager.getProperty(PreferencesConstantManager.USE_CODE_BASE_ONLY));
        System.setProperty("java.security.policy", preferencesManager.getProperty(PreferencesConstantManager.POLICY_PATH));
        System.setSecurityManager(new SecurityManager());
        
        Registry registry = null;
        try{
            registry = LocateRegistry.getRegistry(
                    preferencesManager.getProperty(PreferencesConstantManager.REGISTRY_ADDRESS),
                    Integer.parseInt(preferencesManager.getProperty(PreferencesConstantManager.REGISTRY_PORT)));
        } catch (RemoteException ex) {
            ex.printStackTrace();
            System.err.println("cant locate registry");
        }
        if(registry != null){
            
            try{
                XmlDataManager xmlDataManager = (XmlDataManager)registry.lookup(XML_DATA_MANAGER);
                doWork(xmlDataManager);
            }catch (RemoteException | NotBoundException e){
                System.err.println("Your code don't work");
                e.printStackTrace();
            }
        }
    }
    
    private static void doWork(XmlDataManager xmlDataManager) {    
        
        Building b = new Building("michurina",20);
        System.out.println(xmlDataManager.getBill(b, 1));
        
       Flat flat = xmlDataManager.getFlat(b, 3);
       System.out.println("flat info: number - " + flat.getNumber() + ", area - " + flat.getArea() +
               ", personsQuantity" + flat.getPersonsQuantity());
       System.out.println("Registration : ");
       List<Registration> regs = flat.getRegistrations();
        for (int i = 0; i < regs.size(); i++) {
            
            Registration reg = regs.get(i);
            System.out.println("Registration "+ i + ":");
            System.out.println("date: year -" + reg.getDate().get(Calendar.YEAR) + 
                    ", month" + reg.getDate().get(Calendar.MONTH));
            System.out.println("coldwater:" + reg.getColdwater());
            System.out.println("hotwater:" + reg.getHotwater());
            System.out.println("el:" + reg.getElectricity());
            System.out.println("gas:" + reg.getGas());
            
        }
        
        Calendar registrationDate = Calendar.getInstance();
        registrationDate.set(Calendar.YEAR, 2011);
        registrationDate.set(Calendar.MONTH, 4);
        
        Registration registration = new Registration(registrationDate, 351, 224, 166, 131);
        xmlDataManager.setTariff("gas", 110);
        xmlDataManager.addRegistration(b, 3, registration);
    }
}
