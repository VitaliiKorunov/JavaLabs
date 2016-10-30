package PRIS41.Korunov.wdad.data.managers;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PreferencesManager {
    
    private static PreferencesManager instance;
    private final static String path = "src/PRIS41/Korunov/wdad/resources/configuration/appconfig.xml";
    private Document doc;
    
    private PreferencesManager() throws IOException, ParserConfigurationException, SAXException{
        generateDocument();
    }
    
    public static PreferencesManager getInstance() throws ParserConfigurationException, IOException, SAXException {
        if (instance == null) {
            instance = new PreferencesManager();
        }
        return instance;
    }
    
    public void setProperty(String key, String value) 
            throws IOException{
        getPropertyElement(key).setTextContent(value);
        updateDoc();
    }
    
    public String getProperty(String key){
        return getPropertyElement(key).getTextContent();
    }
    
    public void setProperties(Properties prop) throws IOException{
        for (String key : prop.stringPropertyNames()) {
            setProperty(key, prop.getProperty(key));
        }
    }
        
    public Properties getProperties() {
        Properties props = new Properties();
        XPath xPath = XPathFactory.newInstance().newXPath();
        String expression = "//*[not(*)]";

        try {
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                String key = getNodePath(nodeList.item(i));
                props.put(key, getProperty(key));
            }
        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
        }
        return props;
    }
    
    public void addBindedObject(String name, String className) throws IOException{
        Element bindedObject = doc.createElement("bindedobject");
        bindedObject.setAttribute("name", name);
        bindedObject.setAttribute("class", className);
        Element server = (Element)doc.getElementsByTagName("server").item(0);
        server.appendChild(bindedObject);
        updateDoc();
    }
    
    public void removeBindedObject(String name) throws IOException{
        NodeList bindedObjects = doc.getElementsByTagName("bindedobject");
        for (int i = 0; i < bindedObjects.getLength(); i++) {
            Element bindedObject = (Element)bindedObjects.item(i);
            if (name.equals(bindedObject.getAttribute("name"))) {
                bindedObject.getParentNode().removeChild(bindedObject);
            }
        }
        updateDoc();
    }
    
    private Element getPropertyElement(String key){
        String[] tags = key.split("\\.");
        return (Element)doc.getElementsByTagName(tags[tags.length - 1]).item(0);
    }
    
    private String getNodePath(Node node) {
        Node parent = node.getParentNode();
        if (parent.getNodeName().equals("#document")) {
            return node.getNodeName();
        }
        return getNodePath(parent) + '.' + node.getNodeName();
    }
    
    @Deprecated
    public boolean isCreateRegistry() {
        NodeList nodeList = doc.getElementsByTagName("createregistry");
        return (nodeList.item(0).getTextContent().equals("yes"));
    }

    @Deprecated
    public void setCreateRegistry(boolean createRegistry) throws IOException {
        NodeList nodeList = doc.getElementsByTagName("createregistry");
        if (createRegistry) {
            nodeList.item(0).setTextContent("yes");
        } 
        else {
            nodeList.item(0).setTextContent("no");
        }
        updateDoc();
    }
    
    @Deprecated
    public String getRegistryAddress() {
        NodeList nodeList = doc.getElementsByTagName("registryaddress");
        return nodeList.item(0).getTextContent();
    }

    @Deprecated
    public void setRegistryAddress(String s) throws IOException {
        NodeList nodeList = doc.getElementsByTagName("registryaddress");
        nodeList.item(0).setTextContent(s);
        updateDoc();
    }

    @Deprecated
    public int getRegistryPort() {
        NodeList nodeList = doc.getElementsByTagName("registryport");
        return Integer.parseInt(nodeList.item(0).getTextContent());
    }

    @Deprecated
    public void setRegistryPort(int registryPort) throws IOException {
        NodeList nodeList = doc.getElementsByTagName("registryport");
        nodeList.item(0).setTextContent(String.valueOf(registryPort));
        updateDoc();
    }
    
    @Deprecated
    public String getPolicyPath() {
        NodeList nodeList = doc.getElementsByTagName("policypath");
        return nodeList.item(0).getTextContent();
    }

    @Deprecated
    public void setPolicyPath(String s) throws IOException {
        NodeList nodeList = doc.getElementsByTagName("policypath");
        nodeList.item(0).setTextContent(s);
        updateDoc();
    }

    @Deprecated
    public boolean getUseCodeBaseOnly() {
        NodeList nodeList = doc.getElementsByTagName("usecodebaseonly");
        return nodeList.item(0).getTextContent().equals("yes");
    }

    @Deprecated
    public void setUseCodeBaseOnly(boolean useCodeBaseOnly) throws IOException {
        NodeList nodeList = doc.getElementsByTagName("usecodebaseonly");
        if (useCodeBaseOnly) {
            nodeList.item(0).setTextContent("yes");
        } else {
            nodeList.item(0).setTextContent("no");
        }
        updateDoc();
    }

    @Deprecated
    public String getClassProvider() {
        NodeList nodeList = doc.getElementsByTagName("classprovider");
        return nodeList.item(0).getTextContent();
    }

    @Deprecated
    public void setClassProvider(String classproviderURL) throws IOException {
        NodeList nodeList = doc.getElementsByTagName("classprovider");
        nodeList.item(0).setTextContent(classproviderURL);
        updateDoc();
    }
    
    private void generateDocument() 
            throws IOException, ParserConfigurationException, SAXException {
        
        File xmlFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(xmlFile);
    }
    
    private void updateDoc() throws IOException {
        DOMImplementationLS domImplementationLS =
                (DOMImplementationLS) doc.getImplementation().getFeature("LS", "3.0");
        LSOutput lsOutput = domImplementationLS.createLSOutput();
        FileOutputStream outputStream = new FileOutputStream(path);
        lsOutput.setByteStream(outputStream);
        LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
        lsSerializer.write(doc, lsOutput);
        outputStream.close();
    }
}
