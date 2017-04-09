package client.requests.dataTypes;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestIncrTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestIncr r;
    private int legitNbTokens = 2;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestIncr(tokens);
    }

    @Test
    public void reqIncrNotEnoughToken() throws Exception {
        thrown.expect(Exception.class);
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqIncrLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqIncrTooManyToken() throws Exception {
        thrown.expect(Exception.class);
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqIncrKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqIncrToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("incr(\"token1\")", r.toString());
    }
}