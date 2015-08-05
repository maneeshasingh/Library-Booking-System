
package au.edu.swin.waa;

import java.io.File;


import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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

public class LibraryServiceREST 
{
	//Arraylists declaration 
ArrayList<String> student=new ArrayList<String>();
ArrayList<String> stu=new ArrayList<String>();
ArrayList<String> lib=new ArrayList<String>();
ArrayList<String> bookinfo=new ArrayList<String>();
ArrayList<String> record=new ArrayList<String>();
String result;

//Function to get all the books details of bookstore.xml file
//return arraylist of file data

public  ArrayList<String> getBook() throws Exception
{
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  DocumentBuilder docBuilder = factory.newDocumentBuilder();
  File file = new File("bookstore.xml");
  Document doc = docBuilder.parse(file);
  // The wild card * matches all tags
  NodeList list = doc.getElementsByTagName("*");
  int bookCount = 0;
  for (int i = 0; i < list.getLength(); i++) {
  // Get the elements book which has id,isbn,title,author,publisher,status
  Element element = (Element)list.item(i);
  String nodeName = element.getNodeName();
 if(nodeName.equals("book")){
      bookCount++;
      System.out.println("BOOK " + bookCount);
      String id = element.getAttribute("ID");
     lib.add(id);
   }  else if (nodeName.equals("ISBN")) {
        String isbn ="\tISBN:\t"+ element.getChildNodes().item(0).getNodeValue();
        lib.add(isbn);
	         } 
       else if (nodeName.equals("title")) {
          String title="\tTitle:\t"+ element.getChildNodes().item(0).getNodeValue();
          lib.add(title);
       } 
       else if (nodeName.equals("author")) {
         String author="\tAuthor:\t" + element.getChildNodes().item(0).getNodeValue();
          lib.add(author);
       }
       else if (nodeName.equals("publisher")) {
	           String publisher="\tPublisher:\t" 
	                  + element.getChildNodes().item(0).getNodeValue();
	                   lib.add(publisher);
	         }
       else if (nodeName.equals("publisheddate")) {
	        String  pdate="\tPublished Date:\t" 
	                  + element.getChildNodes().item(0).getNodeValue();
	                   lib.add(pdate);
	         }
       else if (nodeName.equals("status")) {
	        String status="\tStatus:\t" + element.getChildNodes().item(0).getNodeValue();
	                   lib.add(status);
	         }
    } return lib;
	
 }

//Function to get details of student by entering ID
//Return Arraylist of student data 
public ArrayList<String> getStudentDetails(int id) throws Exception 
{
 String output="";
 try 
 {
   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
   DocumentBuilder builder = factory.newDocumentBuilder();
   Document doc = builder.parse("student.xml");
   XPathFactory xPathfactory = XPathFactory.newInstance();
   XPath xpath = xPathfactory.newXPath();
   String expression = "/students/student[@ID="+id+"]";
   Node node = (Node) xpath.compile(expression).evaluate(doc, XPathConstants.NODE);
       if(null != node) {
          NodeList nodeList = node.getChildNodes();
           for (int i = 0;null!=nodeList && i < nodeList.getLength(); i++) {
               Node nod = nodeList.item(i);
               if(nod.getNodeType() == Node.ELEMENT_NODE)
                  output= nodeList.item(i).getNodeName() + " : " + nod.getFirstChild().getNodeValue(); 
                  student.add(output);
                  System.out.println("@@@@"+output);
                }
                }
       }
catch (IOException e) {
       e.printStackTrace();
   } catch (ParserConfigurationException e) {
       e.printStackTrace();
   } catch (XPathExpressionException e) {
       e.printStackTrace();
   }
   return student;
}

//Function to add new book in bookstore file
//return message as book get added
public String addBook(String id,String isbn,String titles,String authors,String publish,String date,String state) throws Exception
{
 DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    Document document = documentBuilder.parse("bookstore.xml");
    Element root = document.getDocumentElement();
    // Root Element
    Element rootElement = document.getDocumentElement();
        Element server = document.createElement("book");
        rootElement.appendChild(server);
        server.setAttribute("ID",id);
         Element name = document.createElement("ISBN");
        name.appendChild(document.createTextNode(isbn));
        server.appendChild(name);
        Element title = document.createElement("title");
        title.appendChild(document.createTextNode(titles));
        server.appendChild(title);
        Element author = document.createElement("author");
       author.appendChild(document.createTextNode(authors));
        server.appendChild(author);
        Element publisher = document.createElement("publisher");
      publisher.appendChild(document.createTextNode(publish));
        server.appendChild(publisher);
         Element publisheddate = document.createElement("publisheddate");
    publisheddate.appendChild(document.createTextNode(date));
        server.appendChild(publisheddate);
        Element status= document.createElement("status");
    status.appendChild(document.createTextNode(state));
        server.appendChild(status);
        root.appendChild(server);
     DOMSource source = new DOMSource(document);
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    StreamResult result = new StreamResult("bookstore.xml");
    transformer.transform(source, result);
    return "The book has been added";
}
//Function to update the details of book through book ID
//Return String message as book get updated
public String updateBook(int id,String isbn,String title,String author,String publisher,String pdate,String status) throws Exception
{
	String s="";
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  DocumentBuilder builder = factory.newDocumentBuilder();
  Document doc = builder.parse("bookstore.xml");
  XPathFactory xpf = XPathFactory.newInstance();
  XPath xpath = xpf.newXPath();

  XPathExpression expr = xpath.compile("bookstore/book[@ID=" + id + "]");
  Node nodeGettingChanged = (Node) expr.evaluate(doc, XPathConstants.NODE);

 NodeList childNodes = nodeGettingChanged.getChildNodes();
for (int i = 0; i != childNodes.getLength(); ++i)
{
  Node child = childNodes.item(i);
  if (!(child instanceof Element))
      continue;

  if (child.getNodeName().equals("ISBN"))
      child.getFirstChild().setNodeValue(isbn);
      else if (child.getNodeName().equals("title"))
      child.getFirstChild().setNodeValue(title) ;
      else if (child.getNodeName().equals("author"))
      child.getFirstChild().setNodeValue(author) ;
      else if (child.getNodeName().equals("publisher"))
      child.getFirstChild().setNodeValue(publisher) ;
      else if (child.getNodeName().equals("publisheddate"))
      child.getFirstChild().setNodeValue(pdate) ;
      else if (child.getNodeName().equals("status"))
      child.getFirstChild().setNodeValue(status);
 
}
 TransformerFactory transformerFactory = TransformerFactory.newInstance();
 Transformer transformer = transformerFactory.newTransformer();
 DOMSource source = new DOMSource(doc);
 StreamResult result = new StreamResult("bookstore.xml");
 transformer.transform(source, result);
 s= "Book records has been updated";
return s;
}
//Function to get book details from book ID
//Return Arraylist of book data
public ArrayList<String> getBookDetail(int id) throws Exception 
{
 
  try 
     {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
   DocumentBuilder builder = factory.newDocumentBuilder();
   Document doc = builder.parse("bookstore.xml");
   XPathFactory xPathfactory = XPathFactory.newInstance();
   XPath xpath = xPathfactory.newXPath();
   String expression = "/bookstore/book[@ID="+id+"]";
   Node node = (Node) xpath.compile(expression).evaluate(doc, XPathConstants.NODE);
       if(null != node) {
          NodeList nodeList = node.getChildNodes();
           for (int i = 0;null!=nodeList && i < nodeList.getLength(); i++) {
               Node nod = nodeList.item(i);
               if(nod.getNodeType() == Node.ELEMENT_NODE)
                  result= nodeList.item(i).getNodeName() + " : " + nod.getFirstChild().getNodeValue(); 
                  bookinfo.add(result); }
                  }
       }
   catch (IOException e) {
       e.printStackTrace();
   } catch (ParserConfigurationException e) {
       e.printStackTrace();
   } catch (XPathExpressionException e) {
       e.printStackTrace();
   }
   return bookinfo;
}
//Function to update the student details through student ID
//return string message after student file updated
public String updateStudent(int id,String name,String pin) throws Exception
{
	String output="";
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  DocumentBuilder builder = factory.newDocumentBuilder();
  Document doc = builder.parse("student.xml");
  XPathFactory xpf = XPathFactory.newInstance();
  XPath xpath = xpf.newXPath();

  XPathExpression expr = xpath.compile("students/student[@ID=" + id + "]");
  Node nodeGettingChanged = (Node) expr.evaluate(doc, XPathConstants.NODE);

 NodeList childNodes = nodeGettingChanged.getChildNodes();
for (int i = 0; i != childNodes.getLength(); ++i)
{
  Node child = childNodes.item(i);
  if (!(child instanceof Element))
      continue;

  if (child.getNodeName().equals("Fullname"))
      child.getFirstChild().setNodeValue(name) ;
  else if (child.getNodeName().equals("PIN"))
      child.getFirstChild().setNodeValue(pin) ;
 
}
 TransformerFactory transformerFactory = TransformerFactory.newInstance();
 Transformer transformer = transformerFactory.newTransformer();
 DOMSource source = new DOMSource(doc);

 StreamResult result = new StreamResult("student.xml");
 transformer.transform(source, result);
 output= "student records updated";
 return output;
}
//Function to validate the book through book ID
//Return message
public String validateBook(String id) throws Exception
{
     String state="";
String status="on loan";
String newstate="available";
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
DocumentBuilder builder = factory.newDocumentBuilder();
Document doc = builder.parse("bookstore.xml");
XPathFactory xPathfactory = XPathFactory.newInstance();
XPath xpath = xPathfactory.newXPath();
String expression = "/bookstore/book[@ID="+id+"]/status";
XPath path = xPathfactory.newXPath();
NodeList nodeList = (NodeList) path.compile(expression).evaluate(doc, XPathConstants.NODESET);
if(null!= nodeList) 
{  for(int i = 0; i < nodeList.getLength(); i++) 
        { 
           state=nodeList.item(i).getFirstChild().getNodeValue();
           if(state.equals(newstate))
          {
        	   //updateBookDetails(id);
        	   return "Book is available";
           }
           else{return "Book is not available";}
       }}
           return "Book is available";  
}
   
//Function to update the book status
//Return message
public String updateBookDetails(String id) throws Exception{
   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
DocumentBuilder builder = factory.newDocumentBuilder();
Document doc = builder.parse("bookstore.xml");
XPath p = XPathFactory.newInstance().newXPath();
        NodeList nodes = (NodeList)p.evaluate("/bookstore/book[@ID="+id+"]/status", doc, XPathConstants.NODESET);
       
//make the change
for (int idx = 0; idx < nodes.getLength(); idx++) {
nodes.item(idx).setTextContent("on loan");
// save the result
Transformer xformer = TransformerFactory.newInstance().newTransformer();
xformer.transform(new DOMSource(doc), new StreamResult(new File("bookstore.xml")));
//updateStudentDetails(id);

  
}return "book details updated";
}
//Function to update the student details in studentrecords.xml file
//Return message
public String updateStudentDetails(String id) throws Exception{
 String  Borrowedbook="";
 String  Requestedbook="";
 DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
Document document = documentBuilder.parse("StudentBookRecord.xml");
Element root = document.getDocumentElement();
XPathFactory xPathFactory = XPathFactory.newInstance();
XPath Path = xPathFactory.newXPath();
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
DocumentBuilder builder = factory.newDocumentBuilder();
Document doc = builder.parse("bookstore.xml");
 XPath p = XPathFactory.newInstance().newXPath();
        
NodeList titleNodeList = (NodeList)Path.evaluate("/bookstore/book[@ID="+id+"]/title", doc, XPathConstants.NODESET);

for(int k = 0; k < titleNodeList.getLength(); k++) 
{  Borrowedbook=titleNodeList.item(k).getFirstChild().getNodeValue();}
// Root Element
Element rootElement = document.getDocumentElement();
// book elements
Element student = document.createElement("student");
rootElement.appendChild(student);
Element Id = document.createElement("ID");

Id.appendChild(document.createTextNode(id));
student.appendChild(Id);
Element book = document.createElement("Borrowedbook");
book.appendChild(document.createTextNode(Borrowedbook));
student.appendChild(book);
Element requestedbook = document.createElement("Requestedbook");
book.appendChild(document.createTextNode(Requestedbook));
student.appendChild(requestedbook);

root.appendChild(student);
DOMSource source = new DOMSource(document);
TransformerFactory transformerFactory = TransformerFactory.newInstance();
Transformer transformer = transformerFactory.newTransformer();
StreamResult result = new StreamResult("StudentBookRecord.xml");
transformer.transform(source, result);
return "The book has been borrowed";
}
//Function to get all the Student borrowing details of studentbookrecord.xml file
//return arraylist of file data

public  ArrayList<String> getStudentRecord() throws Exception
{
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
DocumentBuilder docBuilder = factory.newDocumentBuilder();
File file = new File("StudentBookRecord.xml");
Document doc = docBuilder.parse(file);
// The wild card * matches all tags
NodeList list = doc.getElementsByTagName("*");
int bookCount = 0;
for (int i = 0; i < list.getLength(); i++) {
// Get the elements id.borrowed,requested books
Element element = (Element)list.item(i);
String nodeName = element.getNodeName();
if(nodeName.equals("student")){
    bookCount++;
    System.out.println("Student " + bookCount);
    }  
else if (nodeName.equals("ID")) {
    String id ="\t Student ID:\t"+ element.getChildNodes().item(0).getNodeValue();
    record.add(id);
	         } else if (nodeName.equals("Borrowedbook")) {
      String book ="\tBorrowed Book:\t"+ element.getChildNodes().item(0).getNodeValue();
      record.add(book);
	         } 
     else if (nodeName.equals("Requestedbook")) {
        String title="\tRequested Book:\t"+ element.getChildNodes().item(0).getNodeValue();
        record.add(title);
     } 
     
  } return record;
	
}

}