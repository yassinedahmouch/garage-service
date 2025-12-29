package com.renault.garage.utlis;

import org.springframework.data.jpa.domain.Specification;
import com.renault.garage.entity.Vehicle;

import java.util.Locale;
import java.util.Set;

public final class VehicleSpecificationUtil {

    private VehicleSpecificationUtil() {}

    public static Specification<Vehicle> alwaysTrue() {
        return (root, query, cb) -> cb.conjunction();
    }

    // Filter by brand name (case-insensitive, partial match)
    public static Specification<Vehicle> hasBrandName(String brandName) {
        if (brandName == null || brandName.isBlank()) return alwaysTrue();
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("brand").get("name")),
                        "%" + brandName.toLowerCase(Locale.ROOT) + "%");
    }

    // Filter by garages (set of garage IDs)
    public static Specification<Vehicle> inGarages(Set<Long> garageIds) {
        if (garageIds == null || garageIds.isEmpty()) return alwaysTrue();
        return (root, query, cb) -> root.get("garage").get("garageId").in(garageIds);
    }

    // Combine criteria
    public static Specification<Vehicle> byCriteria(String brandName, Set<Long> garageIds) {
        return hasBrandName(brandName).and(inGarages(garageIds));
    }
}
