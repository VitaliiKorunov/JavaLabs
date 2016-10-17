package PRIS41.Korunov.wdad.learn.xml;

public class TestXmlTask {
    public static void main(String[] args) {
        try {
            XmlTask test = new XmlTask();
            test.setTariff("gas", 105);
            System.out.println("Change Complited");
            System.out.println(test.getBill("michurina", 20, 1));
            test.addRegistration("michurina", 20, 1, 2012, 2, 339, 226, 159, 125);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
