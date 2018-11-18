package com.bisol.calcard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bisol.calcard.domain.CreditProposal;


/**
 * Spring Data  repository for the CreditProposal entity.
 */
@Repository
public interface CreditProposalRepository extends JpaRepository<CreditProposal, Long> {

	Page<CreditProposal> findByTaxpayerId(String cpf, Pageable pageable);
}
