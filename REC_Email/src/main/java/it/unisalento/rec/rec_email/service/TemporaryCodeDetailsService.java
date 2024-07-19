package it.unisalento.rec.rec_email.service;

import it.unisalento.rec.rec_email.domain.TemporaryCode;
import it.unisalento.rec.rec_email.repositories.TemporaryCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class TemporaryCodeDetailsService {
    @Autowired
    TemporaryCodeRepository temporaryCodeRepository;

    public TemporaryCode generateCode(String email) {
        TemporaryCode tempCode = new TemporaryCode();
        tempCode.setCode(UUID.randomUUID().toString());
        tempCode.setEmail(email);
        tempCode.setUsed(false);
        return temporaryCodeRepository.save(tempCode);
    }

    public boolean validateCode(String code, String email) {
        Optional<TemporaryCode> tempCodeOpt = temporaryCodeRepository.findByCode(code);
        if (tempCodeOpt.isPresent()) {
            TemporaryCode tempCode = tempCodeOpt.get();
            if (!tempCode.isUsed() && tempCode.getEmail().equals(email)) {
                tempCode.setUsed(true);
                temporaryCodeRepository.save(tempCode);
                return true;
            } else {
                System.out.println("Code already used or email mismatch.");
            }
        } else {
            System.out.println("Code not found.");
        }
        return false;
    }
}
