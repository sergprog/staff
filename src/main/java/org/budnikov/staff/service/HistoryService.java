package org.budnikov.staff.service;

import org.budnikov.staff.domain.History;

import java.util.Set;

public interface HistoryService {

    History findById(Long id);

    History save(History history);

    void delete(Long id);

    Set<History> findHistoryForPersonById(Long id);
}
