package org.budnikov.staff.repository;

import org.budnikov.staff.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface HistoryRepository extends JpaRepository<History, Long> {
    @Query("select h from History h where h.person.id = :id")
    Set<History> findHistoryForPersonById(@Param("id") Long id);
}
