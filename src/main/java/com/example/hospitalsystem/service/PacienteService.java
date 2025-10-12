package com.example.hospitalsystem.service;

import com.example.hospitalsystem.model.AntecedenteMedico;
import com.example.hospitalsystem.model.HistoriaClinica;
import com.example.hospitalsystem.model.Paciente;
import com.example.hospitalsystem.repository.AntecedenteMedicoRepository;
import com.example.hospitalsystem.repository.HistoriaClinicaRepository;
import com.example.hospitalsystem.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los pacientes,
 * sus historias clínicas y antecedentes médicos.
 */
@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;

    @Autowired
    private AntecedenteMedicoRepository antecedenteMedicoRepository;

    /**
     * Obtiene todos los pacientes registrados.
     */
    public List<Paciente> getAllPacientes() {
        return pacienteRepository.findAll();
    }

    /**
     * Busca un paciente por su ID.
     */
    public Optional<Paciente> getPacienteById(Long id) {
        return pacienteRepository.findById(id);
    }

    /**
     * Busca un paciente por su número de DNI.
     */
    public Optional<Paciente> getPacienteByDni(String dni) {
        return pacienteRepository.findByDni(dni);
    }

    /**
     * Crea un nuevo paciente y genera automáticamente su historia clínica.
     */
    @Transactional
    public Paciente createPaciente(Paciente paciente) {
        // Guardar el paciente
        Paciente savedPaciente = pacienteRepository.save(paciente);

        // Crear automáticamente una historia clínica asociada
        HistoriaClinica historia = new HistoriaClinica();
        historia.setIdPaciente(savedPaciente.getIdPaciente());
        historia.setFechaApertura(LocalDate.now());
        historia.setObservaciones("Historia clínica creada automáticamente");

        historiaClinicaRepository.save(historia);

        return savedPaciente;
    }

    /**
     * Actualiza los datos de un paciente existente.
     */
    public Paciente updatePaciente(Long id, Paciente paciente) {
        if (pacienteRepository.existsById(id)) {
            paciente.setIdPaciente(id);
            return pacienteRepository.save(paciente);
        }
        return null;
    }

    /**
     * Elimina un paciente por su ID.
     */
    public boolean deletePaciente(Long id) {
        if (pacienteRepository.existsById(id)) {
            pacienteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Obtiene la historia clínica asociada a un paciente.
     */
    public Optional<HistoriaClinica> getHistoriaClinicaByPaciente(Long idPaciente) {
        return historiaClinicaRepository.findByIdPaciente(idPaciente);
    }

    /**
     * Obtiene los antecedentes médicos asociados a una historia clínica.
     */
    public List<AntecedenteMedico> getAntecedentesByHistoria(Long idHistoria) {
        return antecedenteMedicoRepository.findByIdHistoria(idHistoria);
    }

    /**
     * Agrega un nuevo antecedente médico a una historia clínica.
     */
    public AntecedenteMedico addAntecedenteMedico(AntecedenteMedico antecedente) {
        return antecedenteMedicoRepository.save(antecedente);
    }
}
