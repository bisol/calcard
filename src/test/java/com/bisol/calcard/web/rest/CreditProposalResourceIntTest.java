package com.bisol.calcard.web.rest;

import static com.bisol.calcard.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.bisol.calcard.CalcardApp;
import com.bisol.calcard.domain.CreditProposal;
import com.bisol.calcard.domain.enumeration.CreditProposalStatus;
import com.bisol.calcard.domain.enumeration.FederationUnit;
import com.bisol.calcard.domain.enumeration.Gender;
import com.bisol.calcard.domain.enumeration.MaritalStatus;
import com.bisol.calcard.domain.enumeration.RejectionReason;
import com.bisol.calcard.repository.CreditProposalRepository;
import com.bisol.calcard.service.CreditProposalService;
import com.bisol.calcard.web.rest.errors.ExceptionTranslator;
/**
 * Test class for the CreditProposalResource REST controller.
 *
 * @see CreditProposalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CalcardApp.class)
public class CreditProposalResourceIntTest {

    private static final String DEFAULT_CLIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TAXPAYER_ID = "AAAAAAAAAA";
    private static final String UPDATED_TAXPAYER_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_CLIENT_AGE = 18;
    private static final Integer UPDATED_CLIENT_AGE = 19;

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final MaritalStatus DEFAULT_MARITAL_STATUS = MaritalStatus.SINGLE;
    private static final MaritalStatus UPDATED_MARITAL_STATUS = MaritalStatus.MARRIED;

    private static final FederationUnit DEFAULT_FEDERATION_UNIT = FederationUnit.AC;
    private static final FederationUnit UPDATED_FEDERATION_UNIT = FederationUnit.AM;

    private static final Integer DEFAULT_DEPENDENTS = 0;
    private static final Integer UPDATED_DEPENDENTS = 1;

    private static final BigDecimal DEFAULT_INCOME = new BigDecimal(0);
    private static final BigDecimal UPDATED_INCOME = new BigDecimal(1);

    private static final CreditProposalStatus DEFAULT_STATUS = CreditProposalStatus.PROCESSING;
    private static final CreditProposalStatus UPDATED_STATUS = CreditProposalStatus.APROVED;

    private static final RejectionReason DEFAULT_REJECTION_REASON = RejectionReason.POLICY;
    private static final RejectionReason UPDATED_REJECTION_REASON = RejectionReason.INCOME;

    private static final BigDecimal DEFAULT_APROVED_MIN = new BigDecimal(1);
    private static final BigDecimal UPDATED_APROVED_MIN = new BigDecimal(2);

    private static final BigDecimal DEFAULT_APROVED_MAX = new BigDecimal(1);
    private static final BigDecimal UPDATED_APROVED_MAX = new BigDecimal(2);

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PROCESSING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PROCESSING_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private CreditProposalRepository creditProposalRepository;

    @Autowired
    private CreditProposalService creditProposalService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCreditProposalMockMvc;

    private CreditProposal creditProposal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CreditProposalResource creditProposalResource = new CreditProposalResource(creditProposalService);
        this.restCreditProposalMockMvc = MockMvcBuilders.standaloneSetup(creditProposalResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreditProposal createEntity(EntityManager em) {
        CreditProposal creditProposal = new CreditProposal()
            .clientName(DEFAULT_CLIENT_NAME)
            .taxpayerId(DEFAULT_TAXPAYER_ID)
            .clientAge(DEFAULT_CLIENT_AGE)
            .gender(DEFAULT_GENDER)
            .maritalStatus(DEFAULT_MARITAL_STATUS)
            .federationUnit(DEFAULT_FEDERATION_UNIT)
            .dependents(DEFAULT_DEPENDENTS)
            .income(DEFAULT_INCOME)
            .status(DEFAULT_STATUS)
            .rejectionReason(DEFAULT_REJECTION_REASON)
            .aprovedMin(DEFAULT_APROVED_MIN)
            .aprovedMax(DEFAULT_APROVED_MAX)
            .creationDate(DEFAULT_CREATION_DATE)
            .processingDate(DEFAULT_PROCESSING_DATE);
        return creditProposal;
    }

    @Before
    public void initTest() {
        creditProposal = createEntity(em);
    }

    @Test
    @Transactional
    public void createCreditProposal() throws Exception {
        int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();

        // Create the CreditProposal
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isCreated());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate + 1);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getClientName()).isEqualTo(DEFAULT_CLIENT_NAME);
        assertThat(testCreditProposal.getTaxpayerId()).isEqualTo(DEFAULT_TAXPAYER_ID);
        assertThat(testCreditProposal.getClientAge()).isEqualTo(DEFAULT_CLIENT_AGE);
        assertThat(testCreditProposal.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testCreditProposal.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testCreditProposal.getFederationUnit()).isEqualTo(DEFAULT_FEDERATION_UNIT);
        assertThat(testCreditProposal.getDependents()).isEqualTo(DEFAULT_DEPENDENTS);
        assertThat(testCreditProposal.getIncome()).isEqualTo(DEFAULT_INCOME);
    }

    @Test
    @Transactional
    public void createCreditProposalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();

        // Create the CreditProposal with an existing ID
        creditProposal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkClientNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditProposalRepository.findAll().size();
        // set the field null
        creditProposal.setClientName(null);

        // Create the CreditProposal, which fails.

        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTaxpayerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditProposalRepository.findAll().size();
        // set the field null
        creditProposal.setTaxpayerId(null);

        // Create the CreditProposal, which fails.

        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClientAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditProposalRepository.findAll().size();
        // set the field null
        creditProposal.setClientAge(null);

        // Create the CreditProposal, which fails.

        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditProposalRepository.findAll().size();
        // set the field null
        creditProposal.setGender(null);

        // Create the CreditProposal, which fails.

        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaritalStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditProposalRepository.findAll().size();
        // set the field null
        creditProposal.setMaritalStatus(null);

        // Create the CreditProposal, which fails.

        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFederationUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditProposalRepository.findAll().size();
        // set the field null
        creditProposal.setFederationUnit(null);

        // Create the CreditProposal, which fails.

        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDependentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditProposalRepository.findAll().size();
        // set the field null
        creditProposal.setDependents(null);

        // Create the CreditProposal, which fails.

        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIncomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditProposalRepository.findAll().size();
        // set the field null
        creditProposal.setIncome(null);

        // Create the CreditProposal, which fails.

        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCreditProposals() throws Exception {
        // Initialize the database
        creditProposalRepository.saveAndFlush(creditProposal);

        // Get all the creditProposalList
        restCreditProposalMockMvc.perform(get("/api/credit-proposals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditProposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].taxpayerId").value(hasItem(DEFAULT_TAXPAYER_ID.toString())))
            .andExpect(jsonPath("$.[*].clientAge").value(hasItem(DEFAULT_CLIENT_AGE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].federationUnit").value(hasItem(DEFAULT_FEDERATION_UNIT.toString())))
            .andExpect(jsonPath("$.[*].dependents").value(hasItem(DEFAULT_DEPENDENTS)))
            .andExpect(jsonPath("$.[*].income").value(hasItem(DEFAULT_INCOME.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].rejectionReason").value(hasItem(DEFAULT_REJECTION_REASON.toString())))
            .andExpect(jsonPath("$.[*].aprovedMin").value(hasItem(DEFAULT_APROVED_MIN.intValue())))
            .andExpect(jsonPath("$.[*].aprovedMax").value(hasItem(DEFAULT_APROVED_MAX.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].processingDate").value(hasItem(DEFAULT_PROCESSING_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getCreditProposal() throws Exception {
        // Initialize the database
        creditProposalRepository.saveAndFlush(creditProposal);

        // Get the creditProposal
        restCreditProposalMockMvc.perform(get("/api/credit-proposals/{id}", creditProposal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(creditProposal.getId().intValue()))
            .andExpect(jsonPath("$.clientName").value(DEFAULT_CLIENT_NAME.toString()))
            .andExpect(jsonPath("$.taxpayerId").value(DEFAULT_TAXPAYER_ID.toString()))
            .andExpect(jsonPath("$.clientAge").value(DEFAULT_CLIENT_AGE))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.maritalStatus").value(DEFAULT_MARITAL_STATUS.toString()))
            .andExpect(jsonPath("$.federationUnit").value(DEFAULT_FEDERATION_UNIT.toString()))
            .andExpect(jsonPath("$.dependents").value(DEFAULT_DEPENDENTS))
            .andExpect(jsonPath("$.income").value(DEFAULT_INCOME.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.rejectionReason").value(DEFAULT_REJECTION_REASON.toString()))
            .andExpect(jsonPath("$.aprovedMin").value(DEFAULT_APROVED_MIN.intValue()))
            .andExpect(jsonPath("$.aprovedMax").value(DEFAULT_APROVED_MAX.intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.processingDate").value(DEFAULT_PROCESSING_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCreditProposal() throws Exception {
        // Get the creditProposal
        restCreditProposalMockMvc.perform(get("/api/credit-proposals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCreditProposal() throws Exception {
        // Initialize the database
        creditProposalService.save(creditProposal);

        int databaseSizeBeforeUpdate = creditProposalRepository.findAll().size();

        // Update the creditProposal
        CreditProposal updatedCreditProposal = creditProposalRepository.findById(creditProposal.getId()).get();
        // Disconnect from session so that the updates on updatedCreditProposal are not directly saved in db
        em.detach(updatedCreditProposal);
        updatedCreditProposal
            .clientName(UPDATED_CLIENT_NAME)
            .taxpayerId(UPDATED_TAXPAYER_ID)
            .clientAge(UPDATED_CLIENT_AGE)
            .gender(UPDATED_GENDER)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .federationUnit(UPDATED_FEDERATION_UNIT)
            .dependents(UPDATED_DEPENDENTS)
            .income(UPDATED_INCOME)
            .status(UPDATED_STATUS)
            .rejectionReason(UPDATED_REJECTION_REASON)
            .aprovedMin(UPDATED_APROVED_MIN)
            .aprovedMax(UPDATED_APROVED_MAX)
            .creationDate(UPDATED_CREATION_DATE)
            .processingDate(UPDATED_PROCESSING_DATE);

        restCreditProposalMockMvc.perform(put("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCreditProposal)))
            .andExpect(status().isOk());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeUpdate);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getClientName()).isEqualTo(UPDATED_CLIENT_NAME);
        assertThat(testCreditProposal.getTaxpayerId()).isEqualTo(UPDATED_TAXPAYER_ID);
        assertThat(testCreditProposal.getClientAge()).isEqualTo(UPDATED_CLIENT_AGE);
        assertThat(testCreditProposal.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testCreditProposal.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testCreditProposal.getFederationUnit()).isEqualTo(UPDATED_FEDERATION_UNIT);
        assertThat(testCreditProposal.getDependents()).isEqualTo(UPDATED_DEPENDENTS);
        assertThat(testCreditProposal.getIncome()).isEqualTo(UPDATED_INCOME);
        assertThat(testCreditProposal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCreditProposal.getRejectionReason()).isEqualTo(UPDATED_REJECTION_REASON);
        assertThat(testCreditProposal.getAprovedMin()).isEqualTo(UPDATED_APROVED_MIN);
        assertThat(testCreditProposal.getAprovedMax()).isEqualTo(UPDATED_APROVED_MAX);
        assertThat(testCreditProposal.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testCreditProposal.getProcessingDate()).isEqualTo(UPDATED_PROCESSING_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCreditProposal() throws Exception {
        int databaseSizeBeforeUpdate = creditProposalRepository.findAll().size();

        // Create the CreditProposal

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditProposalMockMvc.perform(put("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCreditProposal() throws Exception {
        // Initialize the database
        creditProposalService.save(creditProposal);

        int databaseSizeBeforeDelete = creditProposalRepository.findAll().size();

        // Get the creditProposal
        restCreditProposalMockMvc.perform(delete("/api/credit-proposals/{id}", creditProposal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CreditProposal.class);
        CreditProposal creditProposal1 = new CreditProposal();
        creditProposal1.setId(1L);
        CreditProposal creditProposal2 = new CreditProposal();
        creditProposal2.setId(creditProposal1.getId());
        assertThat(creditProposal1).isEqualTo(creditProposal2);
        creditProposal2.setId(2L);
        assertThat(creditProposal1).isNotEqualTo(creditProposal2);
        creditProposal1.setId(null);
        assertThat(creditProposal1).isNotEqualTo(creditProposal2);
    }

    @Test
    @Transactional
    public void test01() throws Exception {
//    	Lucas 	28 	M 	solteiro 	SC 	0 	2500 	Aprovado 	entre 500 - 1000
        int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();
        CreditProposal test = new CreditProposal().clientName("Lucas").clientAge(28).gender(Gender.MALE)
        		.maritalStatus(MaritalStatus.SINGLE).federationUnit(FederationUnit.SC).dependents(0)
        		.income(new BigDecimal(2500)).taxpayerId("12345678900");
        // Create the CreditProposal
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(test)))
            .andExpect(status().isCreated());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate + 1);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getStatus()).isEqualTo(CreditProposalStatus.APROVED);
        assertThat(testCreditProposal.getAprovedMin().intValue()).isEqualTo(500);
        assertThat(testCreditProposal.getAprovedMax().intValue()).isEqualTo(1000);
    }

    @Test
    @Transactional
    public void test02() throws Exception {
//    	Ana 	17 	F 	solteiro 	SP 	0 	1000 	Aprovado 	entre 100 - 500
        int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();
        CreditProposal test = new CreditProposal().clientName("Ana").clientAge(17).gender(Gender.FEMALE)
        		.maritalStatus(MaritalStatus.SINGLE).federationUnit(FederationUnit.SP).dependents(0)
        		.income(new BigDecimal(1000)).taxpayerId("12345678900");
        // Create the CreditProposal
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(test)))
            .andExpect(status().isCreated());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate + 1);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getStatus()).isEqualTo(CreditProposalStatus.APROVED);
        assertThat(testCreditProposal.getAprovedMin().intValue()).isEqualTo(100);
        assertThat(testCreditProposal.getAprovedMax().intValue()).isEqualTo(500);
    }

    @Test
    @Transactional
    public void test03() throws Exception {
//    	Pedro 	68 	M 	casado 	SC 	3 	8000 	Aprovado 	entre 1500 - 2000
        int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();
        CreditProposal test = new CreditProposal().clientName("Pedro").clientAge(68).gender(Gender.MALE)
        		.maritalStatus(MaritalStatus.MARRIED).federationUnit(FederationUnit.SC).dependents(3)
        		.income(new BigDecimal(8000)).taxpayerId("12345678900");
        // Create the CreditProposal
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(test)))
            .andExpect(status().isCreated());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate + 1);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getStatus()).isEqualTo(CreditProposalStatus.APROVED);
        assertThat(testCreditProposal.getAprovedMin().intValue()).isEqualTo(1500);
        assertThat(testCreditProposal.getAprovedMax().intValue()).isEqualTo(2000);
    }

    @Test
    @Transactional
    public void test04() throws Exception {
//		Paula 	61 	F 	casado 	RJ 	3 	5000 	Aprovado 	entre 1000 - 1500
    	int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();
        CreditProposal test = new CreditProposal().clientName("Paula").clientAge(61).gender(Gender.FEMALE)
        		.maritalStatus(MaritalStatus.MARRIED).federationUnit(FederationUnit.RJ).dependents(3)
        		.income(new BigDecimal(5000)).taxpayerId("12345678900");
        // Create the CreditProposal
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(test)))
            .andExpect(status().isCreated());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate + 1);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getStatus()).isEqualTo(CreditProposalStatus.APROVED);
        assertThat(testCreditProposal.getAprovedMin().intValue()).isEqualTo(1000);
        assertThat(testCreditProposal.getAprovedMax().intValue()).isEqualTo(1500);
    }

    @Test
    @Transactional
    public void test05() throws Exception {
//    	João 	56 	M 	divorciado 	RJ 	2 	2000 	Negado	reprovado pela política de crédito
    	int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();
        CreditProposal test = new CreditProposal().clientName("João").clientAge(56).gender(Gender.MALE)
        		.maritalStatus(MaritalStatus.DIVORCED).federationUnit(FederationUnit.RJ).dependents(2)
        		.income(new BigDecimal(2000)).taxpayerId("12345678900");
        // Create the CreditProposal
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(test)))
            .andExpect(status().isCreated());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate + 1);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getStatus()).isEqualTo(CreditProposalStatus.REJECTED);
        assertThat(testCreditProposal.getRejectionReason()).isEqualTo(RejectionReason.POLICY);
    }

    @Test
    @Transactional
    public void test06() throws Exception {
//    	Maria 	45 	F 	divorciado 	SP 	1 	2000 	Negado 	reprovado pela política de crédito
    	int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();
        CreditProposal test = new CreditProposal().clientName("Maria").clientAge(45).gender(Gender.FEMALE)
        		.maritalStatus(MaritalStatus.DIVORCED).federationUnit(FederationUnit.SP).dependents(1)
        		.income(new BigDecimal(2000)).taxpayerId("12345678900");
        // Create the CreditProposal
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(test)))
            .andExpect(status().isCreated());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate + 1);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getStatus()).isEqualTo(CreditProposalStatus.REJECTED);
        assertThat(testCreditProposal.getRejectionReason()).isEqualTo(RejectionReason.POLICY);
    }

    @Test
    @Transactional
    public void test07() throws Exception {
//    	José 	30 	M 	casado 	MA 	2 	8000 	Aprovado 	superior 2000
    	int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();
        CreditProposal test = new CreditProposal().clientName("José").clientAge(30).gender(Gender.MALE)
        		.maritalStatus(MaritalStatus.MARRIED).federationUnit(FederationUnit.MA).dependents(2)
        		.income(new BigDecimal(8000)).taxpayerId("12345678900");
        // Create the CreditProposal
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(test)))
            .andExpect(status().isCreated());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate + 1);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getStatus()).isEqualTo(CreditProposalStatus.APROVED);
        assertThat(testCreditProposal.getAprovedMin().intValue()).isEqualTo(2000);
        assertThat(testCreditProposal.getAprovedMax()).isNull();
    }

    @Test
    @Transactional
    public void test08() throws Exception {
//    	Dinae 	33 	F 	casado 	SP 	1 	10000 	Aprovado 	superior 2000
    	int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();
        CreditProposal test = new CreditProposal().clientName("Dinae").clientAge(33).gender(Gender.FEMALE)
        		.maritalStatus(MaritalStatus.MARRIED).federationUnit(FederationUnit.SP).dependents(1)
        		.income(new BigDecimal(10000)).taxpayerId("12345678900");
        // Create the CreditProposal
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(test)))
            .andExpect(status().isCreated());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate + 1);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getStatus()).isEqualTo(CreditProposalStatus.APROVED);
        assertThat(testCreditProposal.getAprovedMin().intValue()).isEqualTo(2000);
        assertThat(testCreditProposal.getAprovedMax()).isNull();
    }

    @Test
    @Transactional
    public void test09() throws Exception {
//      Marcos 	19 	M 	solteiro 	SC 	1 	400 	Negado 	renda baixa
    	int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();
        CreditProposal test = new CreditProposal().clientName("Marcos").clientAge(19).gender(Gender.MALE)
        		.maritalStatus(MaritalStatus.SINGLE).federationUnit(FederationUnit.SC).dependents(1)
        		.income(new BigDecimal(400)).taxpayerId("12345678900");
        // Create the CreditProposal
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(test)))
            .andExpect(status().isCreated());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate + 1);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getStatus()).isEqualTo(CreditProposalStatus.REJECTED);
        assertThat(testCreditProposal.getRejectionReason()).isEqualTo(RejectionReason.INCOME);
    }

    @Test
    @Transactional
    public void test10() throws Exception {
//    	Suzan 	63 	F 	viuva 	MA 	3 	1500 	Negado 	reprovado pela política de crédito
    	int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();
        CreditProposal test = new CreditProposal().clientName("Suzan").clientAge(63).gender(Gender.FEMALE)
        		.maritalStatus(MaritalStatus.WIDOW).federationUnit(FederationUnit.MA).dependents(3)
        		.income(new BigDecimal(1500)).taxpayerId("12345678900");
        // Create the CreditProposal
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(test)))
            .andExpect(status().isCreated());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate + 1);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getStatus()).isEqualTo(CreditProposalStatus.REJECTED);
        assertThat(testCreditProposal.getRejectionReason()).isEqualTo(RejectionReason.POLICY);
    }

    @Test
    @Transactional
    public void test11() throws Exception {
//      Luci 	28 	F 	solteiro 	SC 	2 	2500 	Aprovado 	entre 100 - 500
    	int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();
        CreditProposal test = new CreditProposal().clientName("Luci").clientAge(28).gender(Gender.FEMALE)
        		.maritalStatus(MaritalStatus.SINGLE).federationUnit(FederationUnit.SC).dependents(2)
        		.income(new BigDecimal(2500)).taxpayerId("12345678900");
        // Create the CreditProposal
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(test)))
            .andExpect(status().isCreated());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate + 1);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getStatus()).isEqualTo(CreditProposalStatus.APROVED);
        assertThat(testCreditProposal.getAprovedMin().intValue()).isEqualTo(100);
        assertThat(testCreditProposal.getAprovedMax().intValue()).isEqualTo(500);
    }

    @Test
    @Transactional
    public void test12() throws Exception {
//      Roberto 	16 	M 	solteiro 	SP 	0 	500 	Negado 	renda baixa
    	int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();
        CreditProposal test = new CreditProposal().clientName("Roberto").clientAge(16).gender(Gender.MALE)
        		.maritalStatus(MaritalStatus.SINGLE).federationUnit(FederationUnit.SP).dependents(0)
        		.income(new BigDecimal(500)).taxpayerId("12345678900");
        // Create the CreditProposal
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(test)))
            .andExpect(status().isCreated());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate + 1);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getStatus()).isEqualTo(CreditProposalStatus.REJECTED);
        assertThat(testCreditProposal.getRejectionReason()).isEqualTo(RejectionReason.INCOME);
    }

    @Test
    @Transactional
    public void test13() throws Exception {
//      Bruno 	30 	M 	casado 	MA 	5 	8000 	Aprovado 	entre 1000 - 1500
    	int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();
        CreditProposal test = new CreditProposal().clientName("Bruno").clientAge(30).gender(Gender.MALE)
        		.maritalStatus(MaritalStatus.MARRIED).federationUnit(FederationUnit.MA).dependents(5)
        		.income(new BigDecimal(8000)).taxpayerId("12345678900");
        // Create the CreditProposal
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(test)))
            .andExpect(status().isCreated());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate + 1);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getStatus()).isEqualTo(CreditProposalStatus.APROVED);
        assertThat(testCreditProposal.getAprovedMin().intValue()).isEqualTo(1000);
        assertThat(testCreditProposal.getAprovedMax().intValue()).isEqualTo(1500);
    }

    @Test
    @Transactional
    public void test14() throws Exception {
//      Ariel 	33 	F 	viuva 	SP 	0 	10000 	Aprovado 	superior 2000
    	int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();
        CreditProposal test = new CreditProposal().clientName("Ariel").clientAge(33).gender(Gender.FEMALE)
        		.maritalStatus(MaritalStatus.WIDOW).federationUnit(FederationUnit.SP).dependents(0)
        		.income(new BigDecimal(10000)).taxpayerId("12345678900");
        // Create the CreditProposal
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(test)))
            .andExpect(status().isCreated());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate + 1);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getStatus()).isEqualTo(CreditProposalStatus.APROVED);
        assertThat(testCreditProposal.getAprovedMin().intValue()).isEqualTo(2000);
        assertThat(testCreditProposal.getAprovedMax()).isNull();
    }
}
