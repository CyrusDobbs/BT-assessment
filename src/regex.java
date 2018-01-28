public class regex {

    // Returns true if string matches regular expression.
    public static boolean checkMonitorTime(String monitorTime){
        return monitorTime.matches("\\d{13}");
    }
    public static boolean checkNodeName(String nodeName){
        return nodeName.matches("[a-zA-Z0-9]+");
    }
    public static boolean checkHello(String hello){return hello.matches("HELLO");}
    public static boolean checkLostFound(String lostFound){
        return lostFound.matches("LOST|FOUND");
    }
}