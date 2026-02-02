package com.dbrand.service;

import com.dbrand.dto.ShoeResponse;
import com.dbrand.dto.ShoeSizeResponse;
import com.dbrand.dto.CreateShoeRequest;
import com.dbrand.model.Shoe;
import com.dbrand.repository.ShoeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShoeService {

    @Autowired
    private ShoeRepository shoeRepository;

    public List<ShoeResponse> getAllShoes(String brand, Integer minSize, Integer maxSize, BigDecimal minPrice, BigDecimal maxPrice) {
        List<ShoeResponse> shoes = shoeRepository.findAll(brand, minSize, maxSize, minPrice, maxPrice);
        
        // Add sizes to each shoe
        for (ShoeResponse shoe : shoes) {
            List<ShoeSizeResponse> sizes = shoeRepository.findSizesByShoeId(UUID.fromString(shoe.getId()));
            shoe.setSizes(sizes);
        }
        
        return shoes;
    }

    public List<Shoe> getAllShoesAdmin() {
        return shoeRepository.findAllAdmin();
    }

    public long getShoeCount() {
        return shoeRepository.count();
    }

    public Shoe getShoeByIdAdmin(String id) {
        return shoeRepository.findByIdAdmin(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Shoe not found"));
    }

    public Optional<ShoeResponse> getShoeById(UUID id) {
        Optional<ShoeResponse> shoeOpt = shoeRepository.findById(id);
        if (shoeOpt.isPresent()) {
            ShoeResponse shoe = shoeOpt.get();
            List<ShoeSizeResponse> sizes = shoeRepository.findSizesByShoeId(id);
            shoe.setSizes(sizes);
            return Optional.of(shoe);
        }
        return Optional.empty();
    }

    public ShoeResponse createShoe(CreateShoeRequest request) {
        Shoe shoe = new Shoe();
        shoe.setName(request.getName());
        shoe.setBrand(request.getBrand());
        shoe.setDescription(request.getDescription());
        shoe.setPrice(request.getPrice());
        shoe.setImageUrl(request.getImageUrl());

        shoe = shoeRepository.save(shoe);

        ShoeResponse response = new ShoeResponse();
        response.setId(shoe.getId().toString());
        response.setName(shoe.getName());
        response.setBrand(shoe.getBrand());
        response.setDescription(shoe.getDescription());
        response.setPrice(shoe.getPrice());
        response.setImageUrl(shoe.getImageUrl());
        response.setSizes(List.of());

        return response;
    }

    public ShoeResponse updateShoe(UUID id, CreateShoeRequest request) {
        if (!shoeRepository.existsById(id)) {
            throw new RuntimeException("Shoe not found");
        }

        Shoe shoe = new Shoe();
        shoe.setId(id);
        shoe.setName(request.getName());
        shoe.setBrand(request.getBrand());
        shoe.setDescription(request.getDescription());
        shoe.setPrice(request.getPrice());
        shoe.setImageUrl(request.getImageUrl());

        shoe = shoeRepository.save(shoe);

        List<ShoeSizeResponse> sizes = shoeRepository.findSizesByShoeId(id);

        ShoeResponse response = new ShoeResponse();
        response.setId(shoe.getId().toString());
        response.setName(shoe.getName());
        response.setBrand(shoe.getBrand());
        response.setDescription(shoe.getDescription());
        response.setPrice(shoe.getPrice());
        response.setImageUrl(shoe.getImageUrl());
        response.setSizes(sizes);

        return response;
    }

    public void deleteShoe(UUID id) {
        if (!shoeRepository.existsById(id)) {
            throw new RuntimeException("Shoe not found");
        }
        shoeRepository.deleteById(id);
    }

    public void deleteShoe(String id) {
        deleteShoe(UUID.fromString(id));
    }

    public void updateShoeSize(UUID shoeId, Integer size, Integer stockCount) {
        if (!shoeRepository.existsById(shoeId)) {
            throw new RuntimeException("Shoe not found");
        }
        shoeRepository.updateShoeSize(shoeId, size, stockCount);
    }

    public void deleteShoeSize(UUID shoeId, Integer size) {
        shoeRepository.deleteShoeSize(shoeId, size);
    }
}
