package ca.bc.gov.open.ccd.comparison11.services;

import ca.bc.gov.open.ccd.common.criminal.file.content.GetCriminalFileContent;
import ca.bc.gov.open.ccd.common.criminal.file.content.GetCriminalFileContentResponse;
import ca.bc.gov.open.ccd.comparison11.config.WebServiceSenderWithAuth;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.container.ListChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

@Service
public class TestService {
    private final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

    private final WebServiceSenderWithAuth webServiceSenderWithAuth;

    private final Javers javers = JaversBuilder.javers().build();

    @Autowired
    public TestService(WebServiceSenderWithAuth webServiceSenderWithAuth) {
        this.webServiceSenderWithAuth = webServiceSenderWithAuth;
    }

    @Value("${host.api-host}")
    private String apiHost;

    @Value("${host.wm-host}")
    private String wmHost;

    @Value("${host.username}")
    private String username;

    @Value("${host.password}")
    private String password;

    private String RAID = "83.0001";
    private String partId = RAID;
    private Instant dtm = Instant.now();

    private PrintWriter fileOutput;
    private static String outputDir = "comparison-tool-1.1/results/";

    private int overallDiff = 0;

    public void runCompares() throws IOException, SOAPException {
        System.out.println("INFO: CCD Diff testing started");

        getCriminalFileContentCompare();
    }

