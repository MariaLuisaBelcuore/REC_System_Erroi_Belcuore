package it.unisalento.rec.rec_payment.service;

import it.unisalento.rec.rec_payment.domain.ClientPayment;
import it.unisalento.rec.rec_payment.domain.MemberPayment;
import it.unisalento.rec.rec_payment.dto.ClientPaymentDTO;
import it.unisalento.rec.rec_payment.dto.ClientPaymentListDTO;
import it.unisalento.rec.rec_payment.exceptions.PaymentNotFoundException;
import it.unisalento.rec.rec_payment.repositories.ClientPaymentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientPaymentDetailsService {
    @Autowired
    ClientPaymentRepository clientPaymentRepository;

    public ClientPaymentDTO createClientPayment(ClientPaymentDTO clientPaymentDTO) {
        ClientPayment clientPayment = new ClientPayment();
        BeanUtils.copyProperties(clientPaymentDTO, clientPayment, "id");
        clientPayment.setCausal("Submission of a task");
        clientPayment.setEmissionDate(LocalDateTime.now());
        clientPayment = clientPaymentRepository.save(clientPayment);
        clientPaymentDTO.setId(clientPayment.getId());
        return clientPaymentDTO;
    }

    public ClientPaymentListDTO searchClientPaymentByClientEmail(String clientEmail) throws PaymentNotFoundException {
        ClientPaymentListDTO clientPaymentListDTO = new ClientPaymentListDTO();
        ArrayList<ClientPaymentDTO> payments = new ArrayList<>();
        clientPaymentListDTO.setList(payments);

        List<ClientPayment> list = clientPaymentRepository.findByClientEmail(clientEmail);

        if (list.isEmpty()) {
            throw new PaymentNotFoundException("No payment made");
        }

        for(ClientPayment clientPayment : list){
            ClientPaymentDTO clientPaymentDTO = new ClientPaymentDTO();
            BeanUtils.copyProperties(clientPayment, clientPaymentDTO);
            payments.add(clientPaymentDTO);
        }
        return clientPaymentListDTO;
    }

    public ClientPaymentDTO searchClientPaymentById(String id) throws PaymentNotFoundException {
        ClientPaymentDTO clientPaymentDTO = new ClientPaymentDTO();
        Optional<ClientPayment> optionalClientPayment = clientPaymentRepository.findById(id);

        if (optionalClientPayment.isEmpty()) {
            throw new PaymentNotFoundException("Non-existing payment");
        }

        ClientPayment clientPayment = optionalClientPayment.get();
        BeanUtils.copyProperties(clientPayment, clientPaymentDTO);
        return clientPaymentDTO;
    }

    public String deleteClientPayment(String id) throws PaymentNotFoundException{
        if (!clientPaymentRepository.existsById(id)) {
            throw new PaymentNotFoundException("Non-existing payment");
        }
        clientPaymentRepository.deleteById(id);
        return id;
    }

    public ClientPaymentListDTO searchAll() throws PaymentNotFoundException {
        ClientPaymentListDTO clientPaymentListDTO = new ClientPaymentListDTO();
        ArrayList<ClientPaymentDTO> payments = new ArrayList<>();
        clientPaymentListDTO.setList(payments);

        List<ClientPayment> list = clientPaymentRepository.findAll();

        if (list.isEmpty()) {
            throw new PaymentNotFoundException("No payment made");
        }

        for(ClientPayment clientPayment : list){
            ClientPaymentDTO clientPaymentDTO = new ClientPaymentDTO();
            BeanUtils.copyProperties(clientPayment, clientPaymentDTO);
            payments.add(clientPaymentDTO);
        }
        return clientPaymentListDTO;
    }
}
