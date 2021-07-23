package com.sample.beerstock.repository;

import com.sample.beerstock.entity.Cerveja;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CervejaRepositorio extends JpaRepository<Cerveja, Long> {
    // Só reconhece se o campo for "name"
    Optional<Cerveja> findByName(String name);
}
