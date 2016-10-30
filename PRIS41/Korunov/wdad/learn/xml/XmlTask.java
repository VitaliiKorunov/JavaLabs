package PRIS41.Korunov.wdad.learn.xml;

import PRIS41.Korunov.wdad.learn.rmi.Building;
import PRIS41.Korunov.wdad.learn.rmi.Flat;
import PRIS41.Korunov.wdad.learn.rmi.Registration;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class XmlTask {
    
    private class RegistrationValues  {
        public double coldwaterReg = 0;
        public double hotwaterReg = 0;
        public double electricityReg = 0;
        public double gasReg = 0;
    }
    
    private Document doc;
    private String path;
    
    public XmlTask() 
            throws IOException, ParserConfigurationException, SAXException{ 
        this("src/PRIS41/Korunov/wdad/learn/xml/housekeeper.xml");
        
        generateDocument();
    }       
    
    public XmlTask(String path) 
            throws IOException, ParserConfigurationException, SAXException{ 
        this.path = path;
        generateDocument();
    } 
    
    public double getBill(String street, int buildingNumber, int flatNumber){
        
        double sum;
        
        NodeList buildings = doc.getElementsByTagName("building");
        NodeList tariffs = doc.getElementsByTagName("tariffs");
        
        NodeList flats = getFlatsNeededBuild(buildings, street, buildingNumber);
        NodeList registrations = getRegistrationsNeededFlat(flats, flatNumber);
        
        Node lastRegistration = getLastRegistretion(registrations);
        int lastYearReg = 0;
        int lastMonthReg = 0;
        Node prevRegistration = null;
        if(lastRegistration != null){
            lastYearReg = Integer.valueOf(lastRegistration.getAttributes().getNamedItem("year").getNodeValue());
            lastMonthReg = Integer.valueOf(lastRegistration.getAttributes().getNamedItem("month").getNodeValue());
            prevRegistration = getPrevRegistration(registrations, lastYearReg, lastMonthReg);
        }      
        
        RegistrationValues reg = new RegistrationValues();
        if(lastRegistration !=null && prevRegistration !=  null){
            RegistrationValues lastReg = getReg(lastRegistration);
            RegistrationValues prevReg = getReg(prevRegistration);
            reg.coldwaterReg = lastReg.coldwaterReg - prevReg.coldwaterReg;
            reg.hotwaterReg = lastReg.hotwaterReg - prevReg.hotwaterReg;
            reg.electricityReg = lastReg.electricityReg - prevReg.electricityReg;
            reg.gasReg = lastReg.gasReg - prevReg.gasReg;
        } 
        
        sum = reg.coldwaterReg * Double.parseDouble(tariffs.item(0).getAttributes().getNamedItem("coldwater").getNodeValue()) +
                reg.hotwaterReg * Double.parseDouble(tariffs.item(0).getAttributes().getNamedItem("hotwater").getNodeValue()) +
                reg.electricityReg * Double.parseDouble(tariffs.item(0).getAttributes().getNamedItem("electricity").getNodeValue()) +
                reg.gasReg * Double.parseDouble(tariffs.item(0).getAttributes().getNamedItem("gas").getNodeValue());
        
        return sum;
    }   
    
    public void setTariff(String tariffName, double newValue) throws IOException {
        NodeList tariffs = doc.getElementsByTagName("tariffs");
        tariffs.item(0).getAttributes().getNamedItem(tariffName).setNodeValue(String.valueOf(newValue));       
        updateDocument();
    }
    
    public void addRegistration (String street, int buildingNumber, int flatNumber, 
            int year, int month,double coldWater, 
            double hotWater, double electricity, double gas) throws IOException{
        
        NodeList buildings = doc.getElementsByTagName("building");
        NodeList flats = getFlatsNeededBuild(buildings, street, buildingNumber);
        
        for (int i = 0; i < flats.getLength(); i++) {
            if(Integer.valueOf(flats.item(i).getAttributes().getNamedItem("number").getNodeValue()) == flatNumber){
                Node newReg = doc.createElement("registration");
                
                ((Element)newReg).setAttribute("year", String.valueOf(year));
                ((Element)newReg).setAttribute("month", String.valueOf(month));
                
                Node coldwater = doc.createElement("coldwater");
                coldwater.setNodeValue(String.valueOf(coldWater));
                
                Node hotwater = doc.createElement("hotwater");
                hotwater.setNodeValue(String.valueOf(hotWater));
                
                Node el = doc.createElement("electricity");
                el.setNodeValue(String.valueOf(electricity));
                
                Node g = doc.createElement("gas");
                g.setNodeValue(String.valueOf(gas));
                
                newReg.appendChild(coldwater);
                newReg.appendChild(hotwater);
                newReg.appendChild(el);
                newReg.appendChild(g);
                
                flats.item(i).appendChild(newReg);
                updateDocument();
            }
        }
    }
    
    public Flat getFlat(Building building, int flatNumber){
        NodeList buildings = doc.getElementsByTagName("building");
        NodeList flats = getFlatsNeededBuild(buildings, building.getStreet(), building.getNumber());
        NodeList registrations = getRegistrationsNeededFlat(flats, flatNumber);
        int personsQuantity=0;
        double area=0;
        
        ArrayList<Registration> regs = new ArrayList();
        
        if(flats != null){
            for (int i= 0; i < flats.getLength(); i++) { //поиск нужной квартиры
                if (flats.item(i).getNodeName().equals("flat") && 
                        Integer.valueOf(flats.item(i).getAttributes().getNamedItem("number").getNodeValue()) == flatNumber){
                    personsQuantity = Integer.valueOf(flats.item(i).getAttributes().getNamedItem("personsquantity").getNodeValue());
                    area = Double.valueOf(flats.item(i).getAttributes().getNamedItem("area").getNodeValue());
                }
            }
        }    
        
        if(registrations != null){
            for (int i = 0; i < registrations.getLength(); i++) {
                if(registrations.item(i).getNodeName().equals("registration")){ 
                    
                    int year = Integer.valueOf(registrations.item(i).getAttributes().getNamedItem("year").getNodeValue());
                    int month = Integer.valueOf(registrations.item(i).getAttributes().getNamedItem("month").getNodeValue());
                    
                    Calendar registrationDate = Calendar.getInstance();
                    registrationDate.set(Calendar.YEAR, year);
                    registrationDate.set(Calendar.MONTH, month);
                    
                    RegistrationValues regValues = getReg(registrations.item(i));
                    
                    Registration reg = new Registration(registrationDate, regValues.coldwaterReg,
                            regValues.hotwaterReg, regValues.electricityReg, regValues.gasReg);
                    regs.add(reg);
                }
            }
        }
        
        Flat flat = new Flat(flatNumber, personsQuantity, area, regs);        
        return flat;
    }
    
    private void generateDocument() 
            throws IOException, ParserConfigurationException, SAXException {
        
        File xmlFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(xmlFile);
    }
    
    private void updateDocument() 
            throws IOException {
        
        DOMImplementationLS domImplementationLS =
                (DOMImplementationLS)doc.getImplementation().getFeature("LS", "3.0");
        LSOutput lsOutput = domImplementationLS.createLSOutput();
        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            lsOutput.setByteStream(outputStream);
            LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
            lsSerializer.write(doc, lsOutput);
        }
    }
    
    private NodeList getFlatsNeededBuild(NodeList buildings, String street, int buildingNumber){
        NodeList flats = null;
        if(buildings != null){   
            for (int i = 0; i < buildings.getLength(); i++) { //поиск нужного дома
                if(buildings.item(i).getAttributes().getNamedItem("street").getNodeValue().equals(street) &&
                        Integer.valueOf(buildings.item(i).getAttributes().getNamedItem("number").getNodeValue()) == buildingNumber){
                    flats = buildings.item(i).getChildNodes();
                }
            }
        }
        return flats;
    }
    
    private NodeList getRegistrationsNeededFlat(NodeList flats, int flatNumber){
        NodeList registrations = null;
        if(flats != null){
            for (int i= 0; i < flats.getLength(); i++) { //поиск нужной квартиры
                if (flats.item(i).getNodeName().equals("flat") && 
                        Integer.valueOf(flats.item(i).getAttributes().getNamedItem("number").getNodeValue()) == flatNumber){
                    registrations = flats.item(i).getChildNodes();
                }
            }
        }
        return registrations;
    }
    
    private Node getLastRegistretion(NodeList registrations){        
        Node registration = null;
        if(registrations != null){
            int lastMonthReg = 0;
            int lastYearReg = 0;
            for (int i = 0; i < registrations.getLength(); i++) { //поиск последней регистрации 
                if(registrations.item(i).getNodeName().equals("registration") &&
                        Integer.valueOf(registrations.item(i).getAttributes().getNamedItem("year").getNodeValue()) >= lastYearReg &&
                        Integer.valueOf(registrations.item(i).getAttributes().getNamedItem("month").getNodeValue()) >= lastMonthReg){
                    registration = registrations.item(i);
                } 
            }
        }
        return registration;
    }
    
    private Node getPrevRegistration(NodeList registrations, int lastYearReg, int lastMonthReg){
        Node registration = null;
        if(registrations != null){
            for (int i = 0; i < registrations.getLength(); i++) { // поиск предпоследней регистрации
                if(registrations.item(i).getNodeName().equals("registration") &&
                        Integer.valueOf(registrations.item(i).getAttributes().getNamedItem("year").getNodeValue()) == lastYearReg &&
                        Integer.valueOf(registrations.item(i).getAttributes().getNamedItem("month").getNodeValue()) == lastMonthReg - 1){                
                        registration = registrations.item(i);
                }
            }

            if(registration == null){
                for (int i = 0; i < registrations.getLength(); i++) { // поиск предпоследней регистрации
                    if(registrations.item(i).getNodeName().equals("registration") &&
                            Integer.valueOf(registrations.item(i).getAttributes().getNamedItem("year").getNodeValue()) == lastYearReg - 1 &&
                            Integer.valueOf(registrations.item(i).getAttributes().getNamedItem("month").getNodeValue()) == 12){
                        registration = registrations.item(i);
                    }
                }
            }  
        }
        return registration;
    }   

    private RegistrationValues getReg(Node registration){
        RegistrationValues reg = new RegistrationValues();
        NodeList attributes = registration.getChildNodes();
        for (int i = 0; i < attributes.getLength(); i++) {
            if(attributes.item(i).getNodeName().equals("coldwater")){
                reg.coldwaterReg = Integer.valueOf(attributes.item(i).getTextContent());
            }
            if(attributes.item(i).getNodeName().equals("hotwater")){
                reg.hotwaterReg = Integer.valueOf(attributes.item(i).getTextContent());
            }
            if(attributes.item(i).getNodeName().equals("electricity")){
                reg.electricityReg = Integer.valueOf(attributes.item(i).getTextContent());
            }
            if(attributes.item(i).getNodeName().equals("gas")){
                reg.gasReg = Integer.valueOf(attributes.item(i).getTextContent());
            }
        }
        return reg;
    }
}
