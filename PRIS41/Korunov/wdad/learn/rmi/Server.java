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
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author vkoru_000
 */
public class Server {

     private static PreferencesManager preferencesManager;
    
    private static final String XML_DATA_MANAGER = "XmlDataManager";
    private static final int XML_DATA_MANAGER_PORT = 33333;
    
     public static void main(String[] args) throws IOException{
         
         try {
            preferencesManager = PreferencesManager.getInstance();
        }catch (IOException | SAXException | ParserConfigurationException ex) {
            ex.printStackTrace();
        }
         
        System.setProperty("java.rmi.server.codebase", PreferencesConstantManager.CLASS_PROVIDER);
        System.setProperty("java.rmi.server.UseCodeBaseOnly", preferencesManager.getProperty(PreferencesConstantManager.USE_CODE_BASE_ONLY));
        System.setProperty("java.rmi.server.logCalls", "true");
        System.setProperty("java.security.policy", preferencesManager.getProperty(PreferencesConstantManager.POLICY_PATH));
        System.setSecurityManager(new SecurityManager());
        
        Registry registry = null;
        try{
            
            if(preferencesManager.getProperty(PreferencesConstantManager.CREATE_REGISTRY).equals("yes"))
                registry = LocateRegistry.createRegistry(Integer.parseInt(preferencesManager.getProperty(PreferencesConstantManager.REGISTRY_PORT)));
            else
                registry = LocateRegistry.getRegistry(Integer.parseInt(preferencesManager.getProperty(PreferencesConstantManager.REGISTRY_PORT)));
            
        }catch (RemoteException ex){
            ex.printStackTrace();
            System.err.println("cant locate registry");
        }
        
        if(registry != null){
            try{
                System.out.println("exporting object ...");
                XmlDataManagerImpl xmlDataManagerImpl = new XmlDataManagerImpl();
                UnicastRemoteObject.exportObject(xmlDataManagerImpl, XML_DATA_MANAGER_PORT);
                registry.rebind(XML_DATA_MANAGER, xmlDataManagerImpl);
                preferencesManager.addBindedObject(XML_DATA_MANAGER, "RPIS41.Korunov.wdad.learn.rmi.XmlDataMangerImpl");
                System.out.println("Waiting ... ");
                System.out.println("Input \"exit\" to close server.");
                Scanner scanner = new Scanner(System.in);
                while(true){
                    String input = scanner.nextLine();
                    if(input.equals("exit")) {
                        try {
                            registry.unbind(XML_DATA_MANAGER);
                            preferencesManager.removeBindedObject(XML_DATA_MANAGER);
                            System.exit(0);
                        }catch (NotBoundException ex){
                            ex.printStackTrace();
                        }
                    }
                }
            }catch (RemoteException ex){
                ex.printStackTrace();
                System.err.println("cant export or bind object");
            }
        }
     }
}
