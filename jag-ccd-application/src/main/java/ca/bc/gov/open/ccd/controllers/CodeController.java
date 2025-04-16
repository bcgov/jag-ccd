package ca.bc.gov.open.ccd.controllers;

import ca.bc.gov.open.ccd.common.code.values.*;
import ca.bc.gov.open.ccd.common.code.values.secure.*;
import ca.bc.gov.open.ccd.exceptions.ORDSException;
import ca.bc.gov.open.ccd.models.OrdsErrorLog;
import ca.bc.gov.open.ccd.models.RequestSuccessLog;
import ca.bc.gov.open.ccd.models.serializers.InstantSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

@Slf4j
@Endpoint
public class CodeController {
    @Value("${ccd.host}" + "common/")
    private String host = "https://127.0.0.1/";

    @Value("${ccd.generic-agen-id}")
    private String genericAgenId;

    @Value("${ccd.generic-part-id}")
    private String genericPartId;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public CodeController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(
            namespace = "http://brooks/CCD.Source.CodeValues.ws.provider:CodeValues",
            localPart = "getCodeValues")
    @ResponsePayload
    public GetCodeValuesResponse getCodeValues(
            @RequestPayload GetCodeValues getCodeValues, MessageContext messageContext)
            throws JsonProcessingException {

        if (messageContext != null) {
            SaajSoapMessage soapResponse = (SaajSoapMessage) messageContext.getResponse();
            soapResponse
                    .getEnvelope()
                    .addNamespaceDeclaration(
                            "SOAP-ENC", "http://schemas.xmlsoap.org/soap/encoding/");
            soapResponse
                    .getEnvelope()
                    .addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
            soapResponse
                    .getEnvelope()
                    .addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");

            SoapHeader respheader = soapResponse.getSoapHeader();

            respheader.addNamespaceDeclaration(
                    "SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/");
            respheader.addNamespaceDeclaration(
                    "SOAP-ENC", "http://schemas.xmlsoap.org/soap/envelope/");
            respheader.addNamespaceDeclaration(
                    "SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/");
        }
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "codevalues")
                        .queryParam(
                                "lastRetrievedDate",
                                InstantSerializer.convert(getCodeValues.getLastRetrievedDate()));

        try {
            HttpEntity<GetCodeValuesResponse> resp =
                    restTemplate.exchange(
                            builder.build().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetCodeValuesResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "getCodeValues")));

            if (resp.getBody().getCeisCodeValues() == null
                    && resp.getBody().getJustinCodeValues() == null) {
                return null;
            }

            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getCodeValues",
                                    ex.getMessage(),
                                    getCodeValues)));
            throw new ORDSException();
        }
    }
}
