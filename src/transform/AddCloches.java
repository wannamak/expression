/**
 * Tool to add a stop to an Hauptwerk organ xml file.
 */

package transform;

import com.google.common.io.Files;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AddCloches {
  private final Config config;

  public static void main(String[] args) throws Exception {
    new AddCloches(args).run();
  }

  public AddCloches(String args[]) {
    config = new Config(args);
  }

  public void run()
      throws IOException, URISyntaxException, TransformerException, SQLException,
      ClassNotFoundException {
    if (config.showHelp || !config.stylesheet.exists() || !config.inputXml.exists()) {
      System.err.printf("Usage    : AddCloches %s %s\n",
          config.stylesheet.getName(),
          config.inputXml.getName());
      System.err.printf("Generates: %s\n", config.outputXml.getAbsolutePath());
      System.exit(-1);
    }
    transformXml();
    postProcess();
    System.out.printf("Generated %s\n", config.outputXml.getAbsolutePath());
  }


  private void transformXml()
      throws TransformerException {
    TransformerFactory factory = TransformerFactory.newInstance();
    Source xslt = new StreamSource(config.stylesheet);
    Transformer transformer = factory.newTransformer(xslt);

    Source text = new StreamSource(config.inputXml);
    transformer.transform(text, new StreamResult(config.outputXml));
  }

  // (ducks).. some things are easier to do outside XSLT.
  private void postProcess() throws IOException {
    String content = Files.toString(config.outputXml, UTF_8);
    // Someone somewhere knows how to preserve linefeeds in XSLT.
    content = content.replaceAll("><o", ">" + System.lineSeparator() + "<o");
    Files.write(content, config.outputXml, UTF_8);
  }

  private class Config {
    private static final String DEFAULT_INPUT = "input.xml";
    private static final String XSLT_FILENAME = "AddCloches.xslt";
    private static final String OUTPUT_XML_EXTENSION = ".Organ_Hauptwerk_xml";

    private final File inputXml;
    private final File outputXml;
    private final boolean showHelp;
    private final File stylesheet;

    public Config(String args[]) {
      showHelp = args.length > 2;
      inputXml = args.length > 0 ? new File(args[0]) : new File(DEFAULT_INPUT);
      String tmp = System.getProperty("java.io.tmpdir");
      outputXml = new File(tmp,
            Files.getNameWithoutExtension(inputXml.getName())
                + OUTPUT_XML_EXTENSION);
      stylesheet = args.length > 1 ? new File(tmp, args[1]) : new File(XSLT_FILENAME);
    }
  }
}
