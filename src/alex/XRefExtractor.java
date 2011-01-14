package alex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.xml.sax.SAXException;

import nu.xom.Element;
import nu.xom.Nodes;

public class XRefExtractor extends XomLNRecordProcessor{

	public static void main(String[] args) throws IOException, SAXException {
		File lnSource= new File(args[0]);
		File xrefTarget = new File(args[1]);
		XRefExtractor extractor = new XRefExtractor(lnSource, xrefTarget);
		extractor.process();
	}
	
	private BufferedWriter writer;
	protected XRefExtractor(File inputFile, File outputFile) throws IOException {
		super(inputFile);
		writer = new BufferedWriter(new FileWriter(outputFile), 100000);
	}

	@Override
	void doProcessDocumentEntry(Element element) {
		Nodes links = element.query("//doclink");
		//TODO: use XPATH to find parent element, then document, then document's ID (name if we know the field)
		
	}

	@Override
	void doStartProcessing() throws IOException {		
	}

	@Override
	void doFinishProcessing() throws IOException {
		writer.close();
	}
	
}
