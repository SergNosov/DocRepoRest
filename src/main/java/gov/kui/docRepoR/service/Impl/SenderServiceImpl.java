package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.dao.SenderRepository;
import gov.kui.docRepoR.dto.SenderDto;
import gov.kui.docRepoR.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Transactional(readOnly = true)
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
    public List<SenderDto> findAllDtos() {
        return senderRepository.findAllDtos();
    }

    @Override
    public Sender findById(int id) {
        return senderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Не найден отправитель с id - " + id));
    }

    @Override
    public SenderDto findDtoById(int id) {
        return senderRepository.findDtoById(id)
                .orElseThrow(() -> new IllegalArgumentException("Не найден отправитель с id - " + id));
    }

    @Override
    @Transactional
    public Sender save(Sender sender) {
        Assert.notNull(sender, "Не указан sender (null)");
        Assert.hasText(sender.getTitle(), "Заголовок (sender.title) пуст. " + sender);

        if (sender.getId() != 0 && !senderRepository.existsById(sender.getId())) {
            throw new IllegalArgumentException("Не найден отправитель с id - " + sender.getId());
        }
        return senderRepository.save(sender);
    }

    @Override
    @Transactional
    public int deleteById(int id) {
        Sender sender = this.findById(id);
        senderRepository.delete(sender);
        return id;
    }

    @Override
    public Page<Sender> findAllPage(Pageable pageable) {
        return senderRepository.findAll(pageable);
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
