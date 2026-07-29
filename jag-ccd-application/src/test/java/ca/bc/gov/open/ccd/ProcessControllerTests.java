package ca.bc.gov.open.ccd;

import static org.mockito.Mockito.when;

import ca.bc.gov.open.ccd.common.process.results.*;
import ca.bc.gov.open.ccd.controllers.CourtController;
import ca.bc.gov.open.ccd.controllers.ProcessController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProcessControllerTests {

    @Mock private ObjectMapper objectMapper;
    @Mock private RestTemplate restTemplate;
    @Mock private ProcessController processController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        processController = Mockito.spy(new ProcessController(restTemplate, objectMapper));
    }

    @Test
    public void processVariationTest() throws JsonProcessingException {

        var req = new ProcessVariation();
        var var = new VariationType();
        var.setCourtAgencyIdentifierCode("A");
        var.setCourtRoomCode("A");
        var.setCourtProceedingDate(Instant.now());
        var.setCreationTime(Instant.now());
        var.setEnterUserId("A");
        var.setEventTime(Instant.now());
        var.setEventTypeCode("A");
        var.setEventLogText("A");
        var.setSourceEventSeqNo("A");
        var.setSourcePackageId("A");

        var vdt = new VariationDetailType();
        vdt.setVariationType("A");
        vdt.setDocmId("A");
        vdt.setDocmTypeDsc("A");
        vdt.setVariationDetailText("A");
        var af = new ApplyToFileVariationType();
        af.setAppearanceId("A");
        af.setFileNumber("A");
        af.setCountId("A");
        af.setCountNumber("A");

        vdt.getApplyToFile().add(af);
        var.getVariationDetail().add(vdt);

        var out = new ProcessVariationResponse();
        out.setStatus("A");

        ResponseEntity<ProcessVariationResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessVariationResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processVariation(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processSpeakerTest() throws JsonProcessingException {
        var req = new ProcessSpeaker();
        var sp = new Speaker();

        sp.setTerminateExistingYN("A");
        var app = new ApplyToFileSpeakerType();
        app.setAppearanceId("A");
        app.setFileNumber("A");
        app.setFileCourtDivisionCd("A");

        sp.getApplyToFile().add(app);

        var sd = new SpeakerDetailsType();
        sd.setSpeakerId("A");
        sd.setSpeakerTypeCd("A");
        sd.setSpeakerSeqNo("A");
        sd.setSpeakerName("A");
        sd.setVoirDireSeqNo("A");
        sd.setSpeakerStatusDsc("A");
        sd.setAttendanceMethodCd("A");
        var se = new SpeakerEventsType();
        se.setSpeakerEventDate(Instant.now());
        se.setSpeakerEventTime("A");
        se.setSpeakerEventText("A");

        sd.getSpeakerEvent().add(se);

        req.setSpeaker(sp);

        var out = new ProcessSpeakerResponse();
        out.setStatus("A");

        ResponseEntity<ProcessSpeakerResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessSpeakerResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processSpeaker(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processCivilResultsTest() throws JsonProcessingException {
        var req = new ProcessCivilResults();
        var cr = new CivilResultType();
        cr.setCourtAgencyIdentifierCode("A");
        cr.setCourtRoomCode("A");
        cr.setCourtProceedingDate(Instant.now());
        cr.setCreationTime("A");
        cr.setEnterUserId("A");
        cr.setEventTime("A");
        cr.setEventTypeCode("A");
        cr.setEventLogText("A");
        cr.setSourceEventSeqNo("A");
        cr.setSourcePackageId("A");

        var cd = new CivilResultDetailType();
        cd.setAppearanceId("A");
        cd.setDocumentId("A");
        cd.setDocumentResultCd("A");
        cd.setWarrantTypeCd("A");
        cd.setWithNoticeImmediateCd("A");
        cd.setWarrantPartyId("A");
        cd.setWarrantPartyName("A");
        cd.setWarrantReasonForArrestCd("A");
        cd.setWarrantReasonForArrestText("A");
        IssueType is = new IssueType();
        is.setConcludedYN("A");
        is.setIssueNumber("A");
        is.setIssueResultCd("A");

        var ni = new NextAppearanceType();
        ni.setAppearanceDate(Instant.now());
        ni.setAppearanceId("A");
        ni.setAppearanceReason("A");
        ni.setCourtAgencyIdentifier("A");
        ni.setCourtRoom("A");
        ni.setAppearanceTime("A");

        var af = new ApplyToFileCivilResultType();
        af.setAppearanceId("A");
        af.setDocumentId("A");
        af.setFileNumber("A");
        af.setFileSeqNo("A");

        ni.getApplyToFile().add(af);

        PartyType pt = new PartyType();
        pt.setPartyId("A");
        pt.setIntepreterLanguageCd("A");
        pt.setAttendanceMethodCd("A");
        pt.setPhoneNumber("A");
        pt.setInstruction("A");

        ni.getParty().add(pt);

        var nd = new NewDocumentFiledType();
        nd.setAppearanceId("A");
        nd.setFilingPartyId("A");
        nd.setFilingPartyName("A");
        nd.setFilingPartyRole("A");
        nd.setDocumentDescription("A");
        nd.setDocumentResultCd("A");
        nd.setDocumentTypeCd("A");
        nd.setAccessLevelCd("A");

        var or = new OralApplicationType();
        or.setAppearanceId("A");
        or.setIssueDescription("A");
        or.setIssueResultCd("A");
        or.setRaisedByPartyId("A");
        or.setRaisedByPartyRole("A");
        or.setRaisedByPartyName("A");
        or.setIssueTypeCd("A");

        var jc = new JudgeCommentType();
        jc.setAppearanceId("A");
        jc.setJudgeCommentText("A");

        var hr = new HearingRestrictionType();
        hr.setHearingRestrictionType("A");
        hr.setJudgePartId("A");

        var at = new ApplyToFileCivilResultType();
        at.setAppearanceId("A");
        at.setFileSeqNo("A");
        at.setDocumentId("A");
        at.setFileNumber("A");

        hr.getApplyToFile().add(at);

        cr.getJudgeComment().add(jc);

        cr.getOralApplication().add(or);

        cr.getNewDocumentFiled().add(nd);

        cr.getNextAppearance().add(ni);
        cd.getIssues().add(is);
        cr.getCivilResultDetail().add(cd);

        req.setCivilResult(cr);

        var out = new ProcessCivilResultsResponse();
        out.setStatus("A");

        ResponseEntity<ProcessCivilResultsResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessCivilResultsResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processCivilResults(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processAppearanceMethodTest() throws JsonProcessingException {
        var req = new ProcessAppearanceMethod();
        var one = new AppearanceMethod();

        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");
        var at = new AppearanceMethodDetailType();
        at.setJuryTrialYN("A");
        var af = new ApplyToFileAppearanceMethodType();
        af.setAppearanceId("A");
        af.setFileNumber("A");
        at.getApplyToFileAppearanceMethod().add(af);
        var pa = new PartyAppearanceMethodType();
        pa.setPartyName("A");
        pa.setPartyRole("A");
        pa.setPartyId("A");
        pa.setPartyAppearanceMethod("A");
        pa.setActionCd("A");
        pa.setAttendingPartyType("A");
        pa.setRemoteAttendanceYN("A");
        pa.setRemoteAttendanceYN("A");
        pa.setRemoteAgencyIdentifierId("A");
        pa.setRemoteCourtRoomAgencyIdentifierId("A");
        pa.setRemoteCourtRoomCd("A");
        at.getPartyAppearanceMethod().add(pa);
        one.getAppearanceMethodDetail().add(at);

        req.setAppearanceMethod(one);

        var out = new ProcessAppearanceMethodResponse();
        out.setStatus("A");

        ResponseEntity<ProcessAppearanceMethodResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessAppearanceMethodResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processAppearanceMethod(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processPleaTest() throws JsonProcessingException {
        var req = new ProcessPlea();
        var one = new Plea();

        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");
        var pd = new PleaDetails();
        pd.setActionCd("A");
        pd.setPleaCd("A");
        pd.setSectionOtherStatuteId("A");
        pd.setSectionOther("A");
        pd.setJudgeStrikePleaYN("A");
        var ap = new ApplyToFilePleaType();
        ap.setAppearanceId("A");
        ap.setCountId("A");
        ap.setFileNumber("A");
        ap.setCountNumber("A");

        pd.getApplyToFile().add(ap);
        one.getPleaDetail().add(pd);
        req.setPlea(one);

        var out = new ProcessPleaResponse();
        out.setStatus("A");

        ResponseEntity<ProcessPleaResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessPleaResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processPlea(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processElectionTest() throws JsonProcessingException {
        var req = new ProcessElection();
        var one = new Election();
        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");
        var ed = new ElectionDetailType();
        ed.setElection("A");
        ed.setCrownProceed("A");
        var ad = new ApplyToFileElectionType();
        ad.setAppearanceId("A");
        ad.setFileNumber("A");
        ad.setCountId("A");
        ad.setCountNumber("A");
        ed.getApplyToFileElection().add(ad);
        one.getElectionDetails().add(ed);

        req.setElection(one);
        var out = new ProcessElectionResponse();
        out.setStatus("A");

        ResponseEntity<ProcessElectionResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessElectionResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processElection(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processBailTest() throws JsonProcessingException {
        var req = new ProcessBail();
        var one = new BailDoc();
        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");
        var bt = new BailDetailType();
        bt.setBailOrderedTypeCd("A");
        bt.setResponsiblePersonName("A");
        bt.setRecogTypeCd("A");
        bt.setRecogAmount("A");
        bt.setRecogDeposit("A");
        bt.getAlternateBailText().add("A");
        bt.setBdpcYN("A");
        bt.setDetainedUnderSec524YN("A");
        bt.setSuretyQty("A");
        bt.setMoreSuretiesAllowedYn("A");
        var st = new SuretyType();
        st.setRecogSurety("A");
        st.setRecogNamedSurety("A");
        st.setSuretyAddress("A");
        st.setSuretyDeclaration("A");
        st.setSuretyDeposit("A");
        bt.getSurety().add(st);
        var af = new ApplyToFileBailType();
        af.setAppearanceId("A");
        af.setCountId("A");
        af.setCountNumber("A");
        af.setFileNumber("A");
        bt.getApplyToFile().add(af);

        one.getBailDetail().add(bt);
        req.setBailDocInput(one);

        var out = new ProcessBailResponse();
        out.setStatus("A");

        ResponseEntity<ProcessBailResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessBailResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processBail(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processCriminalResultTest() throws JsonProcessingException {
        var req = new ProcessCriminalResult();
        var one = new CriminalResult();
        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime("A");
        one.setEnterUserId("A");
        one.setEventTime("A");
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");
        var cd = new CriminalResultsDetail();
        cd.setAppearanceResult("A");
        cd.setJudgeSeizedName("A");
        cd.setJudgeSeizedPartId("A");
        cd.setJudgeDisqualifiedPartId("A");
        cd.setJudgeDisqualifiedName("A");
        cd.setTrialProceededYN("A");
        var af = new ApplyToFileCRType();
        af.setAppearanceId("A");
        af.setFileNumber("A");
        af.setCountId("A");
        af.setCountNumber("A");

        var nc = new NextAppearanceCRType();
        nc.setAppearanceDate(Instant.now());
        nc.setAppearanceTime("A");
        nc.setAppearanceReason("A");
        nc.setCourtAgencyIdentifier("A");
        nc.setCourtRoom("A");
        nc.setInterpreter("A");
        nc.setInterpreterForAllAppr("A");
        nc.setLanguage("A");
        nc.setAppearanceId("A");
        nc.setNotifySchedulerOnlyYN("A");

        var nct = new NextAppearanceMethodCRType();
        nct.setPartyName("A");
        nct.setPartyRole("A");
        nct.setAppearanceMethodCd("A");
        nct.setPhoneNumber("A");
        nct.setInstruction("A");

        nc.getNextAppMethod().add(nct);
        cd.getApplyToFile().add(af);

        var gd = new GenerateDocumentCRType();
        gd.setFormTypeCd("A");
        gd.setNextAppearanceDate(Instant.now());
        gd.setRegistryToCreateYN("A");
        gd.setReferralTo("A");
        gd.setReferralReason("A");
        gd.setReferralComment("A");

        cd.getGenerateDocument().add(gd);
        one.getCriminalResultsDetail().add(cd);
        req.setCriminalResult(one);

        var out = new ProcessCriminalResultResponse();
        out.setStatus("A");

        ResponseEntity<ProcessCriminalResultResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessCriminalResultResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processCriminalResult(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processAgeNoticeTest() throws JsonProcessingException {
        var req = new ProcessAgeNotice();
        var one = new AgeNotice();
        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");

        var an = new AgeNoticeDetailType();
        var af = new ApplyToFileTypeAgeNotice();
        af.setAppearanceId("A");
        af.setFileNumber("A");

        one.getAgeNoticeDetail().add(an);

        var ag = new AgeNoticeEventType();
        ag.setEventType("A");
        ag.setDetailText("A");
        ag.setDOB("A");
        ag.setRelationship("A");
        ag.setProvenBy("A");
        ag.setNoticeTo("A");

        an.getAgeNoticeEvent().add(ag);
        an.getApplyToFileAgeNotice().add(af);

        req.setAgeNotice(one);

        var out = new ProcessAgeNoticeResponse();
        out.setStatus("A");

        ResponseEntity<ProcessAgeNoticeResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessAgeNoticeResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processAgeNotice(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processMatterCallTest() throws JsonProcessingException {
        var req = new ProcessMatterCall();
        var one = new MatterCall();
        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");
        var mc = new MatterCallDetails();
        mc.setActionCd("A");
        mc.setPreviousSourceEventSeqNo("A");
        var cf = new CalledFile();
        cf.setAppearanceId("A");
        cf.setMdocJustinNo("A");
        cf.setPartId("A");
        cf.setProfSeqNo("A");
        cf.setCivilPhysicalFileId("A");
        cf.setFileNumber("A");
        cf.setFileCourtDivisionCd("A");
        mc.getCalledFile().add(cf);
        one.getMatterCallDetails().add(mc);

        req.setMatterCall(one);

        var out = new ProcessMatterCallResponse();
        out.setStatus("A");

        ResponseEntity<ProcessMatterCallResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessMatterCallResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processMatterCall(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processSentenceTest() throws JsonProcessingException {
        var req = new ProcessSentence();
        var one = new Sentence();
        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");
        var sd = new SentencetDetailType();

        one.getSentencetDetail().add(sd);
        var af = new SentenceApplyToFileType();
        af.setAppearanceId("A");
        af.setFileNumber("A");
        af.setCountId("A");
        af.setCountNumber("A");

        var mt = new MonetaryTermType();
        mt.setSentenceTypeCd("A");
        mt.setAmount("A");
        mt.setVictimSurchargeTypeCd("A");
        mt.setDueDate(Instant.now());
        mt.setPaymentScheduleText("A");
        mt.setJailYN("A");
        mt.setSameAsFineYN("A");
        mt.setName("A");
        mt.setSuretyText("A");

        var dt = new DurationType();
        dt.setDurationLength("A");
        dt.setDurationUnitCd("A");
        mt.getDuration().add(dt);

        var nt = new NonMonetaryTermType();
        nt.setSentenceTypeCd("A");
        nt.setTimeServedYN("A");
        nt.setIntermittentYN("A");
        nt.setConcurrentText("A");
        nt.setConsecutiveText("A");
        nt.setTermText("A");
        nt.setSectionText("A");
        nt.setReportingDate(Instant.now());
        nt.setReportingTime(Instant.now());
        nt.setReportingLocationText("A");

        var tt = new TermType();
        tt.setTermTypeCd("A");
        tt.getDuration().add(dt);
        nt.getTerm().add(tt);

        sd.getNonMonetaryTerm().add(nt);
        sd.getMonetaryTerm().add(mt);
        sd.getApplyToFile().add(af);

        req.setSentence(one);

        var out = new ProcessSentenceResponse();
        out.setStatus("A");

        ResponseEntity<ProcessSentenceResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessSentenceResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processSentence(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processBanTest() throws JsonProcessingException {
        var req = new ProcessBan();
        var one = new Ban();

        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");

        var bt = new BanDetailType();
        var af = new ApplyToFileBanType();
        af.setAppearanceId("A");
        af.setFileNumber("A");

        var bi = new BanInfoType();
        bi.setBanStatusCd("A");
        bi.setBanStatusCd("A");
        bi.setBanStatusComment("A");
        bi.setBanStatusDate("A");
        bi.setBanStatuteId("A");
        bi.setBanTypeCd("A");


        bt.getBanInfo().add(bi);
        bt.getApplyToFileBan().add(af);
        one.getBanDetail().add(bt);
        req.setBan(one);

        var out = new ProcessBanResponse();
        out.setStatus("A");

        ResponseEntity<ProcessBanResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessBanResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processBan(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processNoteTest() throws JsonProcessingException {
        var req = new ProcessNote();
        var one = new NoteType();

        one.setSourcePackageId("A");
        one.setEnterUserId("A");

        var af = new ApplyToFileType();

        af.setAppearanceId("A");
        af.setFileNumber("A");
        af.setFileCourtDivisionCd("A");
        one.getApplyToFile().add(af);

        var nd = new NoteDetailType();
        nd.setNoteText("A");
        nd.setNoteSeqNo("A");
        nd.setNoteTypeCd("A");

        one.getNoteDetail().add(nd);
        req.setNote(one);

        var out = new ProcessNoteResponse();
        out.setStatus("A");

        ResponseEntity<ProcessNoteResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessNoteResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processNote(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processArraignmentTest() throws JsonProcessingException {
        var req = new ProcessArraignment();
        var one = new Arraignment();

        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");

        var ad = new ArraignmentDetailType();
        var af = new ApplyToFileArraignmentType();
        af.setAppearanceId("A");
        af.setFileNumber("A");
        ad.getApplyToFileArraignment().add(af);

        var ae = new ArraignmentEventType();
        ae.setEventType("A");
        ae.setDetailText("A");

        ad.getArraignmentEvent().add(ae);
        one.getArraignmentDetail().add(ad);
        req.setArraignment(one);

        var out = new ProcessArraignmentResponse();
        out.setStatus("A");

        ResponseEntity<ProcessArraignmentResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessArraignmentResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processArraignment(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processEndorsementTest() throws JsonProcessingException {

        var req = new ProcessEndorsement();
        var one = new Endorsement();

        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");

        var ad = new EndorsementDetailType();
        var af = new ApplyToFileEndorsementType();
        af.setAppearanceId("A");
        af.setFileNumber("A");
        ad.getApplyToFileEndorsement().add(af);

        var ae = new EndorsementEventType();
        ae.setEventType("A");
        ae.setDetailText("A");

        ad.getEndorsementEvent().add(ae);
        one.getEndorsementDetail().add(ad);
        req.setEndorsement(one);

        var out = new ProcessEndorsementResponse();
        out.setStatus("A");

        ResponseEntity<ProcessEndorsementResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                Mockito.any(String.class),
                Mockito.eq(HttpMethod.POST),
                Mockito.<HttpEntity<String>>any(),
                Mockito.<Class<ProcessEndorsementResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processEndorsement(req);

        Assertions.assertNotNull(resp);

    }

    @Test
    public void processMoveTest() throws JsonProcessingException {
        var req = new ProcessMove();
        var one = new Move();
        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        var md = new MoveDetailType();
        md.setCourtAgencyIdentifier("A");
        md.setCourtRoom("A");

        var af = new ApplyToFileMoveType();
        af.setAppearanceId("A");
        af.setFileCourtDivisionCd("A");
        af.setFileNumber("A");
        md.getApplyToFileMove().add(af);
        one.getMoveDetail().add(md);
        req.setMove(one);

        var out = new ProcessMoveResponse();
        out.setStatus("A");

        ResponseEntity<ProcessMoveResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessMoveResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processMove(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processFindingTest() throws JsonProcessingException {
        var req = new ProcessFinding();
        var one = new Finding();
        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");

        var fd = new FindingDetailType();
        fd.setFinding("A");
        fd.setSectionOtherStatuteId("A");
        fd.setSectionOther("A");

        one.getFindingDetails().add(fd);

        var af = new ApplyToFileFindingType();
        af.setAppearanceId("A");
        af.setFileNumber("A");
        af.setCountId("A");
        af.setCountNumber("A");

        fd.getApplyToFileFinding().add(af);

        var out = new ProcessFindingResponse();
        out.setStatus("A");

        ResponseEntity<ProcessFindingResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessFindingResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processFinding(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processGenericResultTest() throws JsonProcessingException {
        var req = new ProcessGenericResult();
        var one = new GenericResult();
        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");

        var rd = new ResultDetail();
        rd.setEventSubType("A");
        rd.setEventValue("A");
        var af = new ApplyToFileGenericResults();
        af.setAppearanceId("A");
        af.setFileNumber("A");
        af.setCountId("A");
        af.setCountNumber("A");

        rd.getApplyToFileGenericResults().add(af);

        one.getResultDetail().add(rd);

        req.setGenericResult(one);

        var out = new ProcessGenericResultResponse();
        out.setStatus("A");

        ResponseEntity<ProcessGenericResultResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessGenericResultResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processGenericResult(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processCivilAppearanceMethodTest() throws JsonProcessingException {
        var req = new ProcessCivilAppearanceMethod();
        var one = new CivilAppearanceMethod();
        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");

        var cd = new CivilAppearanceMethodDetailType();
        cd.setJuryTrialYN("A");
        var af = new ApplyToFileCivilAppMethodType();
        af.setFileNumber("A");
        af.setAppearanceId("A");

        cd.getApplyToFile().add(af);

        var cm = new CivilPartyAppearanceMethodType();
        cm.setPartyName("A");
        cm.setPartyRole("A");
        cm.setPartyId("A");
        cm.setPartyTypeCd("A");
        cm.setPartyAppearanceMethod("A");

        var ct = new CounselType();
        ct.setCounselFullName("A");
        ct.setPartyId("A");
        ct.setAttendanceMethodCd("A");
        cm.getCounsel().add(ct);

        var rt = new RepresentativeType();
        rt.setRepresentativeFullName("A");
        rt.setAttendanceMethodCd("A");
        rt.setPartyId("A");

        cm.getRepresentative().add(rt);

        cd.getPartyAppearanceMethod().add(cm);

        one.getCivilAppearanceMethodDetail().add(cd);
        req.setCivilAppearanceMethod(one);

        var out = new ProcessCivilAppearanceMethodResponse();
        out.setStatus("A");

        ResponseEntity<ProcessCivilAppearanceMethodResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessCivilAppearanceMethodResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processCivilAppearanceMethod(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processOrderTest() throws JsonProcessingException {
        var req = new ProcessOrder();
        var one = new OrderType();
        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");

        var od = new OrderDetailType();
        var af = new ApplyToFileOrderType();
        af.setAppearanceId("A");
        af.setFileNumber("A");
        od.getApplyToFile().add(af);

        var oe = new OrderEventType();
        oe.setOrderDetailText("A");
        oe.setDueDate(Instant.now());
        oe.setDisclosureAuthorizedTo("A");
        oe.setOrderUnderSection("A");
        oe.setPurposeOfReport("A");
        oe.setCustody("A");
        var oct = new OrderTypeCdType();
        oct.setOrderTypeCd("A");
        oe.getOrderTypeCds().add(oct);

        one.getOrderDetail().add(od);
        req.setOrder(one);

        var out = new ProcessOrderResponse();
        out.setStatus("A");

        ResponseEntity<ProcessOrderResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessOrderResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processOrder(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processCivilOrderResultTest() throws JsonProcessingException {
        var req = new ProcessCivilOrderResult();
        var one = new CivilOrderType();
        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");

        var co = new CivilOrderDetailType();
        co.setOrderTypeCd("A");
        co.setAdjudicatorPartId("A");
        co.setEnteredDate(Instant.now());
        co.setExpiryDate(Instant.now());
        co.setRegistryToDraftYN("A");
        co.setNoticeRequiredYN("A");
        co.setFinalYN("A");
        co.setInterimYN("A");
        co.setByConsentYN("A");
        co.setExParteYN("A");
        co.setSealedYN("A");
        co.setBanYN("A");
        co.setFirearmYN("A");
        co.setVaryDocumentId("A");
        co.setCancelDocumentId("A");

        var af = new ApplyToFileCivilOrderType();
        af.setAppearanceId("A");
        af.setFileNumber("A");
        af.setDocumentId("A");
        af.setFileSeqNo("A");
        co.getApplyToFile().add(af);

        var ot = new OrderTermType();
        ot.setOrderTermSeqNo("A");
        ot.setOrderTermEventTime(Instant.now());
        ot.setOrderTermText("A");
        ot.setOrderTermLogNoteText("A");
        co.getOrderTerm().add(ot);

        var op = new CivilOrderPartyType();
        op.setOrderRoleCd("A");
        op.setPartyFullName("A");
        op.setPartyId("A");
        op.setBirthDate(Instant.now());
        co.getParty().add(op);

        one.getCivilOrderDetail().add(co);

        req.setCivilOrderResult(one);

        var ao = new AdminOrderDetailType();
        ao.getApplyToFile().add(af);
        ao.getOrderTerm().add(ot);

        one.getAdminOrderDetail().add(ao);

        var jc = new JudgeCommentType2();
        jc.setAppearanceId("A");
        jc.setJudgeCommentText("A");

        one.getJudgeComment().add(jc);

        var out = new ProcessCivilOrderResultResponse();
        out.setStatus("A");

        ResponseEntity<ProcessCivilOrderResultResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessCivilOrderResultResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processCivilOrderResult(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processExhibitTest() throws JsonProcessingException {
        var req = new ProcessExhibit();

        var one = new Exhibit();
        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");

        var ed = new ExhibitDetail();

        ed.setExhibitCardNo("A");
        ed.setExhibitType("A");

        var af = new ApplyToFile();
        af.setAppearanceId("A");
        af.setFileNumber("A");

        ed.getApplyToFile().add(af);

        var ei = new ExhibitItem();
        ei.setExhibitNo("A");
        ei.setSubmittedBy("A");
        ei.setExhibitDescription("A");
        ei.setOwnedBy("A");

        ed.getExhibitItem().add(ei);
        one.getExhibitDetail().add(ed);

        req.setExhibitRequest(one);

        var out = new ProcessExhibitResponse();
        out.setXMLMessageID("A");

        ResponseEntity<ProcessExhibitResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessExhibitResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processExhibit(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void processSpecialCourtTest() throws JsonProcessingException {
        var req = new ProcessSpecialCourt();
        var one = new SpecialCourt();
        one.setCourtAgencyIdentifierCode("A");
        one.setCourtRoomCode("A");
        one.setCourtProceedingDate(Instant.now());
        one.setCreationTime(Instant.now());
        one.setEnterUserId("A");
        one.setEventTime(Instant.now());
        one.setEventTypeCode("A");
        one.setEventLogText("A");
        one.setSourceEventSeqNo("A");
        one.setSourcePackageId("A");

        var sp = new SpecialCourtDetail();

        var af = new ApplyToFile2();
        af.setAppearanceId("A");
        af.setFileNumber("A");

        sp.getApplyToFile().add(af);

        sp.getSpecialCourtCd().add("A");
        req.setSpecialCourt(one);
        var out = new ProcessSpecialCourtResponse();
        out.setStatus("A");

        ResponseEntity<ProcessSpecialCourtResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ProcessSpecialCourtResponse>>any()))
                .thenReturn(responseEntity);

        var resp = processController.processSpecialCourt(req);

        Assertions.assertNotNull(resp);
    }
}
