package client.requests.dataTypes;

import client.requests.RequestWithKey;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public class RequestIncr extends RequestWithKey {
    private final int minNbArgs = 1;

    public RequestIncr(ArrayList<String> tokens) throws InvalidNbArgException, NoTokensException {
        super(tokens);
        setNbArgs(minNbArgs);
        parse();
    }

    public void parse() throws InvalidNbArgException {
        if (tokens.size() != nbExpectedTokens()) {
            throw new InvalidNbArgException(tokens.size() - 1, minNbArgs);
        }
    }

    @Override
    public String toString() {
        return "incr(\"" + key + "\")";
    }
}