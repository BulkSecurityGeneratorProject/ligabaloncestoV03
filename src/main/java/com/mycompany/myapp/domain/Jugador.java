package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Jugador.
 */
@Entity
@Table(name = "jugador")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Jugador implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nombre_jugador")
    private String nombreJugador;

    @Column(name = "apellidos_jugador")
    private String apellidosJugador;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "numero_totalcanastas")
    private Integer numeroTotalcanastas;

    @Column(name = "numero_totalasistencias")
    private Integer numeroTotalasistencias;

    @Column(name = "numero_totalrebotes")
    private Integer numeroTotalrebotes;

    @Column(name = "posicion_campo")
    private String posicionCampo;

    @Column(name = "pais")
    private String pais;

    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public String getApellidosJugador() {
        return apellidosJugador;
    }

    public void setApellidosJugador(String apellidosJugador) {
        this.apellidosJugador = apellidosJugador;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getNumeroTotalcanastas() {
        return numeroTotalcanastas;
    }

    public void setNumeroTotalcanastas(Integer numeroTotalcanastas) {
        this.numeroTotalcanastas = numeroTotalcanastas;
    }

    public Integer getNumeroTotalasistencias() {
        return numeroTotalasistencias;
    }

    public void setNumeroTotalasistencias(Integer numeroTotalasistencias) {
        this.numeroTotalasistencias = numeroTotalasistencias;
    }

    public Integer getNumeroTotalrebotes() {
        return numeroTotalrebotes;
    }

    public void setNumeroTotalrebotes(Integer numeroTotalrebotes) {
        this.numeroTotalrebotes = numeroTotalrebotes;
    }

    public String getPosicionCampo() {
        return posicionCampo;
    }

    public void setPosicionCampo(String posicionCampo) {
        this.posicionCampo = posicionCampo;
    }

    public String getPais() {return pais;}

    public void setPais (String pais) { this.pais = pais;}

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Jugador jugador = (Jugador) o;
        return Objects.equals(id, jugador.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Jugador{" +
            "id=" + id +
            ", nombreJugador='" + nombreJugador + "'" +
            ", apellidosJugador='" + apellidosJugador + "'" +
            ", fechaNacimiento='" + fechaNacimiento + "'" +
            ", numeroTotalcanastas='" + numeroTotalcanastas + "'" +
            ", numeroTotalasistencias='" + numeroTotalasistencias + "'" +
            ", numeroTotalrebotes='" + numeroTotalrebotes + "'" +
            ", posicionCampo='" + posicionCampo + "'" +
            ", pais='" + pais + "'" +
            '}';
    }
}
