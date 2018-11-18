package com.bisol.calcard.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisol.calcard.domain.CreditProposal;
import com.bisol.calcard.domain.enumeration.CreditProposalStatus;
import com.bisol.calcard.domain.enumeration.Gender;
import com.bisol.calcard.domain.enumeration.RejectionReason;
import com.bisol.calcard.repository.CreditProposalRepository;

/**
 * Service Implementation for managing CreditProposal.
 */
@Service
@Transactional
public class CreditProposalService {

	private static final BigDecimal MIN_INCOME = new BigDecimal(500);
	private static final long MIN_SCORE = 1000;
	private static final long DEPENDENT_SCORE = 700;
	private static final long MARITAL_STATUS_SCORE = 600;

    private final Logger log = LoggerFactory.getLogger(CreditProposalService.class);

    private final CreditProposalRepository creditProposalRepository;

    public CreditProposalService(CreditProposalRepository creditProposalRepository) {
        this.creditProposalRepository = creditProposalRepository;
    }

    /**
     * Save a creditProposal.
     *
     * @param creditProposal the entity to save
     * @return the persisted entity
     */
    public CreditProposal save(CreditProposal creditProposal) {
        log.debug("Request to save CreditProposal : {}", creditProposal);
        
        if (creditProposal.getId() == null) {
        	creditProposal = creditProposalRepository.save(creditProposal);
        	creditProposal = process(creditProposal);
        }

        return creditProposalRepository.save(creditProposal);
    }

	/**
	 * Approves or rejects credit proposals, based on hand crafted criteria to fit
	 * provided test data.
	 * 
	 * @param creditProposal
	 * @return modified creditProposal
	 */
    private CreditProposal process(CreditProposal creditProposal) {
    	if (creditProposal.getIncome().compareTo(MIN_INCOME) <= 0) {
    		return reject(creditProposal, RejectionReason.INCOME);
    	}

    	long score = creditProposal.getIncome().longValue();
    	
    	score -= creditProposal.getDependents() * DEPENDENT_SCORE;

    	switch (creditProposal.getMaritalStatus()) {
    	case SINGLE:
    		if (creditProposal.getDependents() > 0) {
        		score -= 100;
    		}
		case MARRIED:
			break;
		default:
			score -= MARITAL_STATUS_SCORE;
		}
    	
    	if (creditProposal.getGender() == Gender.MALE) {
    		score -= 100;
    	}

    	if (score < MIN_SCORE) {
    		return reject(creditProposal, RejectionReason.POLICY);
    	}
    	
		creditProposal.setStatus(CreditProposalStatus.APROVED);
		creditProposal.setProcessingDate(LocalDate.now());
		
		if (score < 1500) {
			creditProposal.setAprovedMin(new BigDecimal(100));
			creditProposal.setAprovedMax(new BigDecimal(500));
		} else if (score < 2500) {
			creditProposal.setAprovedMin(new BigDecimal(500));
			creditProposal.setAprovedMax(new BigDecimal(1000));
		} else if (score < 4500) {
			creditProposal.setAprovedMin(new BigDecimal(1000));
			creditProposal.setAprovedMax(new BigDecimal(1500));
		} else if (score < 6500) {
			creditProposal.setAprovedMin(new BigDecimal(1500));
			creditProposal.setAprovedMax(new BigDecimal(2000));
		} else {
			creditProposal.setAprovedMin(new BigDecimal(2000));
		}
    	return creditProposal;
    }
    
    private CreditProposal reject(CreditProposal creditProposal, RejectionReason reason) {
		creditProposal.setRejectionReason(reason);
		creditProposal.setStatus(CreditProposalStatus.REJECTED);
		creditProposal.setProcessingDate(LocalDate.now());
    	return creditProposal;
    }

    /**
	 * Get all the creditProposals matching the given CPF. If CPF is not provided,
	 * return all entities.
	 * 
	 * @param cpf
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
    @Transactional(readOnly = true)
    public Page<CreditProposal> findAll(String cpf, Pageable pageable) {
        log.debug("Request to get all CreditProposals with CPF={}", cpf);
        if (cpf == null || cpf.isEmpty()) {
        	return creditProposalRepository.findAll(pageable);
        } else {
        	return creditProposalRepository.findByTaxpayerId(cpf, pageable);
        }
    }


    /**
     * Get one creditProposal by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<CreditProposal> findOne(Long id) {
        log.debug("Request to get CreditProposal : {}", id);
        return creditProposalRepository.findById(id);
    }

    /**
     * Delete the creditProposal by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CreditProposal : {}", id);
        creditProposalRepository.deleteById(id);
    }
}
