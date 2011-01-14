package alex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;
import nu.xom.NodeFactory;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.Text;

public abstract class XomLNRecordProcessor extends NodeFactory
{

    private File inputFile;

    protected XomLNRecordProcessor(File inputFile)
    {
        this.inputFile = inputFile;
    }

    public void process() throws SAXException, IOException
    {
        doStartProcessing();
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), Charset.forName("UTF-8")), 1000000);
            XMLReader xmlReader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
            xmlReader.setFeature("http://xml.org/sax/features/validation", false);
            xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            Builder parser = new Builder(xmlReader, false, this);
            Document document = parser.build(reader); //ignore document

            reader.close();
        } catch (IOException ex)
        {
            System.out.println("Due to an IOException, the parser could not process: " + inputFile);
            ex.printStackTrace();
        } catch (ParsingException ex)
        {
            System.out.println(ex);
            ex.printStackTrace();
        }
        doFinishProcessing();
    }

    static final Nodes EMPTY_NODES = new Nodes();

    @Override
    public Nodes finishMakingElement(Element element)
    {
        element.setNamespaceURI(null);
        String localName = element.getLocalName();

        if ("document".equals(localName))
        {
            doProcessDocumentEntry(element);
            return EMPTY_NODES;
        } else
        {
            return super.finishMakingElement(element);
        }
    }

    @Override
    public Element makeRootElement(String name, String namespace)
    {
        return super.makeRootElement(name, null);
    }

    abstract void doStartProcessing() throws IOException;
    abstract void doProcessDocumentEntry(Element element);
    abstract void doFinishProcessing() throws IOException;
}
