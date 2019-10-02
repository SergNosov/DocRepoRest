package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.Entity.Sender;
import gov.kui.docRepoR.dao.SenderRepository;
import gov.kui.docRepoR.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SenderServiceImpl implements SenderService {

    private SenderRepository senderRepository;

    @Autowired
    public SenderServiceImpl(SenderRepository senderRepository) {
        this.senderRepository = senderRepository;
    }

    @Override
    public List<Sender> findAll() {
        return senderRepository.findAll();
    }

    @Override
    public Sender findById(int id) {
        Optional<Sender> result = senderRepository.findById(id);

        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RuntimeException("Did not find sender id - "+id);
        }
    }

    @Override
    public Sender save(Sender sender) {
        return senderRepository.save(sender);
    }

    @Override
    public void deleteById(int id) {
        senderRepository.deleteById(id);
    }
}
