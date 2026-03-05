package com.alura.literalura.principal;

import com.alura.literalura.dto.DatosAutor;
import com.alura.literalura.dto.DatosLibro;
import com.alura.literalura.dto.DatosRespuestaAPI;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    \n=== LITERALURA: CATÁLOGO DE LIBROS ===
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Top 10 libros más descargados
                    
                    0 - Salir
                    ========================================
                    Elija una opción válida:""";

            System.out.println(menu);

            try {
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
                teclado.nextLine();
                continue;
            }

            switch (opcion) {
                case 1 -> buscarLibro();
                case 2 -> listarLibrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivos();
                case 5 -> listarLibrosPorIdioma();
                case 6 -> top10Libros();
                case 0 -> System.out.println("Cerrando la aplicación... ¡Adios!");
                default -> System.out.println("Opción inválida. Intente de nuevo.");
            }
        }
    }

    // Buscar un libro en la base de datos
    private void buscarLibro() {
        System.out.println("Ingresa el nombre del libro que desea buscar:");
        String tituloLibro = teclado.nextLine();

        try {
            String tituloCodificado = URLEncoder.encode(tituloLibro, StandardCharsets.UTF_8);
            String json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloCodificado);
            DatosRespuestaAPI datosBusqueda = conversor.obtenerDatos(json, DatosRespuestaAPI.class);
            Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream().findFirst();

            if (libroBuscado.isPresent()) {
                DatosLibro datosLibro = libroBuscado.get();

                Optional<Libro> libroExistente = libroRepository.findByTituloIgnoreCase(datosLibro.titulo());
                if (libroExistente.isPresent()) {
                    System.out.println("\n El libro ya está registrado en nuestra base de datos. No se puede duplicar.");
                    return;
                }

                System.out.println("\nLibro encontrado en Gutendex! Procesando guardado...");

                Autor autor;

                if (datosLibro.autores().isEmpty()) {
                    Optional<Autor> autorAnonimo = autorRepository.findByNombreIgnoreCase("Anónimo");
                    if (autorAnonimo.isPresent()) {
                        autor = autorAnonimo.get();
                    } else {
                        autor = new Autor("Anónimo", null, null);
                        autorRepository.save(autor);
                    }
                } else {
                    DatosAutor datosAutor = datosLibro.autores().get(0);
                    Optional<Autor> autorExistente = autorRepository.findByNombreIgnoreCase(datosAutor.nombre());

                    if (autorExistente.isPresent()) {
                        autor = autorExistente.get();
                    } else {
                        autor = new Autor(datosAutor.nombre(), datosAutor.anoNacimiento(), datosAutor.anoFallecimiento());
                        autorRepository.save(autor);
                    }
                }

                String idioma = datosLibro.idiomas().isEmpty() ? "Desconocido" : datosLibro.idiomas().get(0);

                Libro libro = new Libro(datosLibro.titulo(), autor, idioma, datosLibro.numeroDeDescargas());
                libroRepository.save(libro);

                System.out.println("¡Libro guardado exitosamente!\n");
                System.out.println(libro);

            } else {
                System.out.println("\n Libro no encontrado en la API de Gutendex.");
            }
        } catch (Exception e) {
            System.out.println("\n Ocurrió un error inesperado al buscar el libro: " + e.getMessage());
        }
    }

    // Mostrar los libros registrados en la base de datos
    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("\n No hay libros registrados en la base de datos.");
        } else {
            System.out.println("\n--- LISTA DE LIBROS REGISTRADOS ---");
            libros.forEach(System.out::println);
            System.out.println("\n-----------------------------------");
        }
    }

    // Mostrar los autores registrados en la base de datos
    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("\n No hay autores registrados en la base de datos.");
        } else {
            System.out.println("\n--- LISTA DE AUTORES REGISTRADOS ---");
            autores.forEach(System.out::println);
            System.out.println("\n-----------------------------------");
        }
    }

    // Mostrar lista de autores vivos en determinado año
    private void listarAutoresVivos() {
        System.out.println("Ingrese el año para consultar qué autores estaban vivos:");
        try {
            int ano = teclado.nextInt();
            teclado.nextLine();

            List<Autor> autoresVivos = autorRepository.buscarAutoresVivosEnAno(ano);

            if (autoresVivos.isEmpty()) {
                System.out.println("\n No se encontraron autores vivos en el año " + ano + " en nuestra base de datos.");
            } else {
                System.out.println("\n Autores vivos en " + ano + ": ");
                autoresVivos.forEach(System.out::println);
                System.out.println("\n--------------------------------");
            }
        } catch (InputMismatchException e) {
            System.out.println("\n Error: Debe ingresar un año numérico válido.");
            teclado.nextLine();
        }
    }

    // Mostrar lista de libros por idioma
    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingrese el idioma para buscar los libros:
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """);
        String idioma = teclado.nextLine().toLowerCase();

        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("\n No hay libros registrados en el idioma '" + idioma + "'.");
        } else {
            System.out.println("\n--- LIBROS EN EL IDIOMA '" + idioma.toUpperCase() + "' ---");
            librosPorIdioma.forEach(System.out::println);
            System.out.println("\n-----------------------------------");
        }
    }

    // Top 10 de libros más descargados
    private void top10Libros() {
        List<Libro> top10 = libroRepository.findTop10ByOrderByNumeroDeDescargasDesc();

        if (top10.isEmpty()) {
            System.out.println("\n No hay libros suficientes en la base de datos.");
        } else {
            System.out.println("\n--- TOP 10 LIBROS MÁS DESCARGADOS ---");
            top10.forEach(System.out::println);
        }
    }
}
