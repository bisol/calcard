package com.bisol.calcard.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bisol.calcard.domain.CreditProposal;
import com.bisol.calcard.service.CreditProposalService;
import com.bisol.calcard.web.rest.errors.BadRequestAlertException;
import com.bisol.calcard.web.rest.util.HeaderUtil;
import com.bisol.calcard.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing CreditProposal.
 */
@RestController
@RequestMapping("/api")
public class CreditProposalResource {

    private final Logger log = LoggerFactory.getLogger(CreditProposalResource.class);

    private static final String ENTITY_NAME = "creditProposal";

    private final CreditProposalService creditProposalService;

    public CreditProposalResource(CreditProposalService creditProposalService) {
        this.creditProposalService = creditProposalService;
    }

    /**
     * POST  /credit-proposals : Create a new creditProposal.
     *
     * @param creditProposal the creditProposal to create
     * @return the ResponseEntity with status 201 (Created) and with body the new creditProposal, or with status 400 (Bad Request) if the creditProposal has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/credit-proposals")
    @Timed
    public ResponseEntity<CreditProposal> createCreditProposal(@Valid @RequestBody CreditProposal creditProposal) throws URISyntaxException {
        log.debug("REST request to save CreditProposal : {}", creditProposal);
        if (creditProposal.getId() != null) {
            throw new BadRequestAlertException("A new creditProposal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CreditProposal result = creditProposalService.save(creditProposal);
        return ResponseEntity.created(new URI("/api/credit-proposals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /credit-proposals : Updates an existing creditProposal.
     *
     * @param creditProposal the creditProposal to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated creditProposal,
     * or with status 400 (Bad Request) if the creditProposal is not valid,
     * or with status 500 (Internal Server Error) if the creditProposal couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/credit-proposals")
    @Timed
    public ResponseEntity<CreditProposal> updateCreditProposal(@Valid @RequestBody CreditProposal creditProposal) throws URISyntaxException {
        log.debug("REST request to update CreditProposal : {}", creditProposal);
        if (creditProposal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CreditProposal result = creditProposalService.save(creditProposal);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, creditProposal.getId().toString()))
            .body(result);
    }

    /**
     * GET  /credit-proposals : get all the creditProposals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of creditProposals in body
     */
    @GetMapping("/credit-proposals")
    @Timed
    public ResponseEntity<List<CreditProposal>> getAllCreditProposals(String cpf, Pageable pageable) {
        log.debug("REST request to get a page of CreditProposals with CPF={}", cpf);
        Page<CreditProposal> page = creditProposalService.findAll(cpf, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/credit-proposals");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /credit-proposals/:id : get the "id" creditProposal.
     *
     * @param id the id of the creditProposal to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the creditProposal, or with status 404 (Not Found)
     */
    @GetMapping("/credit-proposals/{id}")
    @Timed
    public ResponseEntity<CreditProposal> getCreditProposal(@PathVariable Long id) {
        log.debug("REST request to get CreditProposal : {}", id);
        Optional<CreditProposal> creditProposal = creditProposalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(creditProposal);
    }

    /**
     * DELETE  /credit-proposals/:id : delete the "id" creditProposal.
     *
     * @param id the id of the creditProposal to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/credit-proposals/{id}")
    @Timed
    public ResponseEntity<Void> deleteCreditProposal(@PathVariable Long id) {
        log.debug("REST request to delete CreditProposal : {}", id);
        creditProposalService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
