package ca.bc.gov.open.ccd.controllers;

import ca.bc.gov.open.ccd.common.process.results.*;
import ca.bc.gov.open.ccd.exceptions.ORDSException;
import ca.bc.gov.open.ccd.models.OrdsErrorLog;
import ca.bc.gov.open.ccd.models.RequestSuccessLog;
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

import java.text.MessageFormat;

@Slf4j
@Endpoint
public class ProcessController {

    @Value("${ccd.host}")
    private String host = "https://127.0.0.1/";

    private static final String PROCESS_NAMESPACE =
            "http://court.ag.gov.bc.ca/CCD.Source.ProcessResults.ws.provider:ProcessResults";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProcessController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processVariation")
    @ResponsePayload
    public ProcessVariationResponse processVariation(@RequestPayload ProcessVariation process)
            throws JsonProcessingException {

        var inner = process.getVariation() != null ? process.getVariation() : new VariationType();

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "criminal/variation");

        int i = 0;
        for (var detail : inner.getVariationDetail()) {
            detail.setVariationDetailId(Integer.toString(i++));
        }

        HttpEntity<VariationType> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessVariationResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessVariationResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processVariation")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processVariation",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processSpeaker")
    @ResponsePayload
    public ProcessSpeakerResponse processSpeaker(@RequestPayload ProcessSpeaker process)
            throws JsonProcessingException {

        var inner = process.getSpeaker() != null ? process.getSpeaker() : new Speaker();

        int i = 0;
        for (var detail : inner.getSpeakerDetail()) {
            detail.setSpeakerDetailId(Integer.toString(i++));
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "criminal/speaker");

        HttpEntity<Speaker> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessSpeakerResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessSpeakerResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processSpeaker")));
            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processSpeaker",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processCivilResults")
    @ResponsePayload
    public ProcessCivilResultsResponse processCivilResults(
            @RequestPayload ProcessCivilResults process) throws JsonProcessingException {

        var inner =
                process.getCivilResult() != null ? process.getCivilResult() : new CivilResultType();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "civil/results");

        int i = 0;
        for (var details : inner.getCivilResultDetail()) {
            details.setCivilResultDetailId(Integer.toString(i++));
        }

        i = 0;
        for (var appearances : inner.getNextAppearance()) {
            appearances.setNextAppearanceId(Integer.toString(i++));
        }

        i = 0;
        for (var restrictions : inner.getHearingRestriction()) {
            restrictions.setHearingRestrictionId(Integer.toString(i++));
        }

        HttpEntity<CivilResultType> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessCivilResultsResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessCivilResultsResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processCivilResults")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processCivilResults",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processAppearanceMethod")
    @ResponsePayload
    public ProcessAppearanceMethodResponse processAppearanceMethod(
            @RequestPayload ProcessAppearanceMethod process) throws JsonProcessingException {

        var inner =
                process.getAppearanceMethod() != null
                        ? process.getAppearanceMethod()
                        : new AppearanceMethod();

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "criminal/appearance-method");

        int i = 0;
        for (AppearanceMethodDetailType detail : inner.getAppearanceMethodDetail()) {
            detail.setAppearanceMethodDetailId(Integer.toString(i++));
        }

