package client.requests;

import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public abstract class Request {
    protected ArrayList<String> tokens;
    private int nbArgs;

    public Request(ArrayList<String> tokens) throws NoTokensException {
        this.tokens = tokens;
        nbArgs = tokens.size() - 1;
        if (tokens.isEmpty()) {
            throw new NoTokensException();
        }
    }

    public int getNbArgs() {
        return nbArgs;
    }

    protected int nbExpectedTokens() {
        return nbArgs + 1;
    }

    public void setNbArgs(int nbArgs) throws IllegalArgumentException {
        if (nbArgs < 0) {
            throw new IllegalArgumentException("The number of arguments must be >= 0.");
        }
        this.nbArgs = nbArgs;
    }

    @Override
    abstract public String toString();
}