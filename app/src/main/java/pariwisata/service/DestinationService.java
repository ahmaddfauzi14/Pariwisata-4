package pariwisata.service;

import java.util.List;
import java.util.UUID;

import pariwisata.dao.DestinationRepository;
import pariwisata.model.Destination;

public class DestinationService {
    private DestinationRepository destinationRepository;

    public DestinationService() {
        this.destinationRepository = new DestinationRepository();
    }

    public boolean createDestination(String name, String category, String description, String address, 
        String price, String mapUrl, String operationalStatus, 
        String openHour, String closeHour, String photoUrl) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama destinasi tidak boleh kosong");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Kategori destinasi tidak boleh kosong");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Deskripsi destinasi tidak boleh kosong");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Alamat destinasi tidak boleh kosong");
        }

        String id = UUID.randomUUID().toString();
        Destination destination = new Destination(
            id, name, category, description, address, price, mapUrl, 
            operationalStatus, openHour, closeHour, photoUrl
        );

        return destinationRepository.create(destination);
    }

    public List<Destination> getAllDestinations() {
        return destinationRepository.getAll();
    }

    public Destination getDestinationById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID tidak boleh kosong");
        }
        return destinationRepository.getById(id);
    }

    public boolean updateDestination(String id, String name, String category, String description, String address,
        String price, String mapUrl, String operationalStatus,
        String openHour, String closeHour, String photoUrl) {
        if (id == null || id.trim().isEmpty()) {    
            throw new IllegalArgumentException("ID tidak boleh kosong");
        }

        Destination destination = destinationRepository.getById(id);
        if (destination == null) {
            throw new IllegalArgumentException("Destinasi tidak ditemukan");
        }

        if (name != null && !name.trim().isEmpty()) {
            destination.setName(name);
        }
        if (category != null && !category.trim().isEmpty()) {
            destination.setCategory(category);
        }
        if (description != null && !description.trim().isEmpty()) {
            destination.setDescription(description);
        }
        if (address != null && !address.trim().isEmpty()) {
            destination.setAddress(address);
        }
        if (price != null && !price.trim().isEmpty()) {
            destination.setPrice(price);
        }
        if (mapUrl != null && !mapUrl.trim().isEmpty()) {
            destination.setMapUrl(mapUrl);
        }
        if (operationalStatus != null && !operationalStatus.trim().isEmpty()) {
            destination.setOperationalStatus(operationalStatus);
        }
        if (openHour != null && !openHour.trim().isEmpty()) {
            destination.setOpenHour(openHour);
        }
        if (closeHour != null && !closeHour.trim().isEmpty()) {
            destination.setCloseHour(closeHour);
        }
        if (photoUrl != null && !photoUrl.trim().isEmpty()) {
            destination.setPhotoUrl(photoUrl);
        }

        return destinationRepository.update(destination);
    }

    public boolean deleteDestination(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID tidak boleh kosong");
        }

        Destination destination = destinationRepository.getById(id);
        if (destination == null) {
            throw new IllegalArgumentException("Destinasi tidak ditemukan");
        }

        return destinationRepository.delete(id);
    }

    public List<Destination> searchDestinations(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return destinationRepository.getAll();
        }

        List<Destination> allDestinations = destinationRepository.getAll();
        if (allDestinations == null) {
            return null;
        }

        List<Destination> results = new java.util.ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        
        for (Destination dest : allDestinations) {
            if (dest.getName().toLowerCase().contains(lowerKeyword) ||
                dest.getCategory().toLowerCase().contains(lowerKeyword) ||
                dest.getAddress().toLowerCase().contains(lowerKeyword)) {
                results.add(dest);
            }
        }

        return results;
    }

    public List<Destination> filterByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Kategori tidak boleh kosong");
        }

        List<Destination> allDestinations = destinationRepository.getAll();
        if (allDestinations == null) {
            return null;
        }

        List<Destination> results = new java.util.ArrayList<>();
        for (Destination dest : allDestinations) {
            if (dest.getCategory().equalsIgnoreCase(category)) {
                results.add(dest);
            }
        }

        return results;
    }
}
