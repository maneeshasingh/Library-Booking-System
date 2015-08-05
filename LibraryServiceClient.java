package au.edu.swin.waa;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;

import au.edu.swin.waa.LibraryServiceSOAPStub.ValidateStudentDetails;
import au.edu.swin.waa.LibraryServiceSOAPStub.ValidateStudentDetailsResponse;

public class LibraryServiceClient 
   {
	public static void main(String[] args) throws AxisFault, LibraryServiceSOAPExceptionException 
	{  
		Scanner in = new Scanner(System.in); 
	     System.out.println("WELCOME TO Unilib....  ");
	     System.out.println("Please enter 1 to view all books");
	     System.out.println("Please enter 2 to borrow a book");
	     System.out.println("Please enter 3 to view Student records");
	       int option = in.nextInt();
	       if (option==1){
	    	   viewBooks();  
	       }
	       if (option==2){
	    	   borrowBook();
	    	   System.out.println("Enter Book ID:  ");
	          String id = in.next();
	    	   Validate(id);
	    	   updateBookDetails(id);
	    	   updateStudentDetails(id);
	    	   
	       }
	       if(option==3){
	    	   getStudentRecord();}
	      
	       }
   
	 //Function to retrieve books from bookstore database 
	public static void viewBooks() throws LibraryServiceSOAPExceptionException, AxisFault
	{
	String epr = "http://localhost:9763/services/LibraryServiceREST.LibraryServiceRESTHttpEndpoint/titles";
	EndpointReference targetEPR = new EndpointReference(epr);

	Options options = new Options();
	options.setTo(targetEPR);
	options.setProperty(Constants.Configuration.MESSAGE_TYPE, "text/xml");
	options.setProperty(Constants.Configuration.HTTP_METHOD, Constants.Configuration.HTTP_METHOD_GET);
	options.setProperty(Constants.Configuration.ENABLE_REST, Constants.VALUE_TRUE);

	ServiceClient sender = new ServiceClient();
	sender.setOptions(options);

	OMElement response = sender.sendReceive( viewBookRequestPayload());
	viewBookResponsePayload(response);
	       }
	// }
private static OMElement viewBookRequestPayload() {
    OMFactory fac = OMAbstractFactory.getOMFactory();
    OMNamespace omNs = fac.createOMNamespace("http://waa.swin.edu.au", "ns");
    OMElement method = fac.createOMElement("getBook", omNs);
   return method;
}
private static void viewBookResponsePayload(OMElement response) {
	Iterator iterator = response.getChildrenWithLocalName("return");
	while(iterator.hasNext()){
		OMElement returnElement = (OMElement)iterator.next();
	System.out.println(returnElement.getText());}
}
//function to borrow a book
public static void borrowBook() throws LibraryServiceSOAPExceptionException, AxisFault
{  String id;
	try
	{
	LibraryServiceSOAPStub stub = new LibraryServiceSOAPStub();
  // Check student details
   ValidateStudentDetails validateStudentDetails = new ValidateStudentDetails();
   Scanner in = new Scanner(System.in); 
   System.out.println("Enter Student ID:  ");
     int studentid = in.nextInt();
     System.out.println("Enter PIN:  ");
     int pin = in.nextInt();
    validateStudentDetails.setId(studentid);
    validateStudentDetails.setPin(pin);
    ValidateStudentDetailsResponse validateStudentDetailsResponse = stub.validateStudentDetails( validateStudentDetails);
     System.out.println(validateStudentDetailsResponse.get_return());
	}
	catch (AxisFault e) {
	  e.printStackTrace();
 } catch (RemoteException e) {
	  e.printStackTrace();}
 }

//Function to validate the book detail such as id and status.
	public static void Validate(String id)throws AxisFault
{
	 String epr2 = "http://localhost:9763/services/LibraryServiceREST.LibraryServiceRESTHttpEndpoint/validateBook";
     
     EndpointReference targetEPR2 = new EndpointReference(epr2);

		Options options2 = new Options();
		options2.setTo(targetEPR2);
		options2.setProperty(Constants.Configuration.MESSAGE_TYPE, "text/x-www-form-urlencoded");
		options2.setProperty(Constants.Configuration.HTTP_METHOD, Constants.Configuration.HTTP_METHOD_GET);
		options2.setProperty(Constants.Configuration.ENABLE_REST, Constants.VALUE_TRUE);

		ServiceClient sender = new ServiceClient();
		sender.setOptions(options2);

		OMElement response = sender.sendReceive(validateRequestPayload(id));
		validateResponsePayload(response);}
	   
	//payload
     private static OMElement validateRequestPayload(String id) {
 		
         OMFactory fac = OMAbstractFactory.getOMFactory();
         OMNamespace omNs = fac.createOMNamespace("http://waa.swin.edu.au", "ns");
         OMElement method = fac.createOMElement("validateBook", omNs);
         OMElement bookId = fac.createOMElement("id", omNs);
	     bookId.addChild(fac.createOMText(bookId, id));
	     method.addChild(bookId);
	     return method;
     }
   //payload response
    private static void validateResponsePayload(OMElement response) {
 	Iterator iterator = response.getChildrenWithLocalName("return");
 	OMElement returnElement = (OMElement)iterator.next();
 	System.out.println(returnElement.getText());
 }
    
    //function to update the status of book as the book get borrowed
    public static void updateBookDetails(String id)throws AxisFault
   	{
   		String epr = "http://localhost:9763/services/LibraryServiceREST.LibraryServiceRESTHttpEndpoint/updateBookDetails";
   		EndpointReference targetEPR = new EndpointReference(epr);

   		Options options = new Options();
   		options.setTo(targetEPR);
   		options.setProperty(Constants.Configuration.MESSAGE_TYPE, "text/xml");
   		options.setProperty(Constants.Configuration.HTTP_METHOD, Constants.Configuration.HTTP_METHOD_PUT);
   		options.setProperty(Constants.Configuration.ENABLE_REST, Constants.VALUE_TRUE);

   		ServiceClient sender = new ServiceClient();
   		sender.setOptions(options);
   		
   		OMElement response = sender.sendReceive(updateBookPayload(id));
   		updateBookProcessResponsePayload(response);
   	}
       //payload method
   	private static OMElement updateBookPayload(String id) {
   	      OMFactory fac = OMAbstractFactory.getOMFactory();
   	      OMNamespace omNs = fac.createOMNamespace("http://waa.swin.edu.au", "ns");
   	      
   	      OMElement method = fac.createOMElement("updateBookDetails", omNs);
   	      
   	      OMElement bookId = fac.createOMElement("id", omNs);
   	      bookId.addChild(fac.createOMText(bookId, id));
   	      method.addChild(bookId);
   	      return method;
   	  }
       //payload response method
   	private static void updateBookProcessResponsePayload(OMElement response) {
   		Iterator iterator = response.getChildrenWithLocalName("return");
   		OMElement returnElement = (OMElement)iterator.next();
   		System.out.println(returnElement.getText());
   	}
       
  //function to update the details of student as the book get borrowed
	 public static void updateStudentDetails(String id)throws AxisFault
		{
			String epr = "http://localhost:9763/services/LibraryServiceREST.LibraryServiceRESTHttpEndpoint/updateStudentDetails";
			EndpointReference targetEPR = new EndpointReference(epr);

			Options options = new Options();
			options.setTo(targetEPR);
			options.setProperty(Constants.Configuration.MESSAGE_TYPE, "text/xml");
			options.setProperty(Constants.Configuration.HTTP_METHOD, Constants.Configuration.HTTP_METHOD_PUT);
			options.setProperty(Constants.Configuration.ENABLE_REST, Constants.VALUE_TRUE);

			ServiceClient sender = new ServiceClient();
			sender.setOptions(options);
			
			OMElement response = sender.sendReceive(updateStudentPayload(id));
			updateStudentResponsePayload(response);
		}
	    //payload method
		private static OMElement updateStudentPayload(String id) {
		      OMFactory fac = OMAbstractFactory.getOMFactory();
		      OMNamespace omNs = fac.createOMNamespace("http://waa.swin.edu.au", "ns");
		      
		      OMElement method = fac.createOMElement("updateStudentDetails", omNs);
		      
		      OMElement bookId = fac.createOMElement("id", omNs);
		      bookId.addChild(fac.createOMText(bookId, id));
		      method.addChild(bookId);
		      return method;
		  }
	    //payload response method
		private static void updateStudentResponsePayload(OMElement response) {
			Iterator iterator = response.getChildrenWithLocalName("return");
			OMElement returnElement = (OMElement)iterator.next();
			System.out.println(returnElement.getText());
		}
		//function to get records of students from student record database.
		public static void getStudentRecord() throws LibraryServiceSOAPExceptionException, AxisFault
		{
		String epr = "http://localhost:9763/services/LibraryServiceREST.LibraryServiceRESTHttpEndpoint/record";
		EndpointReference targetEPR = new EndpointReference(epr);

		Options options = new Options();
		options.setTo(targetEPR);
		options.setProperty(Constants.Configuration.MESSAGE_TYPE, "text/xml");
		options.setProperty(Constants.Configuration.HTTP_METHOD, Constants.Configuration.HTTP_METHOD_GET);
		options.setProperty(Constants.Configuration.ENABLE_REST, Constants.VALUE_TRUE);
        ServiceClient sender = new ServiceClient();
		sender.setOptions(options);

		OMElement response = sender.sendReceive( viewStudentRecordPayload());
		viewStudentRecordResponsePayload(response);
	     }
		// }
	private static OMElement viewStudentRecordPayload() {
	    OMFactory fac = OMAbstractFactory.getOMFactory();
	    OMNamespace omNs = fac.createOMNamespace("http://waa.swin.edu.au", "ns");
	    OMElement method = fac.createOMElement("getStudentRecord", omNs);
	   return method;
	}
	private static void viewStudentRecordResponsePayload(OMElement response) {
		Iterator iterator = response.getChildrenWithLocalName("return");
		while(iterator.hasNext()){
			OMElement returnElement = (OMElement)iterator.next();
		System.out.println(returnElement.getText());}
	}
	
	
	    
 }
 			
		
	