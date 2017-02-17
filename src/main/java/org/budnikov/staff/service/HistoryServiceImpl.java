package org.budnikov.staff.service;

import org.budnikov.staff.domain.History;
import org.budnikov.staff.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service("historyService")
@Repository
@Transactional
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    HistoryRepository historyRepository;

    @Transactional(readOnly = true)
    public History findById(Long id) {
        return historyRepository.findOne(id);
    }

    @Transactional
    public History save(History history) {
        return historyRepository.save(history);
    }

    @Transactional
    public void delete(Long id) {
        historyRepository.delete(historyRepository.findOne(id));
    }

    @Transactional(readOnly = true)
    public Set<History> findHistoryForPersonById(Long id) {
        return historyRepository.findHistoryForPersonById(id);
    }
}
