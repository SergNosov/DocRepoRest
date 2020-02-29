package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.dao.SenderRepository;
import gov.kui.docRepoR.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SenderServiceImpl implements SenderService {
    private final SenderRepository senderRepository;

    @Autowired
    public SenderServiceImpl(SenderRepository senderRepository) {
        this.senderRepository = senderRepository;
    }

    @Override
    public List<Sender> findAll() {
        return senderRepository.findAll();
    }

    @Override
    public Page<Sender> findAllPage(Pageable pageable) {
        return senderRepository.findAll(pageable);
    }

    @Override
    public Sender findById(int id) {
        return senderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Не найден отправитель с id - " + id));
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
