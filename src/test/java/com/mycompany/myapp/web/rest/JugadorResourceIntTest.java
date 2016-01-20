package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Jugador;
import com.mycompany.myapp.repository.JugadorRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the JugadorResource REST controller.
 *
 * @see JugadorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class JugadorResourceIntTest {

    private static final String DEFAULT_NOMBRE_JUGADOR = "AAAAA";
    private static final String UPDATED_NOMBRE_JUGADOR = "BBBBB";
    private static final String DEFAULT_APELLIDOS_JUGADOR = "AAAAA";
    private static final String UPDATED_APELLIDOS_JUGADOR = "BBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_NUMERO_TOTALCANASTAS = 1;
    private static final Integer UPDATED_NUMERO_TOTALCANASTAS = 2;

    private static final Integer DEFAULT_NUMERO_TOTALASISTENCIAS = 1;
    private static final Integer UPDATED_NUMERO_TOTALASISTENCIAS = 2;

    private static final Integer DEFAULT_NUMERO_TOTALREBOTES = 1;
    private static final Integer UPDATED_NUMERO_TOTALREBOTES = 2;
    private static final String DEFAULT_POSICION_CAMPO = "AAAAA";
    private static final String UPDATED_POSICION_CAMPO = "BBBBB";

    @Inject
    private JugadorRepository jugadorRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restJugadorMockMvc;

    private Jugador jugador;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JugadorResource jugadorResource = new JugadorResource();
        ReflectionTestUtils.setField(jugadorResource, "jugadorRepository", jugadorRepository);
        this.restJugadorMockMvc = MockMvcBuilders.standaloneSetup(jugadorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        jugador = new Jugador();
        jugador.setNombreJugador(DEFAULT_NOMBRE_JUGADOR);
        jugador.setApellidosJugador(DEFAULT_APELLIDOS_JUGADOR);
        jugador.setFechaNacimiento(DEFAULT_FECHA_NACIMIENTO);
        jugador.setNumeroTotalcanastas(DEFAULT_NUMERO_TOTALCANASTAS);
        jugador.setNumeroTotalasistencias(DEFAULT_NUMERO_TOTALASISTENCIAS);
        jugador.setNumeroTotalrebotes(DEFAULT_NUMERO_TOTALREBOTES);
        jugador.setPosicionCampo(DEFAULT_POSICION_CAMPO);
    }

    @Test
    @Transactional
    public void createJugador() throws Exception {
        int databaseSizeBeforeCreate = jugadorRepository.findAll().size();

        // Create the Jugador

        restJugadorMockMvc.perform(post("/api/jugadors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jugador)))
                .andExpect(status().isCreated());

        // Validate the Jugador in the database
        List<Jugador> jugadors = jugadorRepository.findAll();
        assertThat(jugadors).hasSize(databaseSizeBeforeCreate + 1);
        Jugador testJugador = jugadors.get(jugadors.size() - 1);
        assertThat(testJugador.getNombreJugador()).isEqualTo(DEFAULT_NOMBRE_JUGADOR);
        assertThat(testJugador.getApellidosJugador()).isEqualTo(DEFAULT_APELLIDOS_JUGADOR);
        assertThat(testJugador.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testJugador.getNumeroTotalcanastas()).isEqualTo(DEFAULT_NUMERO_TOTALCANASTAS);
        assertThat(testJugador.getNumeroTotalasistencias()).isEqualTo(DEFAULT_NUMERO_TOTALASISTENCIAS);
        assertThat(testJugador.getNumeroTotalrebotes()).isEqualTo(DEFAULT_NUMERO_TOTALREBOTES);
        assertThat(testJugador.getPosicionCampo()).isEqualTo(DEFAULT_POSICION_CAMPO);
    }

    @Test
    @Transactional
    public void getAllJugadors() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadors
        restJugadorMockMvc.perform(get("/api/jugadors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(jugador.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombreJugador").value(hasItem(DEFAULT_NOMBRE_JUGADOR.toString())))
                .andExpect(jsonPath("$.[*].apellidosJugador").value(hasItem(DEFAULT_APELLIDOS_JUGADOR.toString())))
                .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
                .andExpect(jsonPath("$.[*].numeroTotalcanastas").value(hasItem(DEFAULT_NUMERO_TOTALCANASTAS)))
                .andExpect(jsonPath("$.[*].numeroTotalasistencias").value(hasItem(DEFAULT_NUMERO_TOTALASISTENCIAS)))
                .andExpect(jsonPath("$.[*].numeroTotalrebotes").value(hasItem(DEFAULT_NUMERO_TOTALREBOTES)))
                .andExpect(jsonPath("$.[*].posicionCampo").value(hasItem(DEFAULT_POSICION_CAMPO.toString())));
    }

    @Test
    @Transactional
    public void getJugador() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get the jugador
        restJugadorMockMvc.perform(get("/api/jugadors/{id}", jugador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(jugador.getId().intValue()))
            .andExpect(jsonPath("$.nombreJugador").value(DEFAULT_NOMBRE_JUGADOR.toString()))
            .andExpect(jsonPath("$.apellidosJugador").value(DEFAULT_APELLIDOS_JUGADOR.toString()))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.numeroTotalcanastas").value(DEFAULT_NUMERO_TOTALCANASTAS))
            .andExpect(jsonPath("$.numeroTotalasistencias").value(DEFAULT_NUMERO_TOTALASISTENCIAS))
            .andExpect(jsonPath("$.numeroTotalrebotes").value(DEFAULT_NUMERO_TOTALREBOTES))
            .andExpect(jsonPath("$.posicionCampo").value(DEFAULT_POSICION_CAMPO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJugador() throws Exception {
        // Get the jugador
        restJugadorMockMvc.perform(get("/api/jugadors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJugador() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

		int databaseSizeBeforeUpdate = jugadorRepository.findAll().size();

        // Update the jugador
        jugador.setNombreJugador(UPDATED_NOMBRE_JUGADOR);
        jugador.setApellidosJugador(UPDATED_APELLIDOS_JUGADOR);
        jugador.setFechaNacimiento(UPDATED_FECHA_NACIMIENTO);
        jugador.setNumeroTotalcanastas(UPDATED_NUMERO_TOTALCANASTAS);
        jugador.setNumeroTotalasistencias(UPDATED_NUMERO_TOTALASISTENCIAS);
        jugador.setNumeroTotalrebotes(UPDATED_NUMERO_TOTALREBOTES);
        jugador.setPosicionCampo(UPDATED_POSICION_CAMPO);

        restJugadorMockMvc.perform(put("/api/jugadors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jugador)))
                .andExpect(status().isOk());

        // Validate the Jugador in the database
        List<Jugador> jugadors = jugadorRepository.findAll();
        assertThat(jugadors).hasSize(databaseSizeBeforeUpdate);
        Jugador testJugador = jugadors.get(jugadors.size() - 1);
        assertThat(testJugador.getNombreJugador()).isEqualTo(UPDATED_NOMBRE_JUGADOR);
        assertThat(testJugador.getApellidosJugador()).isEqualTo(UPDATED_APELLIDOS_JUGADOR);
        assertThat(testJugador.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testJugador.getNumeroTotalcanastas()).isEqualTo(UPDATED_NUMERO_TOTALCANASTAS);
        assertThat(testJugador.getNumeroTotalasistencias()).isEqualTo(UPDATED_NUMERO_TOTALASISTENCIAS);
        assertThat(testJugador.getNumeroTotalrebotes()).isEqualTo(UPDATED_NUMERO_TOTALREBOTES);
        assertThat(testJugador.getPosicionCampo()).isEqualTo(UPDATED_POSICION_CAMPO);
    }

    @Test
    @Transactional
    public void deleteJugador() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

		int databaseSizeBeforeDelete = jugadorRepository.findAll().size();

        // Get the jugador
        restJugadorMockMvc.perform(delete("/api/jugadors/{id}", jugador.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Jugador> jugadors = jugadorRepository.findAll();
        assertThat(jugadors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
