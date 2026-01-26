package com.Risk.Repository;

import com.Risk.Entity.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione,Integer> {

    @Query("SELECT p FROM Prenotazione p ORDER BY p.DataAppuntamento DESC")
    List<Prenotazione> findAllOrderByDataAppuntamento();
}
