import java.util.Scanner;
import java.util.Vector;
import java.io.FileNotFoundException;
import java.io.File;

public class nodeReporter {

    private static Vector<String> notifications = new Vector<String>();
    private static Vector<node> nodes = new Vector<node>();

    public static void main(String[] args) throws FileNotFoundException {

        // Ensure an input file has been given, if so read it
        if (args.length == 1) {
            fileRead(args[0]);
        } else {
            throw new IllegalArgumentException("Exactly 1 parameter required");
        }

        // Iterate through notifications
        for (String notificationRead : notifications) {

            // Create array from parts the current notification
            String[] splitNotification = notificationRead.split("\\s+");

            // Check if notification is a 'HELLO' statement
            if (splitNotification.length == 4) {

                // Ensure validity of input
                inputCheck((notifications.indexOf(notificationRead)+1), splitNotification[0], splitNotification[1], splitNotification[2], splitNotification[3], null);

                // Do no check if node is already present if no nodes have been added yet
                if (nodes.size() != 0) {

                    // Check to see if node is present
                    int indexOfExistingNode = nodeSearch(splitNotification[2]);

                    // If node is not present, create it
                    if (indexOfExistingNode == -1) {
                        nodes.add(new node(splitNotification[2], "ALIVE", splitNotification[2] + " " + splitNotification[3], splitNotification[0], splitNotification[1]));
                    }

                    // If node already exists, update its status if the notification is more up to date
                    else {
                        if (nodes.get(indexOfExistingNode).getTimeOfNodeIndicator() < Long.parseLong(splitNotification[1])) {
                            nodes.get(indexOfExistingNode).setStatus("ALIVE");
                            nodes.get(indexOfExistingNode).setMostRecentIndicator(splitNotification[2] + " " + splitNotification[3]);
                            nodes.get(indexOfExistingNode).setTimeMonitorLastIndicator(splitNotification[0]);
                            nodes.get(indexOfExistingNode).setTimeNodeLastIndicator(splitNotification[1]);
                        }
                    }
                }

                // Add the node mentioned in the first 'HELLO' statement
                else {
                    nodes.add(new node(splitNotification[2], "ALIVE", splitNotification[2] + " " + splitNotification[3], splitNotification[0], splitNotification[1]));
                }
            }

            // Notification must be a 'LOST' or 'FOUND' statement
            else if (splitNotification.length == 5) {

                // Check validity of input
                inputCheck((notifications.indexOf(notificationRead)+1), splitNotification[0], splitNotification[1], splitNotification[2], splitNotification[3], splitNotification[4]);

                // Check if LOST/FOUND node is already present
                int indexOfExistingNode = nodeSearch(splitNotification[4]);

                // Retrieve index of node that has LOST/FOUND another node (we call this the Active Node)
                int indexOfActiveNode = nodeSearch(splitNotification[2]);

                // If node is not present, create it
                if (indexOfExistingNode == -1) {

                    // If new node is LOST then it is DEAD
                    if (splitNotification[3].equals("LOST")) {
                        // Create LOST node
                        nodes.add(new node(splitNotification[4], "DEAD", splitNotification[2] + " " + splitNotification[3] + " " + splitNotification[4], splitNotification[0], splitNotification[1]));
                    }

                    // If new node is FOUND then it is ALIVE
                    else {
                        // Create FOUND node
                        nodes.add(new node(splitNotification[4], "ALIVE", splitNotification[2] + " " + splitNotification[3] + " " + splitNotification[4], splitNotification[0], splitNotification[1]));
                    }
                }

                // If LOST/FOUND node already exists, update its status
                else {

                    // Only update existing node if notification is more up to date
                    if (nodes.get(indexOfExistingNode).getTimeOfNodeIndicator() < Long.parseLong(splitNotification[1])) {
                        nodes.get(indexOfExistingNode).setMostRecentIndicator(splitNotification[2] + " " + splitNotification[3] + " " + splitNotification[4]);
                        nodes.get(indexOfExistingNode).setTimeMonitorLastIndicator(splitNotification[0]);
                        nodes.get(indexOfExistingNode).setTimeNodeLastIndicator(splitNotification[1]);

                        // Update LOST node as DEAD
                        if (splitNotification[3].equals("LOST")) {
                            nodes.get(indexOfExistingNode).setStatus("DEAD");
                        }

                        // Update FOUND node as ALIVE
                        else {
                            nodes.get(indexOfExistingNode).setStatus("ALIVE");
                        }
                    }
                }

                // Update Active Node if this notification is more up to date
                if (nodes.get(indexOfActiveNode).getTimeOfNodeIndicator() < Long.parseLong(splitNotification[1])) {
                    // Active Node must be ALIVE to lose another node
                    nodes.get(indexOfActiveNode).setStatus("ALIVE");
                    nodes.get(indexOfActiveNode).setMostRecentIndicator(splitNotification[2] + " " + splitNotification[3] + " " + splitNotification[4]);
                    nodes.get(indexOfActiveNode).setTimeMonitorLastIndicator(splitNotification[0]);
                    nodes.get(indexOfActiveNode).setTimeNodeLastIndicator(splitNotification[1]);
                }
            }
            else {
                System.out.println("Input invalid on Line " + (notifications.indexOf(notificationRead)+1) + ", exiting...");
                System.exit(0);
            }
        }

        // Print most up to date info on each existing node;
        printToConsole();

    }

    // Read file in and split it line by line
    public static void fileRead(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            while (sc.hasNextLine()) {
                String notification = sc.nextLine();
                notifications.add(notification);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    // Searches for a node by nodeName
    // Returns nodes index OR -1 if not found
    public static int nodeSearch(String nodeName) {
        int indexOfActiveNode = -1;
        for (node currentNode : nodes) {
            indexOfActiveNode += 1;
            if (currentNode.getNodeName().equals(nodeName)) {
                return indexOfActiveNode;
            }
        }
        return -1;
    }

    // Uses regex to validate input
    public static void inputCheck(int notificationIndex, String monitorTime, String nodeTime, String activeNode, String action, String existingNode) {
        if (existingNode == null) {
            if (!regex.checkMonitorTime(monitorTime) |
                    !regex.checkMonitorTime(nodeTime) |
                    !regex.checkNodeName(activeNode) |
                    !regex.checkHello(action)
                    ) {
                System.out.println("Input invalid on Line " + notificationIndex + ", exiting...");
                System.exit(0);
            }
        }
        else {
            if (!regex.checkMonitorTime(monitorTime) |
                    !regex.checkMonitorTime(nodeTime) |
                    !regex.checkNodeName(activeNode) |
                    !regex.checkLostFound(action) |
                    !regex.checkNodeName(existingNode)
                    ) {
                System.out.println("Input invalid on Line " + notificationIndex + ", exiting...");
                System.exit(0);
            }
        }
    }

    // Prints latest information on nodes to console
    public static void printToConsole() {
        for (node currentNode : nodes) {
            System.out.println(currentNode.createOutputString());
        }
    }
}
