package client.requests;

import client.requests.client.RequestClientTestSuite;
import client.requests.dataStructures.list.RequestDataStructuresTestSuite;
import client.requests.dataTypes.RequestDataTypesTestSuite;
import client.requests.exceptions.ClientExceptionsTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RequestTest.class,
        RequestWithKeyTest.class,
        RequestClientTestSuite.class,
        RequestDataTypesTestSuite.class,
        RequestDataStructuresTestSuite.class,
        ClientExceptionsTestSuite.class,
        RequestNameTest.class
})
public class RequestTestSuite {}
