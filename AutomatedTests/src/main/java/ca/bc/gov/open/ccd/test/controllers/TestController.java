package ca.bc.gov.open.ccd.test.controllers;

import ca.bc.gov.open.ccd.test.services.TestService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tests")
public class TestController {
    private TestService testService;

    @Autowired
    public TestController(TestService testService) throws IOException {

        this.testService = testService;
        this.testService.setAuthentication("CCDUserMapping-soapui-project-template.xml");
        this.testService.setAuthentication("CCDCivilFileContent-soapui-project-template.xml");
        this.testService.setAuthentication("CCDCodeValues-soapui-project-template.xml");
        this.testService.setAuthentication("CCDCriminalFileContent-soapui-project-template.xml");
        this.testService.setAuthentication("CCDGetUserLogin-soapui-project-template.xml");
        this.testService.setAuthentication("CCDGetROPReport-soapui-project-template.xml");
        this.testService.setAuthentication("CCDProcessResults-soapui-project-template.xml");
    }

    @GetMapping(value = "/all")
    public ResponseEntity runAllTests() throws IOException {

        File f = testService.runAllTests();
        if (f != null && f.exists()) {
            InputStream inputStream = new FileInputStream(f.getAbsolutePath());
            //      This is not great streaming would be better but small files should be ok
            byte[] out = org.apache.commons.io.IOUtils.toByteArray(inputStream);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("content-disposition", "attachment; filename=" + f.getName());
            responseHeaders.add("Content-Type", "application/zip");
            f.delete();
            return new ResponseEntity<byte[]>(out, responseHeaders, HttpStatus.OK);
        } else {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Content-Type", "application/json");
            return new ResponseEntity<String>(
                    "{\"status\": \"All tests passed\"}", responseHeaders, HttpStatus.OK);
        }
    }

    @GetMapping("/ping")
    public String ping() {
        return "ping";
    }
}
