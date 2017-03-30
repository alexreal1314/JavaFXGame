package Logic;

import Generated.GameDescriptor;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by alex on 29/11/2016.
 */
public class XMLReader {
    private JAXBContext jaxbContext;
    private Unmarshaller jaxbUnmarshaller;
    private GameDescriptor gameDes;

    public void LoadXML(String xmlSource) throws SAXException, JAXBException, InvalidXMLException, InvalidExtension {
        File file = new File(xmlSource);

        jaxbContext = JAXBContext.newInstance(GameDescriptor.class);
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        gameDes = (GameDescriptor) jaxbUnmarshaller.unmarshal(file);

        String extension = "";
        int i = xmlSource.lastIndexOf('.');
        if (i > 0) {
            extension = xmlSource.substring(i+1);
        }
        if(!extension.equalsIgnoreCase("xml")){
            throw new InvalidExtension("The file needs to be with .xml extension\n");
        }
    }

    public GameDescriptor GetXMLGame() { return gameDes; }
}



