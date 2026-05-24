package pariwisata.service;

import java.util.List;
import java.util.UUID;

import pariwisata.dao.MedsosRepository;
import pariwisata.model.Medsos;

public class MedsosService {
    private MedsosRepository medsosRepository;

    public MedsosService() {
        this.medsosRepository = new MedsosRepository();
    }

    public boolean createMedsos(String destinationId, String platformName, String url) {
        if (destinationId == null || destinationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination ID tidak boleh kosong");
        }

        if (platformName == null || platformName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama platform tidak boleh kosong");
        }

        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL tidak boleh kosong");
        }

        String id = UUID.randomUUID().toString();
        Medsos medsos = new Medsos(id, destinationId, platformName, url);

        return medsosRepository.create(medsos);
    }

    public Medsos getMedsosById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Medsos ID tidak boleh kosong");
        }
        return medsosRepository.getById(id);
    }

    public List<Medsos> getAllMedsos() {
        return medsosRepository.getAll();
    }

    public boolean updateMedsos(String id, String platformName, String url) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Medsos ID tidak boleh kosong");
        }

        Medsos medsos = medsosRepository.getById(id);
        if (medsos == null) {
            throw new IllegalArgumentException("Medsos tidak ditemukan");
        }

        if (platformName != null && !platformName.trim().isEmpty()) {
            medsos.setPlatformName(platformName);
        }

        if (url != null && !url.trim().isEmpty()) {
            medsos.setUrl(url);
        }

        return medsosRepository.update(medsos);
    }

    public boolean deleteMedsos(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Medsos ID tidak boleh kosong");
        }

        Medsos medsos = medsosRepository.getById(id);
        if (medsos == null) {
            throw new IllegalArgumentException("Medsos tidak ditemukan");
        }

        return medsosRepository.delete(id);
    }

    public List<Medsos> getMedsosbyDestination(String destinationId) {
        if (destinationId == null || destinationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination ID tidak boleh kosong");
        }
        return medsosRepository.getByDestinationId(destinationId);
    }

    public int getMedsosCountByDestination(String destinationId) {
        if (destinationId == null || destinationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination ID tidak boleh kosong");
        }

        List<Medsos> medsosList = medsosRepository.getByDestinationId(destinationId);
        return medsosList == null ? 0 : medsosList.size();
    }

    public Medsos getMedsosbyDestinationAndPlatform(String destinationId, String platformName) {
        if (destinationId == null || destinationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination ID tidak boleh kosong");
        }
        if (platformName == null || platformName.trim().isEmpty()) {
            throw new IllegalArgumentException("Platform name tidak boleh kosong");
        }

        List<Medsos> medsosList = medsosRepository.getByDestinationId(destinationId);
        if (medsosList == null) {
            return null;
        }

        for (Medsos medsos : medsosList) {
            if (medsos.getPlatformName().equalsIgnoreCase(platformName)) {
                return medsos;
            }
        }

        return null;
    }
}
