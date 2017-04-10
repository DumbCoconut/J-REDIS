package client;

import client.requests.Request;
import client.requests.RequestName;
import client.requests.client.RequestAddServer;
import client.requests.client.RequestHelp;
import client.requests.dataTypes.*;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;
import server.RedisLikeServer;
import server.Server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    private RedisLikeServer server;
    private Registry registry;

    private boolean exitRequested;
    private ArrayList<String> tokens;

    public static void main(String[] args) {
        Client c = new Client();
    }

    public Client() {
        enterLoop();
    }

    public void enterLoop() {
        Scanner terminal = new Scanner(System.in);
        exitRequested = false;
        while (!exitRequested) {
            System.out.print("> ");
            String cmd = terminal.nextLine();
            tokens = splitIntoTokens(cmd);
            parseAndexecuteCmd();
        }
    }

    public void parseAndexecuteCmd() {
        /* early exit */
        if (tokens.isEmpty()) {
            return;
        }

        String cmd = tokens.get(0).toLowerCase();

        if (cmd.equals(RequestName.getInstance().getHelpCmd())) {
            doHelp();
        } else if (cmd.equals(RequestName.getInstance().getQuitCmd()) || cmd.equals(RequestName.getInstance().getExitCmd())) {
            doExit();
        } else if (cmd.equals(RequestName.getInstance().getAddServerCmd())) {
            doAddServer();
        } else if (cmd.equals(RequestName.getInstance().getGetCmd())) {
            doGet();
        } else if (cmd.equals(RequestName.getInstance().getSetCmd())) {
            doSet();
        } else if (cmd.equals(RequestName.getInstance().getTypeCmd())) {
            doType();
        } else if (cmd.equals(RequestName.getInstance().getDecrCmd())) {
            doDecr();
        } else if (cmd.equals(RequestName.getInstance().getDecrByCmd())) {
            doDecrBy();
        } else if (cmd.equals(RequestName.getInstance().getIncrCmd())) {
            doIncr();
        } else if (cmd.equals(RequestName.getInstance().getIncrByCmd())) {
            doIncrBy();
        } else if (cmd.equals(RequestName.getInstance().getDelCmd())) {
            doDel();
        } else {
            doUndefinedCmd(cmd);
        }
    }

    /**
     * Split a string into tokens. Each word is a token, except if in quotes.
     * <p>
     *     Example: "Hello my name is" will be split into [hello, my, name, is] but
     *     "Hello \"my name is\"" will be split into [hello, my name is].
     *     Note that delimiters like "," are considered part of the word if they are
     *     not separated from it by a space.
     * </p>
     * @param s The string to parse.
     * @return ArrayList containing the tokens.
     */
    public ArrayList<String> splitIntoTokens(String s) {
        ArrayList<String> list = new ArrayList<>();
        /*
         * [^"]     - token starting with something other than "
         * \S*      - followed by zero or more non-space characters
         * ...or...
         * ".+?"    - a "-symbol followed by whatever, until another "
         */
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(s);
        while (m.find()) {
            list.add(m.group(1).replace("\"", ""));
        }
        return list;
    }

    private void doHelp() {
        try {
            RequestHelp r = new RequestHelp(tokens);
            System.out.println(r.getMessage());
        } catch (NoTokensException e) {
            System.out.println(e.getMessage());
        }
    }

    private void doExit() {
        exitRequested = true;
        System.out.println("Bye!");
    }

    private void doAddServer() {
        try {
            RequestAddServer r = new RequestAddServer(tokens);
            addServer(r.getIp(), r.getName());
        } catch (InvalidNbArgException | NoTokensException e) {
            System.out.println(e.getMessage());
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void doGet() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestGet r = new RequestGet(tokens);
                System.out.println(get(r.getKey()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doSet() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestSet r = new RequestSet(tokens);
                set(r.getKey(), r.getObject());
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doType() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestType r = new RequestType(tokens);
                System.out.println(type(r.getKey()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doDecr() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestDecr r = new RequestDecr(tokens);
                System.out.println(decr(r.getKey()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doDecrBy() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestDecrBy r = new RequestDecrBy(tokens);
                System.out.println(decrBy(r.getKey(), Integer.valueOf(r.getInteger())));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doIncr() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestIncr r = new RequestIncr(tokens);
                System.out.println(incr(r.getKey()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doIncrBy() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestIncrBy r = new RequestIncrBy(tokens);
                System.out.println(incrBy(r.getKey(), Integer.valueOf(r.getInteger())));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doDel() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestDel r = new RequestDel(tokens);
                System.out.println(del(r.getKey()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doUndefinedCmd(String cmd) {
        System.out.println("(error) I'm sorry, I don't recognize that command. "
                + "Did you mean \"" + RequestName.getInstance().findClosestCmdMatch(cmd) + "\"?");
    }

    private boolean isServerSet() {
        return !(server == null);
    }

    private void addServer(String ip, String name) throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(ip);
        server = (RedisLikeServer) registry.lookup(name);
    }

    private void printServerNotSet() {
        System.out.println("Server is not set. Please add a server. Type \"help add_server\" if you need help.");
    }

    /**
     * Test if the specified key exists.
     * @param key The key to test.
     * @return True if the key exists, false otherwise.
     */
    private boolean exists(String key) {
        // TODO
        return false;
    }

    /**
     * Get the value of the specified key.
     * @param key The key we want the value of.
     * @return The value of the key if it exists, null otherwise.
     */
    private Object get(String key) {
        try {
            return server.get(key);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Set key to hold the string value. If key already holds a value, it is overwritten, regardless of its type.
     * @param key The key holding the value.
     * @param value The value to set.
     */
    private void set(String key, Object value) {
        try {
            server.set(key, value);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the type of the value stored at key in form of a string.
     * @param key The key holding the value.
     * @return one of "none", "string", "int", "list". "none" is returned if the key does not exist.
     */
    private String type(String key) {
        try {
            return server.type(key);
        } catch (RemoteException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Decrement the number stored at key by one.
     * <p>
     *     If the key does not exist or contains a value of a wrong type, set the key to the value of "0"
     *     before to perform the increment or decrement operation.
     * </p>
     * @param key The key holding the value.
     * @return the new value of key after the decrement.
     */
    private int decr(String key) {
        try {
            return server.decr(key);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Decrement the number stored at key by one.
     * <p>
     *     If the key does not exist or contains a value of a wrong type, set the key to the value of "0"
     *     before to perform the increment or decrement operation.
     * </p>
     * @param key The key holding the value.
     * @param integer The increment value.
     * @return the new value of key after the decrement.
     */
    private int decrBy(String key, int integer) {
        try {
            return server.decrBy(key, integer);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Increment the number stored at key by one.
     * <p>
     *     If the key does not exist or contains a value of a wrong type, set the key to the value of "0"
     *     before to perform the increment or decrement operation.
     * </p>
     * @param key The key holding the value.
     * @return the new value of key after the Increment.
     */
    private int incr(String key) {
        try {
            return server.incr(key);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Increment the number stored at key by one.
     * <p>
     *     If the key does not exist or contains a value of a wrong type, set the key to the value of "0"
     *     before to perform the increment or decrement operation.
     * </p>
     * @param key The key holding the value.
     * @param integer The increment value.
     * @return the new value of key after the Increment.
     */
    private int incrBy(String key, int integer) {
        try {
            return server.incrBy(key, integer);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Remove the specified key. If a given key does not exist no operation is performed for this key.
     * @param key The key to remove.
     * @return True if the key existed and has been removed, false otherwise.
     */
    private boolean del(String key) {
        try {
            return server.del(key);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }
}