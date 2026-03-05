package com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;
    private Integer anoNacimiento;
    private Integer anoFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

    public Autor() { }

    public Autor(String nombre, Integer anoNacimiento, Integer anoFallecimiento) {
        this.nombre = nombre;
        this.anoNacimiento = anoNacimiento;
        this.anoFallecimiento = anoFallecimiento;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getAnoNacimiento() { return anoNacimiento; }
    public void setAnoNacimiento(Integer anoNacimiento) { this.anoNacimiento = anoNacimiento; }

    public Integer getAnoFallecimiento() { return anoFallecimiento; }
    public void setAnoFallecimiento(Integer anoFallecimiento) { this.anoFallecimiento = anoFallecimiento; }

    public List<Libro> getLibros() { return libros; }
    public void setLibros(List<Libro> libros) { this.libros = libros; }

    @Override
    public String toString() {
        String librosRegistrados = libros.stream()
                .map(Libro::getTitulo)
                .collect(Collectors.joining(", "));
        return "---------- AUTOR ----------\n" +
                "Nombre: " + nombre + "\n" +
                "Fecha de Nacimiento: " + (anoNacimiento != null ? anoNacimiento : "Desconocido") + "\n" +
                "Fecha de Fallecimiento: " + (anoFallecimiento != null ? anoFallecimiento : "Desconocido") + "\n" +
                "Libros registrados: [" + (librosRegistrados.isEmpty() ? "Ninguno" : librosRegistrados) + "]\n" +
                "---------------------------\n";
    }
}
