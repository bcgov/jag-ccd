package ca.bc.gov.open.ccd;

import static org.mockito.Mockito.when;

import ca.bc.gov.open.ccd.common.document.Document;
import ca.bc.gov.open.ccd.common.document.GetDocument;
import ca.bc.gov.open.ccd.controllers.DocumentController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DocumentControllerTests {
    @Autowired private ObjectMapper objectMapper;

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void getDocumentTest() throws JsonProcessingException, InterruptedException {

        var req = new GetDocument();
        var one = new Document();

        one.setDocumentId("A");
        one.setCourtDivisionCd("A");

        req.setDocumentRequest(one);

        Map<String, String> m = new HashMap<>();
        m.put(
                "url",
                "https://test:8443/courts/Servlet?&url=https%3A%2F%2Ftest.com%2Fpp%2Fdb%2Ftest%2Fget%2F%3FTicket%3DPcHWnmp");
        m.put("resultCd", "1");
        m.put("resultMessage", "success");
        m.put("status", "1");
        ResponseEntity<Map<String, String>> responseEntity2 =
                new ResponseEntity<>(m, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<ParameterizedTypeReference<Map<String, String>>>any()))
                .thenReturn(responseEntity2);

        var out = "A";
        ResponseEntity<byte[]> responseEntity =
                new ResponseEntity<>(out.getBytes(StandardCharsets.UTF_8), HttpStatus.OK);

        // Set up to adobe response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<byte[]>>any()))
                .thenReturn(responseEntity);

        DocumentController documentController = new DocumentController(restTemplate, objectMapper);
        var resp = documentController.getDocument(req);
        Assertions.assertNotNull(resp);
    }
}