    private void getCriminalFileContentCompare() throws IOException, SOAPException {
        int diffCounter = 0;

        wmHost = "https://wsgw.dev.jag.gov.bc.ca/ccdapp/ocp/api/CriminalFileContent";
        apiHost = "https://jag-ccd-1-1-52e74e-dev.apps.silver.devops.gov.bc.ca/ws";

        GetCriminalFileContent request = new GetCriminalFileContent();
        request.setAgencyIdentifierCd(RAID);
        request.setProceedingDate(dtm);
        request.setRoomCd("009");
        request.setAppearanceID("83.0001");

        InputStream inputIds = getClass().getResourceAsStream("/getCriminalFileContent.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput =
                new PrintWriter(outputDir + " getCriminalFileContent.txt", StandardCharsets.UTF_8);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("\nINFO: getCriminalFileContent with CCD Mdoc: " + line);
            request.setMdocJustinNo(line);
            if (!compare(new GetCriminalFileContentResponse(), request)) {
                fileOutput.println("\nINFO: getCriminalFileContent with CCD Mdoc: " + line);
                ++diffCounter;
            }
        }

        System.out.println(
                "########################################################\n"
                        + "INFO: getFileDetailCriminal Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        fileOutput.println(
                "########################################################\n"
                        + "INFO: getFileDetailCriminal Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        overallDiff += diffCounter;
        fileOutput.close();
    }

    public <T, G> boolean compare(T response, G request) throws SOAPException {

        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();

        SaajSoapMessageFactory messageFactory =
                new SaajSoapMessageFactory(MessageFactory.newInstance("SOAP 1.1 Protocol"));
        messageFactory.afterPropertiesSet();

        webServiceTemplate.setMessageSender(webServiceSenderWithAuth);
        webServiceTemplate.setMessageFactory(messageFactory);
        jaxb2Marshaller.setContextPaths(
                "ca.bc.gov.open.ccd.common.code.values",
                "ca.bc.gov.open.ccd.common.code.values.secure",
                "ca.bc.gov.open.ccd.common.code.values.secure",
                "ca.bc.gov.open.ccd.common.criminal.file.content",
                "ca.bc.gov.open.ccd.common.criminal.file.content.secure",
                "ca.bc.gov.open.ccd.common.dev.utils",
                "ca.bc.gov.open.ccd.common.document",
                "ca.bc.gov.open.ccd.common.document.secure",
                "ca.bc.gov.open.ccd.common.process.results",
                "ca.bc.gov.open.ccd.common.rop.report",
                "ca.bc.gov.open.ccd.common.rop.report.secure",
                "ca.bc.gov.open.ccd.common.user.login",
                "ca.bc.gov.open.ccd.common.user.mapping");

        webServiceTemplate.setMarshaller(jaxb2Marshaller);
        webServiceTemplate.setUnmarshaller(jaxb2Marshaller);
        webServiceTemplate.afterPropertiesSet();

        T resultObjectWM = null;
        T resultObjectAPI = null;

        try {
            // resultObjectWM = (T) webServiceTemplate.marshalSendAndReceive(wmHost, request);
            resultObjectAPI = (T) webServiceTemplate.marshalSendAndReceive(apiHost, request);
            Thread.sleep(5000);

        } catch (Exception e) {
            System.out.println("ERROR: Failed to send request... " + e);
            fileOutput.println("ERROR: Failed to send request... " + e);

            try {
                Thread.sleep(5000);
            } catch (Exception e1) {
            }
        }

        Diff diff = javers.compare(resultObjectAPI, resultObjectWM);

        String responseClassName = response.getClass().getName();
        if (diff.hasChanges()) {
            printDiff(diff);
            return false;
        } else {
            if (resultObjectAPI == null && resultObjectWM == null)
                System.out.println(
                        "WARN: "
                                + responseClassName.substring(
                                        responseClassName.lastIndexOf('.') + 1)
                                + ": NULL responses");
            else
                System.out.println(
                        "INFO: "
                                + responseClassName.substring(
                                        responseClassName.lastIndexOf('.') + 1)
                                + ": No Diff Detected");
            return true;
        }
    }

    private void printDiff(Diff diff) {
        List<Change> actualChanges = new ArrayList<>(diff.getChanges());

        actualChanges.removeIf(
                c -> {
                    if (c instanceof ValueChange) {
                        return ((ValueChange) c).getLeft() == null
                                && ((ValueChange) c).getRight().toString().isBlank();
                    }

                    return false;
                });

        int diffSize = actualChanges.size();

        if (diffSize == 0) {
            System.out.println("Only null and blank changes detected");
            return;
        }

        String[] header = new String[] {"Property", "API Response", "WM Response"};
        String[][] table = new String[diffSize + 1][3];
        table[0] = header;

        for (int i = 0; i < diffSize; ++i) {
            Change ch = diff.getChanges().get(i);

            if (ch instanceof ListChange) {
                String apiVal =
                        ((ListChange) ch).getLeft() == null
                                ? "null"
                                : ((ListChange) ch).getLeft().toString();
                String wmVal =
                        ((ListChange) ch).getRight() == null
                                ? "null"
                                : ((ListChange) ch).getRight().toString();
                table[i + 1][0] = ((ListChange) ch).getPropertyNameWithPath();
                table[i + 1][1] = apiVal;
                table[i + 1][2] = wmVal;
            } else if (ch instanceof ValueChange) {
                String apiVal =
                        ((ValueChange) ch).getLeft() == null
                                ? "null"
                                : ((ValueChange) ch).getLeft().toString();
                String wmVal =
                        ((ValueChange) ch).getRight() == null
                                ? "null"
                                : ((ValueChange) ch).getRight().toString();
                table[i + 1][0] = ((ValueChange) ch).getPropertyNameWithPath();
                table[i + 1][1] = apiVal;
                table[i + 1][2] = wmVal;
            }
        }

        boolean leftJustifiedRows = false;
        int totalColumnLength = 10;
        /*
         * Calculate appropriate Length of each column by looking at width of data in
         * each column.
         *
         * Map columnLengths is <column_number, column_length>
         */
        Map<Integer, Integer> columnLengths = new HashMap<>();
        Arrays.stream(table)
                .forEach(
                        a ->
                                Stream.iterate(0, (i -> i < a.length), (i -> ++i))
                                        .forEach(
                                                i -> {
                                                    if (columnLengths.get(i) == null) {
                                                        columnLengths.put(i, 0);
                                                    }
                                                    if (columnLengths.get(i) < a[i].length()) {
                                                        columnLengths.put(i, a[i].length());
                                                    }
                                                }));

        for (Map.Entry<Integer, Integer> e : columnLengths.entrySet()) {
            totalColumnLength += e.getValue();
        }
        fileOutput.println("=".repeat(totalColumnLength));
        System.out.println("=".repeat(totalColumnLength));

        final StringBuilder formatString = new StringBuilder("");
        String flag = leftJustifiedRows ? "-" : "";
        columnLengths.entrySet().stream()
                .forEach(e -> formatString.append("| %" + flag + e.getValue() + "s "));
        formatString.append("|\n");

        Stream.iterate(0, (i -> i < table.length), (i -> ++i))
                .forEach(
                        a -> {
                            fileOutput.printf(formatString.toString(), table[a]);
                            System.out.printf(formatString.toString(), table[a]);
                        });

        fileOutput.println("=".repeat(totalColumnLength));
        System.out.println("=".repeat(totalColumnLength));
    }
}
