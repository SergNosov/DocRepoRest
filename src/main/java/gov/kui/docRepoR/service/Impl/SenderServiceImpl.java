package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.Entity.Sender;
import gov.kui.docRepoR.dao.SenderRepository;
import gov.kui.docRepoR.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
            throw new RuntimeException("Не найден отправитель с id - "+id);
        }
    }

    @Override
    public Sender save(Sender sender) {
        if (!(sender == null || sender.getTitle() == null || sender.getTitle().trim().isEmpty())) {
            if (sender.getId() != 0) {
                this.findById(sender.getId());
            }
            return senderRepository.save(sender);
        } else {
            throw new IllegalArgumentException("Не указан Sender (null), или заголовок (sender.title) пуст.");
        }
    }

    @Override
    public int deleteById(int id) {
        senderRepository.deleteById(this.findById(id).getId());
        return id;
    }

    @Override
    public boolean isExistsValueInField(Object value, String fieldName) {
        if (value == null || fieldName == null) {
            return false;
        }
        if (!fieldName.equals("title")) {
            throw new UnsupportedOperationException("Validation on field: '" + fieldName + "' not supported.");
        }
        return this.senderRepository.existsByTitle(value.toString());
    }
}
