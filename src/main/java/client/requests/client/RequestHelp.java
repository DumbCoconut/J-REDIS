package client.requests.client;

import client.requests.Request;
import client.requests.RequestName;
import client.requests.exceptions.NoTokensException;

import java.util.*;
import java.util.stream.Collectors;

public class RequestHelp extends Request {
    /**
     * Separate the different blocks (one per request).
     */
    private final String SEPARATOR = "\n--------------------\n";

    /**
     * List containing all the commands where help has been requested.
     */
    ArrayList<String> requests;

    /**
     * Constructor
     * @param tokens The different words of the request, e.g {"help", "get", "set"}.
     * @throws NoTokensException When no tokens are provided to the request.
     */
    public RequestHelp(ArrayList<String> tokens) throws NoTokensException {
        super(tokens);
        requests = new ArrayList<>();
        parse();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return getMessage();
    }

    /**
     * Parse the tokens and retrieve the commands where help has been requested.
     */
    public void parse() {
        /* sublist because we skip "help" */
        /* remove the duplicates */
        /* put everything to lower case */
        List<String> upperCase = tokens.subList(1, tokens.size()).stream()
                                                                 .map(String::toUpperCase)
                                                                 .distinct()
                                                                 .collect(Collectors.toList());

        /* add the sublist as our requests */
        requests.addAll(upperCase);

        /* sort requests alphabetically */
        requests.sort(String::compareToIgnoreCase);
    }

    /**
     * Get the separator.
     * @return The separator.
     */
    public String getSeparator() {
        return SEPARATOR;
    }

    /**
     * Get all the commands where help has been requested.
     * @return all the commands where help has been requested.
     */
    public ArrayList<String> getRequests() {
        return requests;
    }

    /**
     * Get the message containing information about all the commands where help has been requested.
     * @return Help containing information about all the commands where help has been requested.
     */
    public String getMessage() {
        if (tokens.size() == 1) {
            return getHelp();
        }

        // create the help
        // we do not need to sort because requests is already sorted
        ArrayList<String> res = new ArrayList<>();
        for (String cmd : requests) {
            if (cmd.equals(RequestName.getInstance().getHelpCmd())) {
                res.add(getHelpHelp());
            } else if (cmd.equals(RequestName.getInstance().getQuitCmd())) {
                res.add(getHelpQuit());
            } else if (cmd.equals(RequestName.getInstance().getExitCmd())) {
                res.add(getHelpExit());
            } else if (cmd.equals(RequestName.getInstance().getAddServerCmd())) {
                res.add(getHelpAddServer());
            } else if (cmd.equals(RequestName.getInstance().getGetCmd())) {
                res.add(getHelpGet());
            } else if (cmd.equals(RequestName.getInstance().getSetCmd())) {
                res.add(getHelpSet());
            } else if (cmd.equals(RequestName.getInstance().getDecrCmd())) {
                res.add(getHelpDecr());
            } else if (cmd.equals(RequestName.getInstance().getDecrByCmd())) {
                res.add(getHelpDecrBy());
            } else if (cmd.equals(RequestName.getInstance().getIncrCmd())) {
                res.add(getHelpIncr());
            } else if (cmd.equals(RequestName.getInstance().getIncrByCmd())) {
                res.add(getHelpIncrBy());
            } else if (cmd.equals(RequestName.getInstance().getTypeCmd())) {
                res.add(getHelpType());
            } else if (cmd.equals(RequestName.getInstance().getDelCmd())) {
                res.add(getHelpDel());
            } else {
                res.add(cmd.toUpperCase() + " : (error) I'm sorry, I don't recognize the command \"" +
                                            cmd.toUpperCase() + "\". " + "Did you mean \"" +
                                            RequestName.getInstance().findClosestCmdMatch(cmd) + "\"?");
            }
        }

        // return the reconstituted string
        return String.join(SEPARATOR, res);
    }

    /**
     * Get the help message of all the commands.
     * @return The help message of all the commands.
     */
    public String getHelp() {
        // create the help
        ArrayList<String> l = new ArrayList<>();
        l.add(getHelpHelp());
        l.add(getHelpQuit());
        l.add(getHelpExit());
        l.add(getHelpAddServer());
        l.add(getHelpGet());
        l.add(getHelpSet());
        l.add(getHelpDecr());
        l.add(getHelpDecrBy());
        l.add(getHelpIncr());
        l.add(getHelpIncrBy());
        l.add(getHelpType());
        l.add(getHelpDel());

        // sort the help by alphabetical order
        l.sort(String::compareTo);

        // return the reconstituted string
        return String.join(SEPARATOR, l);
    }

    /**
     * Get the help message of HELP.
     * @return The help message of HELP.
     */
    public String getHelpHelp() {
        String res = "";
        res += "HELP [cmd1, cmd2, ... cmdN]" + "\n\n"

            +  "DESCRIPTION: Display the help for the given commands. If no command is provided, display " +
               "the help for all the commands.";

        return res;
    }

