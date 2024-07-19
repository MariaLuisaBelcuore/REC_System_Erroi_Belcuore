package it.unisalento.rec.rec_payment.service;

import it.unisalento.rec.rec_payment.domain.MemberPayment;
import it.unisalento.rec.rec_payment.dto.MemberPaymentDTO;
import it.unisalento.rec.rec_payment.dto.MemberPaymentListDTO;
import it.unisalento.rec.rec_payment.exceptions.PaymentNotFoundException;
import it.unisalento.rec.rec_payment.repositories.MemberPaymentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberPaymentDetailsService {
    @Autowired
    MemberPaymentRepository memberPaymentRepository;

    public MemberPaymentDTO createMemberPayment(MemberPaymentDTO memberPaymentDTO) {
        MemberPayment memberPayment = new MemberPayment();
        BeanUtils.copyProperties(memberPaymentDTO, memberPayment, "id");
        memberPayment.setCausal("Buy of reward");
        memberPayment.setEmissionDate(LocalDateTime.now());
        memberPayment.setPaymentorcredit("Payment");


        memberPayment = memberPaymentRepository.save(memberPayment);
        BeanUtils.copyProperties(memberPayment, memberPaymentDTO);
        return memberPaymentDTO;
    }

    public MemberPaymentListDTO searchMemberPaymentByMemberEmail(String memberEmail) throws PaymentNotFoundException {
        MemberPaymentListDTO memberPaymentListDTO = new MemberPaymentListDTO();
        ArrayList<MemberPaymentDTO> payments = new ArrayList<>();
        memberPaymentListDTO.setList(payments);

        List<MemberPayment> list = memberPaymentRepository.findByMemberEmail(memberEmail);

        if (list.isEmpty()) {
            throw new PaymentNotFoundException("No credit");
        }

        for(MemberPayment memberPayment : list){
            MemberPaymentDTO memberPaymentDTO = new MemberPaymentDTO();
            BeanUtils.copyProperties(memberPayment, memberPaymentDTO);
            payments.add(memberPaymentDTO);
        }
        return memberPaymentListDTO;
    }

    public MemberPaymentDTO searchMemberPaymentById(String id) throws PaymentNotFoundException {
        MemberPaymentDTO memberPaymentDTO = new MemberPaymentDTO();
        Optional<MemberPayment> optionalMemberPayment = memberPaymentRepository.findById(id);

        if (optionalMemberPayment.isEmpty()) {
            throw new PaymentNotFoundException("Non-existent credit");
        }

        MemberPayment memberPayment = optionalMemberPayment.get();
        BeanUtils.copyProperties(memberPayment, memberPaymentDTO);
        return memberPaymentDTO;
    }

    public String deleteMemberPayment(String id) throws PaymentNotFoundException{
        if (!memberPaymentRepository.existsById(id)) {
            throw new PaymentNotFoundException("Non-existent credit");
        }
        memberPaymentRepository.deleteById(id);
        return id;
    }
}
