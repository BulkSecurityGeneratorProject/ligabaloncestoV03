package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Equipo;
import com.mycompany.myapp.repository.EquipoRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Equipo.
 */
@RestController
@RequestMapping("/api")
public class EquipoResource {

    private final Logger log = LoggerFactory.getLogger(EquipoResource.class);
        
    @Inject
    private EquipoRepository equipoRepository;
    
    /**
     * POST  /equipos -> Create a new equipo.
     */
    @RequestMapping(value = "/equipos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Equipo> createEquipo(@RequestBody Equipo equipo) throws URISyntaxException {
        log.debug("REST request to save Equipo : {}", equipo);
        if (equipo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("equipo", "idexists", "A new equipo cannot already have an ID")).body(null);
        }
        Equipo result = equipoRepository.save(equipo);
        return ResponseEntity.created(new URI("/api/equipos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("equipo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /equipos -> Updates an existing equipo.
     */
    @RequestMapping(value = "/equipos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Equipo> updateEquipo(@RequestBody Equipo equipo) throws URISyntaxException {
        log.debug("REST request to update Equipo : {}", equipo);
        if (equipo.getId() == null) {
            return createEquipo(equipo);
        }
        Equipo result = equipoRepository.save(equipo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("equipo", equipo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /equipos -> get all the equipos.
     */
    @RequestMapping(value = "/equipos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Equipo>> getAllEquipos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Equipos");
        Page<Equipo> page = equipoRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/equipos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /equipos/:id -> get the "id" equipo.
     */
    @RequestMapping(value = "/equipos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Equipo> getEquipo(@PathVariable Long id) {
        log.debug("REST request to get Equipo : {}", id);
        Equipo equipo = equipoRepository.findOne(id);
        return Optional.ofNullable(equipo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /equipos/:id -> delete the "id" equipo.
     */
    @RequestMapping(value = "/equipos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEquipo(@PathVariable Long id) {
        log.debug("REST request to delete Equipo : {}", id);
        equipoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("equipo", id.toString())).build();
    }
}
