package com.renault.garage.utlis;

import com.renault.garage.entity.Brand;
import com.renault.garage.entity.Garage;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public final class GarageSpecificationUtil {
    
    private GarageSpecificationUtil() {}

    // Base "always true" specification
    public static Specification<Garage> alwaysTrue() {
        return (root, query, cb) -> cb.conjunction();
    }

    // Filter by garage name
    public static Specification<Garage> hasName(String name) {
        if (name == null || name.isBlank()) return alwaysTrue();
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("garageName")), "%" + name.toLowerCase(Locale.ROOT) + "%");
    }

    // Filter by city
    public static Specification<Garage> hasCity(String city) {
        if (city == null || city.isBlank()) return alwaysTrue();
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("address").get("city")), "%" + city.toLowerCase(Locale.ROOT) + "%");
    }

    // Filter by available brand
    public static Specification<Garage> hasBrand(Brand brand) {
        if (brand == null || brand.getBrandId() == null) return alwaysTrue();

        return (root, query, cb) -> {
            query.distinct(true);
            Join<Object, Object> vehicleJoin = root.join("vehicles", JoinType.INNER);
            return cb.equal(vehicleJoin.get("brand").get("brandId"), brand.getBrandId());
        };
    }

    // Combine all criteria
    public static Specification<Garage> byCriteria(String name, String city, Brand brand) {
        return hasName(name).and(hasCity(city)).and(hasBrand(brand));
    }
}

