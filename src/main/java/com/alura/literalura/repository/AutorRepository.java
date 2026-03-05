package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombreIgnoreCase(String nombre);
    List<Autor> findByAnoNacimientoLessThanEqualAndAnoFallecimientoGreaterThanEqual(Integer anoNacimiento, Integer anoFallecimiento);
    @Query("SELECT a FROM Autor a WHERE a.anoNacimiento <= :ano AND (a.anoFallecimiento >= :ano OR a.anoFallecimiento IS NULL)")
    List<Autor> buscarAutoresVivosEnAno(Integer ano);
}
