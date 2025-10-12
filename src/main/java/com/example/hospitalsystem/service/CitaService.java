package com.example.hospitalsystem.service;
import com.example.hospitalsystem.model.Cita;
import com.example.hospitalsystem.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    public List<Cita> getAllCitas() {
        return citaRepository.findAll();
    }

    public Optional<Cita> getCitaById(Long id) {
        return citaRepository.findById(id);
    }

    public List<Cita> getCitasByPaciente(Long idPaciente) {
        return citaRepository.findByIdPaciente(idPaciente);
    }

    public List<Cita> getCitasByMedico(Long idMedico) {
        return citaRepository.findByIdMedico(idMedico);
    }

    public List<Cita> getCitasByFecha(LocalDate fecha) {
        return citaRepository.findByFecha(fecha);
    }

    public Cita createCita(Cita cita) {
        return citaRepository.save(cita);
    }

    public Cita updateCita(Long id, Cita cita) {
        if (citaRepository.existsById(id)) {
            cita.setIdCita(id);
            return citaRepository.save(cita);
        }
        return null;
    }

    public Cita cambiarEstado(Long id, String nuevoEstado) {
        Optional<Cita> citaOpt = citaRepository.findById(id);
        if (citaOpt.isPresent()) {
            Cita cita = citaOpt.get();
            cita.setEstado(nuevoEstado);
            return citaRepository.save(cita);
        }
        return null;
    }

    public boolean deleteCita(Long id) {
        if (citaRepository.existsById(id)) {
            citaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