        HttpEntity<AppearanceMethod> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessAppearanceMethodResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessAppearanceMethodResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processAppearanceMethod")));

            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processAppearanceMethod",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processPlea")
    @ResponsePayload
    public ProcessPleaResponse processPlea(@RequestPayload ProcessPlea process)
            throws JsonProcessingException {

        var inner = process.getPlea() != null ? process.getPlea() : new Plea();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "criminal/plea");

        int i = 0;
        for (var detail : inner.getPleaDetail()) {
            detail.setPleaDetailId(Integer.toString(i++));
        }

        HttpEntity<Plea> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessPleaResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessPleaResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processPlea")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processPlea",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processElection")
    @ResponsePayload
    public ProcessElectionResponse processElection(@RequestPayload ProcessElection process)
            throws JsonProcessingException {

        var inner = process.getElection() != null ? process.getElection() : new Election();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "criminal/election");

        int i = 0;
        for (var detail : inner.getElectionDetails()) {
            detail.setElectionDetailId(Integer.toString(i++));
        }

        HttpEntity<Election> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessElectionResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessElectionResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processElection")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processElection",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processBail")
    @ResponsePayload
    public ProcessBailResponse processBail(@RequestPayload ProcessBail process)
            throws JsonProcessingException {

        var inner = process.getBailDocInput() != null ? process.getBailDocInput() : new BailDoc();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "criminal/bail");

        int i = 0;
        for (var detail : inner.getBailDetail()) {
            detail.setBailDetailId(Integer.toString(i++));
        }

        HttpEntity<BailDoc> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessBailResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessBailResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processBail")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processBail",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processCriminalResult")
    @ResponsePayload
    public ProcessCriminalResultResponse processCriminalResult(
            @RequestPayload ProcessCriminalResult process) throws JsonProcessingException {

        var inner =
                process.getCriminalResult() != null
                        ? process.getCriminalResult()
                        : new CriminalResult();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "criminal/results");

        int i = 0;
        for (CriminalResultsDetail detail : inner.getCriminalResultsDetail()) {
            detail.setCriminalResultsDetailId(Integer.toString(i++));
            int j = 0;
            for (NextAppearanceCRType nextAppr : detail.getNextAppearance()) {
                nextAppr.setNextAppearanceId(Integer.toString(j++));
            }
        }

        HttpEntity<CriminalResult> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessCriminalResultResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessCriminalResultResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processCriminalResult")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processCriminalResult",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processAgeNotice")
    @ResponsePayload
    public ProcessAgeNoticeResponse processAgeNotice(@RequestPayload ProcessAgeNotice process)
            throws JsonProcessingException {

        var inner = process.getAgeNotice() != null ? process.getAgeNotice() : new AgeNotice();

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "criminal/age-notice");

        int i = 0;
        for (var detail : inner.getAgeNoticeDetail()) {
            detail.setAgeNoticeDetailId(Integer.toString(i++));
        }

        HttpEntity<AgeNotice> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessAgeNoticeResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessAgeNoticeResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processAgeNotice")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processAgeNotice",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processMatterCall")
    @ResponsePayload
    public ProcessMatterCallResponse processMatterCall(@RequestPayload ProcessMatterCall process)
            throws JsonProcessingException {

        var inner = process.getMatterCall() != null ? process.getMatterCall() : new MatterCall();

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "criminal/mattercall");

        int i = 0;
        for (var detail : inner.getMatterCallDetails()) {
            detail.setMatterCallDetailId(Integer.toString(i++));
        }

        HttpEntity<MatterCall> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessMatterCallResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessMatterCallResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processMatterCall")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processMatterCall",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processSentence")
    @ResponsePayload
    public ProcessSentenceResponse processSentence(@RequestPayload ProcessSentence process)
            throws JsonProcessingException {

        var inner = process.getSentence() != null ? process.getSentence() : new Sentence();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "criminal/sentence");

        int i = 0;
        for (var detail : inner.getSentencetDetail()) {
            detail.setSentencetDetailId(Integer.toString(i++));
            int j = 0;
            for (var monetaryTerm : detail.getMonetaryTerm()) {
                monetaryTerm.setMonetaryTermId(Integer.toString(j++));
            }
            j = 0;
            for (var nonMonetaryTerm : detail.getNonMonetaryTerm()) {
                nonMonetaryTerm.setNonMonetaryTermId(Integer.toString(j++));
                int k = 0;
                for (var term : nonMonetaryTerm.getTerm()) {
                    term.setTermId(Integer.toString(k++));
                }
            }
        }

        HttpEntity<Sentence> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessSentenceResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessSentenceResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processSentence")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processSentence",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processBan")
    @ResponsePayload
    public ProcessBanResponse processBan(@RequestPayload ProcessBan process)
            throws JsonProcessingException {

        var inner = process.getBan() != null ? process.getBan() : new Ban();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "criminal/ban");

        int i = 0;
        for (var detail : inner.getBanDetail()) {
            detail.setBanDetailId(Integer.toString(i++));
        }

        HttpEntity<Ban> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessBanResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessBanResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processBan")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processBan",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processNote")
    @ResponsePayload
    public ProcessNoteResponse processNote(@RequestPayload ProcessNote process)
            throws JsonProcessingException {

        var inner = process.getNote() != null ? process.getNote() : new NoteType();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "criminal/note");

        HttpEntity<NoteType> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessNoteResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessNoteResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processNote")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processNote",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processArraignment")
    @ResponsePayload
    public ProcessArraignmentResponse processArraignment(@RequestPayload ProcessArraignment process)
            throws JsonProcessingException {

        var inner = process.getArraignment() != null ? process.getArraignment() : new Arraignment();

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "criminal/arraignment");

        int i = 0;
        for (var detail : inner.getArraignmentDetail()) {
            detail.setArraignmentDetailId(Integer.toString(i++));
        }

        HttpEntity<Arraignment> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessArraignmentResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessArraignmentResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processArraignment")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processArraignment",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processEndorsement")
    @ResponsePayload
    public ProcessEndorsementResponse processEndorsement(@RequestPayload ProcessEndorsement process)
            throws JsonProcessingException {
        var inner = process.getEndorsement() != null ? process.getEndorsement() : new Endorsement();

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromUriString(MessageFormat.format("{0}{1}", host, "criminal/endorsement"));

        int i = 0;
        for (var detail : inner.getEndorsementDetail()) {
            detail.setEndorsementDetailId(Integer.toString(i++));
        }

        HttpEntity<Endorsement> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {

            HttpEntity<ProcessEndorsementResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessEndorsementResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processEndorsement")));
            return resp.getBody();
        } catch (Exception ex) {

            inner.setEnterUserId("");

            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processEndorsement",
                                    ex.getMessage(),
                                    process)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processMove")
    @ResponsePayload
    public ProcessMoveResponse processMove(@RequestPayload ProcessMove process)
            throws JsonProcessingException {

        var inner = process.getMove() != null ? process.getMove() : new Move();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "criminal/move");

        int i = 0;
        for (var detail : inner.getMoveDetail()) {
            detail.setMoveDetailId(Integer.toString(i++));
        }

        HttpEntity<Move> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessMoveResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessMoveResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processMove")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processMove",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processFinding")
    @ResponsePayload
    public ProcessFindingResponse processFinding(@RequestPayload ProcessFinding process)
            throws JsonProcessingException {

        var inner = process.getFinding() != null ? process.getFinding() : new Finding();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "criminal/finding");

        int i = 0;
        for (var detail : inner.getFindingDetails()) {
            detail.setFindingDetailId(Integer.toString(i++));
        }

        HttpEntity<Finding> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessFindingResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessFindingResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processFinding")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processFinding",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processGenericResult")
    @ResponsePayload
    public ProcessGenericResultResponse processGenericResult(
            @RequestPayload ProcessGenericResult process) throws JsonProcessingException {

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "common/generic-result");

        var inner =
                process != null && process.getGenericResult() != null
                        ? process.getGenericResult()
                        : new GenericResult();

        int i = 0;
        for (var detail : inner.getResultDetail()) {
            detail.setResultDetailId(Integer.toString(i++));
        }

        HttpEntity<GenericResult> payload = new HttpEntity<>(inner, new HttpHeaders());
        try {
            HttpEntity<ProcessGenericResultResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessGenericResultResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processGenericResult")));
            return resp.getBody();
        } catch (Exception ex) {
            if (process != null && process.getGenericResult() != null)
                process.getGenericResult().setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processGenericResult",
                                    ex.getMessage(),
                                    process)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "ProcessCivilAppearanceMethod")
    @ResponsePayload
    public ProcessCivilAppearanceMethodResponse processCivilAppearanceMethod(
            @RequestPayload ProcessCivilAppearanceMethod process) throws JsonProcessingException {

        var inner =
                process.getCivilAppearanceMethod() != null
                        ? process.getCivilAppearanceMethod()
                        : new CivilAppearanceMethod();

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "civil/" + "appearance-method");

        int i = 0;
        for (CivilAppearanceMethodDetailType detail : inner.getCivilAppearanceMethodDetail()) {
            detail.setCivilAppearanceMethodDetailId(Integer.toString(i++));
            int j = 0;
            for (CivilPartyAppearanceMethodType party : detail.getPartyAppearanceMethod()) {
                party.setPartyAppearanceMethodId(Integer.toString(j++));
            }
        }

        HttpEntity<CivilAppearanceMethod> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessCivilAppearanceMethodResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessCivilAppearanceMethodResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "ProcessCivilAppearanceMethod")));

            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "ProcessCivilAppearanceMethod",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processOrder")
    @ResponsePayload
    public ProcessOrderResponse processOrder(@RequestPayload ProcessOrder process)
            throws JsonProcessingException {

        var inner = process.getOrder() != null ? process.getOrder() : new OrderType();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "criminal/order");

        int i = 0;
        for (var detail : inner.getOrderDetail()) {
            detail.setOrderDetailId(Integer.toString(i++));
            int j = 0;
            for (var event : detail.getOrderEvent()) {
                event.setOrderEventId(Integer.toString(j++));
            }
        }

        HttpEntity<OrderType> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessOrderResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessOrderResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processOrder")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processOrder",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processCivilOrderResult")
    @ResponsePayload
    public ProcessCivilOrderResultResponse processCivilOrderResult(
            @RequestPayload ProcessCivilOrderResult process) throws JsonProcessingException {

        var inner =
                process.getCivilOrderResult() != null
                        ? process.getCivilOrderResult()
                        : new CivilOrderType();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "civil/order");

        int i = 0;
        for (var detail : inner.getCivilOrderDetail()) {
            detail.setCivilOrderDetailId(Integer.toString(i++));
        }

        i = 0;
        for (var detail : inner.getAdminOrderDetail()) {
            detail.setAdminOrderDetailId(Integer.toString(i++));
        }

        HttpEntity<CivilOrderType> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessCivilOrderResultResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessCivilOrderResultResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processCivilOrderResult")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processCivilOrderResult",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processExhibit")
    @ResponsePayload
    public ProcessExhibitResponse processExhibit(@RequestPayload ProcessExhibit process)
            throws JsonProcessingException {

        var inner =
                process.getExhibitRequest() != null ? process.getExhibitRequest() : new Exhibit();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "criminal/exhibit");

        int i = 0;
        for (var detail : inner.getExhibitDetail()) {
            detail.setExhibitDetailId(Integer.toString(i++));
        }

        HttpEntity<Exhibit> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessExhibitResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessExhibitResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processExhibit")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processExhibit",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "processSpecialCourt")
    @ResponsePayload
    public ProcessSpecialCourtResponse processSpecialCourt(
            @RequestPayload ProcessSpecialCourt process) throws JsonProcessingException {

        var inner =
                process.getSpecialCourt() != null ? process.getSpecialCourt() : new SpecialCourt();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "appearance");

        HttpEntity<SpecialCourt> payload = new HttpEntity<>(inner, new HttpHeaders());

        try {
            HttpEntity<ProcessSpecialCourtResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            payload,
                            ProcessSpecialCourtResponse.class);

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "processSpecialCourt")));
            return resp.getBody();
        } catch (Exception ex) {
            inner.setEnterUserId("");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "processSpecialCourt",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }
}
