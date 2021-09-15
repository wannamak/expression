/**
 * Tool to add expression to an ODF file.
 *
 * First, generates a stylesheet from the supplied stylesheet template.
 * This involves querying a database for the appropriate PipeIDs.
 * We rely on an XSLT transformation to add most expression information
 * to the ODF file. However, the EnclosurePipe table requires a list of all
 * enclosed PipeId. Out of convenience we read that from a mysql database, but
 * it could come from the ODF file.
 *
 * Second, uses the generated stylesheet to transform the ODF to one with
 * expression.
 */

package transform;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.google.common.io.Files;

public class AddExpression {
  // TODO System.lineSeparator()
  private static final String LF = "<xsl:text>&#10;</xsl:text>";
  private static final int ATTENUATION_DECIBELS = 5;
  private static final int DIVISION_ID = 3;

  private final Config config;

  public static void main(String[] args) throws Exception {
    new AddExpression(args).run();
  }

  public AddExpression(String args[]) {
    config = new Config(args);
  }

  public void run()
      throws IOException, URISyntaxException, TransformerException, SQLException,
      ClassNotFoundException {
    if (config.showHelp) {
      System.err.printf("Usage    : AddExpression %s %s [%s]\n",
          config.stylesheetTemplate.getName(),
          config.inputXml.getName(),
          config.jdbcUrl);
      System.err.printf("Generates: %s\n", config.outputXml.getAbsolutePath());
      System.exit(-1);
    }
    generateStylesheet();
    transformXml();
    postProcess();
    System.out.printf("Generated %s\n", config.outputXml.getAbsolutePath());
  }

  private void generateStylesheet()
      throws IOException, SQLException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    Connection conn = DriverManager.getConnection(config.jdbcUrl);
    Statement stmt = conn.createStatement();

    // Find the PipeId for all pipes in a division.
    ResultSet results = stmt.executeQuery("SELECT pipe_soundengine01.PipeId "
        + "FROM pipe_soundengine01 "
        + "INNER JOIN stoprank on pipe_soundengine01.RankID = stoprank.RankID "
        + "INNER JOIN stop on stoprank.StopID = stop.StopID "
        + "WHERE stop.DivisionID = "
        + DIVISION_ID
        + " AND stoprank.MidiNoteNumIncrementFromDivisionToRank = 0");
    // the last condition is to eliminate duplicates in the join due to the
    // piccolo breaking back.

    String enclosurePipeData = "";
    while (results.next()) {
      enclosurePipeData += String.format(
          "<o><a>%d</a><b>%d</b><c>%d</c><d>%s</d><e>%s</e><h>%s</h></o>%s",
          2, // new EnclosureID
          results.getInt(1), // PipeID
          ATTENUATION_DECIBELS, // FiltParamWhenClsd_OverallAttnDb
          "2e+2", // FiltParamWhenClsd_MaxFreqHz
          "8e+3", // FiltParamWhenClsd_MinFreqHz
          "4e+3", // FiltParamWhenOpen_MinFreqHz
          System.lineSeparator());
      enclosurePipeData += LF;
    }
    conn.close();

    String content = Files.toString(config.stylesheetTemplate, UTF_8);
    content = content.replace("ENCLOSURE_PIPE_DATA", enclosurePipeData);
    Files.write(content, config.generatedStylesheet, UTF_8);
  }

  private void transformXml()
      throws TransformerException, IOException {
    TransformerFactory factory = TransformerFactory.newInstance();
    Source xslt = new StreamSource(config.generatedStylesheet);
    Transformer transformer = factory.newTransformer(xslt);

    Source text = new StreamSource(config.inputXml);
    transformer.transform(text, new StreamResult(config.outputXml));
  }

  // (ducks).. some things are easier to do outside XSLT.
  private void postProcess() throws IOException {
    String content = Files.toString(config.outputXml, UTF_8);
    // Someone somewhere knows how to preserve linefeeds in XSLT.
    content = content.replaceAll("><o", ">" + System.lineSeparator() + "<o");
    // Reposition the existing swell pedal slightly right.
    content = content.replaceAll("<g>508</g>", "<g>523</g>");
    Files.write(content, config.outputXml, UTF_8);
  }

  private class Config {
    private static final String OUTPUT_XSLT_FILENAME = "generated.xslt";
    private static final String OUTPUT_XML_EXTENSION = ".Organ_Hauptwerk_xml";
    private static final String DEFAULT_JDBC_URL = "jdbc:mysql://localhost:3306/db_conn?user=root&password=sqlpassword";

    private final File inputXml;
    private final File outputXml;
    private final File stylesheetTemplate;
    private final String jdbcUrl;
    private final boolean showHelp;
    private final File generatedStylesheet;

    public Config(String args[]) {
      stylesheetTemplate = args.length == 0
          ? new File("AddExpression.xslt.template")
          : new File(args[0]);
      inputXml = args.length < 2 ? new File("input.xml") : new File(args[1]);
      String tmp = System.getProperty("java.io.tmpdir");
      outputXml = new File(tmp,
          Files.getNameWithoutExtension(inputXml.getName())
              + OUTPUT_XML_EXTENSION);
      jdbcUrl = args.length < 3 ? DEFAULT_JDBC_URL : args[2];
      showHelp = args.length != 2 && args.length != 3;
      generatedStylesheet = new File(tmp, OUTPUT_XSLT_FILENAME);
    }
  }
}
