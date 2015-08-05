package au.edu.swin.waa;



import java.io.File;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;


public class LibraryServiceSOAP
{
	String result="";
	//Function to validate student details(id,pin) and return appropriate message on execution
   public String validateStudentDetails(int id,int pin) throws Exception 
	   {
	      
	      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	      DocumentBuilder builder = factory.newDocumentBuilder();
	      Document doc = builder.parse("student.xml");
	      XPathFactory xPathfactory = XPathFactory.newInstance();
	      XPath xpath = xPathfactory.newXPath();
	      String expression = "/students/student[@ID="+id+"]|/students/student[PIN="+pin+"]/Fullname";
	         
	        NodeList nodeList = (NodeList) xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);
	          if(null != nodeList) {
	         
	           for (int i = 0; i < nodeList.getLength(); i++) 
	              {
	        	         result= "Log in Successful";
	                     
	              }
	              }
	              else
	              {
	            	  result="You have entered wrong deatils,Try again";}
	                  return result;
	           }
}





