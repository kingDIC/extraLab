import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
/**
 * Course:      Data Structures and Algorithms for Computational Linguistics II SS2021
 * Assignment:  (extra Lab)
 * Author:      (Daniel Capkan)
 * Description: (Text to XML Converter)
 *
 * Honor Code:  I pledge that this program represents my own work.
 *  I received help from:
 *   -
 *  in designing and debugging my program.
 */
/**
 *
 * @author Verena Henrich
 */
public class TextToXmlConverter {
    public static String XML_ELEMENT_CORPUS = "corpus";
    public static String XML_ELEMENT_SENTENCE = "s";
    public static String XML_ELEMENT_WORD = "t";
    public static String XML_ATTRIBUTE_ID = "id";
    public static String XML_ATTRIBUTE_LANGUAGE = "lang";
    public static String XML_ATTRIBUTE_WORD = "word";
    public static void main(String[] args) {
        Document corpus = convertTextToXml("corpus_as_text.txt");
        saveCorpus(corpus, "corpus_as_xml.xml");
    }

    /**
     * This method should read a text file (one sentence per line) and convert it
     * to an XML document.
     * 
     * @param pathToCorpusAsText the input text file that should be converted to XML
     * @return the XML document that has been created from the text file
     */
    public static Document convertTextToXml(String pathToCorpusAsText) {

        try{
            // creating DOM parser and BR to read through .txt data
            BufferedReader in = new BufferedReader(new FileReader(pathToCorpusAsText));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();

            // creating the doc and adding attributes and names
            Document xmlDoc = impl.createDocument(null, XML_ELEMENT_CORPUS, null);
            Element root = xmlDoc.getDocumentElement();
            root.setAttribute(XML_ATTRIBUTE_ID, "corpus-for-selftest");
            root.setAttribute(XML_ATTRIBUTE_LANGUAGE, "de");
            // string for every line in .txt and line counter for attributes
            String str;
            int lineCounter = 1;
            // while loop while implementing value of str
            while ((str = in.readLine()) != null) {

                //tokenizing the string with given method and adding attributes
                String [] elements = tokenize(str);
                Element e0 = xmlDoc.createElement(XML_ELEMENT_SENTENCE);
                e0.setAttribute(XML_ATTRIBUTE_ID, XML_ELEMENT_SENTENCE+lineCounter);

                // for loop iterating through elements in array and creating and adding empty tag elements with attributes
                for (int i =0; i<elements.length; i++){
                    Element e1 = xmlDoc.createElement(XML_ELEMENT_WORD);
                    e1.setAttribute(XML_ATTRIBUTE_ID, XML_ELEMENT_SENTENCE+lineCounter+"_"+(i+1));
                    e1.setAttribute(XML_ATTRIBUTE_WORD, elements[i]);
                    e0.appendChild(e1);


                }
                root.appendChild(e0);
                lineCounter++;
            }
            in.close();
            return xmlDoc;
        }
        //catching exceptions
        catch (Exception e) { e.printStackTrace(); }
        // extra return statement to satisfy static condition
        return null;
    }
    
    /**
     * Converts a sentence into an array of tokens.
     * 
     * @param sentence the sentence to be tokenized
     * @return the tokenized sentence as a string array
     */
    public static String[] tokenize(String sentence) {
        // adds empty spaces before and after each of the following punctuation marks
        // Daniel: quotation marks seem not to work but couldn't fix it
        sentence = sentence.replace(".", " . ");
        sentence = sentence.replace(",", " , ");
        sentence = sentence.replace("?", " ? ");
        sentence = sentence.replace("!", " ! ");
        sentence = sentence.replace("(", " ( ");
        sentence = sentence.replace(")", " ) ");
        sentence = sentence.replace("\"", " \" ");
        sentence = sentence.replace("»", " » ");
        sentence = sentence.replace("«", " « ");
        sentence = sentence.replace(":", " : ");
        sentence = sentence.replace(";", " ; ");
        
        // remove leading and training white space
        sentence = sentence.trim();
        
        // split the sentence into tokens at white-space and return the result
        return sentence.split("\\s+");
    }

    /**
     * Save the given XML document to a file at specified location "pathToSaveXml".
     * 
     * @param document the XML document that should be saved to a file
     * @param pathToSaveXml location where to save XML file to
     */
    public static void saveCorpus(Document document, String pathToSaveXml) {
        try {
            // set up a transformer
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 4);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // create string from xml tree
            StringWriter stringWriter = new StringWriter();
            StreamResult result = new StreamResult(stringWriter);
            DOMSource source = new DOMSource(document);
            transformer.transform(source, result);
            String xmlString = stringWriter.toString();

            // print xml
            FileWriter writer = new FileWriter(pathToSaveXml);
            writer.write(xmlString);
            writer.close();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
