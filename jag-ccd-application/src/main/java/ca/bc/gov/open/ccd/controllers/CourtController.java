package ca.bc.gov.open.ccd.controllers;

import ca.bc.gov.open.ccd.configuration.SoapConfig;
import ca.bc.gov.open.ccd.court.one.CourtListTypes;
import ca.bc.gov.open.ccd.court.one.GetCrtList;
import ca.bc.gov.open.ccd.court.one.GetCrtListResponse;
import ca.bc.gov.open.ccd.court.secure.one.GetCrtListSecure;
import ca.bc.gov.open.ccd.court.secure.one.GetCrtListSecureResponse;
import ca.bc.gov.open.ccd.exceptions.ORDSException;
import ca.bc.gov.open.ccd.models.OrdsErrorLog;
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
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Slf4j
@Endpoint
public class CourtController {
    @Value("${ccd.host}")
    private String host = "https://127.0.0.1/";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public CourtController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(namespace = SoapConfig.SOAP_NAMESPACE, localPart = "getCrtList")
    @ResponsePayload
    public GetCrtListResponse getCrtList(@RequestPayload GetCrtList getCrtList)
            throws JsonProcessingException {

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "appearance")
                        .queryParam("agencyIdentifierCd", getCrtList.getAgencyIdentifierCd())
                        .queryParam("roomCd", getCrtList.getRoomCd())

                        //                    TODO This might be a date but not sure confirm with
                        // ords
                        .queryParam("proceedingDate", getCrtList.getProceedingDate())
                        .queryParam("divisionCd", getCrtList.getDivisionCd())
                        .queryParam("fileNumber", getCrtList.getFileNumber());

        try {
            HttpEntity<CourtListTypes> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            CourtListTypes.class);
            var out = new GetCrtListResponse();
            out.setCourtLists(resp.getBody());
            return out;
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getCrtList",
                                    ex.getMessage(),
                                    null)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = SoapConfig.SOAP_NAMESPACE, localPart = "getCrtListSecure")
    @ResponsePayload
    public GetCrtListSecureResponse getCrtListSecure(@RequestPayload GetCrtListSecure getCrtList)
            throws JsonProcessingException {

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "appearance")
                        .queryParam("agencyIdentifierCd", getCrtList.getAgencyIdentifierCd())
                        .queryParam("roomCd", getCrtList.getRoomCd())

                        //                    TODO This might be a date but not sure confirm with
                        // ords
                        .queryParam("proceedingDate", getCrtList.getProceedingDate())
                        .queryParam("divisionCd", getCrtList.getDivisionCd())
                        .queryParam("fileNumber", getCrtList.getFileNumber())
                        .queryParam("requestAgenId", getCrtList.getRequestAgencyIdentifierId())
                        .queryParam("requestPartId", getCrtList.getRequestPartId())
                        .queryParam(
                                "requestDtm", InstantSerializer.convert(getCrtList.getRequestDtm()))
                        .queryParam("applicationCd", getCrtList.getApplicationCd());

        try {
            HttpEntity<GetCrtListSecureResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetCrtListSecureResponse.class);
            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getCrtListSecure",
                                    ex.getMessage(),
                                    null)));
            throw new ORDSException();
        }
    }
}
