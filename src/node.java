public class node {
    private String nodeName;
    private String status;
    private String timeMonitorLastIndicator;
    private long i;
    private String timeNodeLastIndicator;
    private String lastIndicator;

    public node(String name, String initialStatus, String statusIndicator, String timeMonitorOfIndicator, String timeNodeOfIndicator) {
        nodeName = name;
        status = initialStatus;
        timeMonitorLastIndicator = timeMonitorOfIndicator;
        timeNodeLastIndicator = timeNodeOfIndicator;
        lastIndicator = statusIndicator;
        i = Long.parseLong(timeNodeLastIndicator);
    }

    public String getNodeName(){return nodeName;}
    public long getTimeOfNodeIndicator(){return i;}
    public String createOutputString(){
        return nodeName + " " + status + " " + timeMonitorLastIndicator + " " + lastIndicator;
    }

    public void setMostRecentIndicator(String newIndicator){
        lastIndicator = newIndicator;
    }
    public void setTimeMonitorLastIndicator(String time){
        timeMonitorLastIndicator = time;
    }
    public void setTimeNodeLastIndicator(String time){
        timeNodeLastIndicator = time;
        i = Long.parseLong(timeNodeLastIndicator);
    }
    public void setStatus(String newStatus){
        status = newStatus;
    }
}
