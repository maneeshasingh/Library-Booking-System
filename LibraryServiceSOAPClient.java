package au.edu.swin.waa;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Scanner;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import au.edu.swin.waa.LibraryServiceSOAPStub.ValidateStudentDetails;
import au.edu.swin.waa.LibraryServiceSOAPStub.ValidateStudentDetailsResponse;

 public class LibraryServiceSOAPClient
  {
	public static void main(String[] args) throws RemoteException, LibraryServiceSOAPExceptionException
	 {
	    String epr = "http://localhost:9763/services/LibraryServiceREST.LibraryServiceRESTHttpEndpoint/titles";
		EndpointReference targetEPR = new EndpointReference(epr);
                Options options = new Options();
		options.setTo(targetEPR);
		options.setProperty(Constants.Configuration.MESSAGE_TYPE, "text/xml");
		options.setProperty(Constants.Configuration.HTTP_METHOD, Constants.Configuration.HTTP_METHOD_POST);
		options.setProperty(Constants.Configuration.ENABLE_REST, Constants.VALUE_TRUE);

		ServiceClient sender = new ServiceClient();
		sender.setOptions(options);

		OMElement response = sender.sendReceive(createRequestPayload());
	        processResponsePayload(response);
	 }
        //method to create payload function
	private static OMElement createRequestPayload() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://waa.swin.edu.au", "ns");
        OMElement method = fac.createOMElement("getBook", omNs);
        OMElement postCode = fac.createOMElement("ID", omNs);
        postCode.addChild(fac.createOMText(postCode, "12"));
        method.addChild(postCode);

        return method;
        }
	//method to process the payload function
	private static void processResponsePayload(OMElement response) {
		Iterator iterator = response.getChildrenWithLocalName("return");
		OMElement returnElement = (OMElement)iterator.next();
		System.out.println(returnElement.getText());
	}
			
	}