    /**
     * Get the help message of EXIT.
     * @return The help message of EXIT.
     */
    public String getHelpExit() {
        String res = "";
        res += "EXIT" + "\n\n"

            +  "DESCRIPTION: Terminate the client. This command is the same as QUIT.";

        return res;
    }

    /**
     * Get the help message of QUIT.
     * @return The help message of QUIT.
     */
    public String getHelpQuit() {
        String res = "";
        res += "QUIT" + "\n\n"

            +  "DESCRIPTION: Terminate the client. This command is the same as EXIT.";

        return res;
    }

    /**
     * Get the help message of ADD_SERVER.
     * @return The help message of ADD_SERVER.
     */
    public String getHelpAddServer() {
        String res = "";
        res += "ADD_SERVER server_ip server_name" + "\n\n"

            +  "DESCRIPTION: Connect to the given server. server_ip is the IP of the server (if local, 127.0.0.1). " +
               "server_name is the name of the server on the host (example: server_0)";

        return res;
    }

    /**
     * Get the help message of GET.
     * @return The help message of GET.
     */
    public String getHelpGet() {
        String res = "";
        res += "GET key" + "\n\n"

            +  "DESCRIPTION: Get the value of the specified key. If the key does not exist " +
               "the special value 'null' is returned.";

        return res;
    }

    /**
     * Get the help message of SET.
     * @return The help message of SET.
     */
    public String getHelpSet() {
        String res = "";
        res += "SET key value" + "\n\n"

            +  "DESCRIPTION: Set key to hold the value. If key already holds a value, it is overwritten, " +
               "regardless of its type.";

        return res;
    }

    /**
     * Get the help message of DECR.
     * @return The help message of DECR.
     */
    public String getHelpDecr() {
        String res = "";
        res += "DECR key" + "\n\n"

            +  "DESCRIPTION: Increment or decrement the number stored at key by one. " +
               "If the key does not exist or contains a value of a wrong type, set the key to the value of \"0\" " +
               "before to perform the increment or decrement operation." + "\n\n"

            +  "INCRBY and DECRBY work just like INCR and DECR but instead to increment/decrement by 1 " +
               "the increment/decrement is integer.";

        return res;
    }

    /**
     * Get the help message of DECRBY.
     * @return The help message of DECRBY.
     */
    public String getHelpDecrBy() {
        String res = "";
        res += "DECRBY key integer" + "\n\n"

            +  "DESCRIPTION: Increment or decrement the number stored at key by one. " +
               "If the key does not exist or contains a value of a wrong type, set the key to the value of \"0\" " +
               "before to perform the increment or decrement operation." + "\n\n"

            +  "INCRBY and DECRBY work just like INCR and DECR but instead to increment/decrement by 1 " +
               "the increment/decrement is integer.";

        return res;
    }

    /**
     * Get the help message of INCR.
     * @return The help message of INCR.
     */
    public String getHelpIncr() {
        String res = "";
        res += "INCR key" + "\n\n"

            +  "DESCRIPTION: Increment or decrement the number stored at key by one. " +
               "If the key does not exist or contains a value of a wrong type, set the key to the value of \"0\" " +
               "before to perform the increment or decrement operation." + "\n\n"

            +  "INCRBY and DECRBY work just like INCR and DECR but instead to increment/decrement by 1 " +
               "the increment/decrement is integer.";

        return res;
    }

    /**
     * Get the help message of INCRBY.
     * @return The help message of INCRBY.
     */
    public String getHelpIncrBy() {
        String res = "";
        res += "INCRBY key integer" + "\n\n"

            +  "DESCRIPTION: Increment or decrement the number stored at key by one. " +
               "If the key does not exist or contains a value of a wrong type, set the key to the value of \"0\" " +
               "before to perform the increment or decrement operation." + "\n\n"

            +  "INCRBY and DECRBY work just like INCR and DECR but instead to increment/decrement by 1 " +
               "the increment/decrement is integer.";

        return res;
    }

    /**
     * Get the help message of TYPE.
     * @return The help message of TYPE.
     */
    public String getHelpType() {
        String res = "";
        res += "TYPE key" + "\n\n"

            + "Return the type of the value stored at key in form of a string. The type can be one of \"none\", " +
              "\"string\", \"list\", \"set\". \"none\" is returned if the key does not exist.";

        return res;
    }

    /**
     * Get the help message of DEL.
     * @return The help message of DEL.
     */
    public String getHelpDel() {
        String res = "";
        res += "DEL key" + "\n\n"

            +  "DESCRIPTION: Remove the specified keys. If a given key does not exist no operation is performed " +
               "for this key. The command returns the number of keys removed.";

        return res;
    }
}