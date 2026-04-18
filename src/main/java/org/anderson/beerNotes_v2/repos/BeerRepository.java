package org.anderson.beerNotes_v2.repos;

import org.anderson.beerNotes_v2.entity.Beer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BeerRepository extends ListCrudRepository<Beer, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Beer b WHERE b.name = :name")
    int deleteByName(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("UPDATE Beer b SET b.note = :note WHERE b.name = :name")
    int updateBeerNote(@Param("name") String name, @Param("note") String note);

    boolean existsByName(String name);

    // New method: Updates allowed fields for a beer by name (efficient in-place update)
    @Transactional
    @Modifying
    @Query("UPDATE Beer b SET b.style = :style, b.brewery = :brewery, b.origin = :origin, b.note = :note WHERE b.name = :name")
    int updateBeerFields(@Param("name") String name, @Param("style") String style, @Param("brewery") String brewery, @Param("origin") String origin, @Param("note") String note);
}
