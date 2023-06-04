package com.example.usermicroservice.service;

import com.example.usermicroservice.model.Rate;
import com.example.usermicroservice.model.Reservation;
import com.example.usermicroservice.model.User;
import com.example.usermicroservice.model.enums.ReservationStatus;

import com.example.usermicroservice.repository.RateRepository;
import com.example.usermicroservice.repository.UserRepository;
import communication.ListReservation;
import communication.LongId;
import communication.ReservationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;

import static com.example.usermicroservice.mapper.ReservationMapper.convertReservationGrpcToReservation;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final UserRepository userRepository;

    private final RateRepository rateRepository;
    private final RateService rateService;

    private ReservationServiceGrpc.ReservationServiceBlockingStub getStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9095)
                .usePlaintext()
                .build();
        return ReservationServiceGrpc.newBlockingStub(channel);
    }
    public List<Reservation> findAllByHostId(Long hostId){
        ReservationServiceGrpc.ReservationServiceBlockingStub blockingStub = getStub();

        ListReservation reservations = blockingStub.findAllByHostId(LongId.newBuilder().setId(hostId).build());
        List<Reservation> retVal = new ArrayList<>();
        for(communication.Reservation res : reservations.getReservationsList()){
            retVal.add(convertReservationGrpcToReservation(res));
        }
        return retVal;
    }

    public boolean checkReservationHistory(Long hostId, Long guestId) {
        List<Reservation> reservations = findAllByHostId(hostId);

        for(Reservation res : reservations){
            if(res.getUserId().equals(guestId)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkCanUserRate(Long hostId, Long guestId) {
        List<Rate> rates = rateRepository.findAllByGuestId(guestId);
        if(rates.size() > 0) {
            return false;
        }
        return true;
    }

    public boolean calculateHighlighted(Long hostId){
        boolean cancellationRate = false;
        boolean leastFiveReservations = false;
        boolean moreThan50days = false;
        boolean avgGrade = false;
        List<Reservation> reservations = findAllByHostId(hostId);
        List<Reservation> acceptedReservations = new ArrayList<>();
        List<Reservation> canceledReservations = new ArrayList<>();

        for(Reservation res : reservations){
            if(res.getStatus().equals(ReservationStatus.ACCEPTED))
                acceptedReservations.add(res);
            else if(res.getStatus().equals(ReservationStatus.CANCELED))
                canceledReservations.add(res);
        }

        if(acceptedReservations.size() == 0)
            cancellationRate = false;
        else if((canceledReservations.size()/acceptedReservations.size()*100) < 5)
            cancellationRate = true;

        List<Reservation> pastAcceptedReservations = new ArrayList<>();
        for(Reservation res : acceptedReservations)
            if(LocalDate.now().isAfter(res.getEnd()))
                pastAcceptedReservations.add(res);

        if(pastAcceptedReservations.size() >= 5)
            leastFiveReservations = true;


        int days = 0;
        for(Reservation res : pastAcceptedReservations){
            days += ChronoUnit.DAYS.between(res.getStart(), res.getEnd());
        }
        if(days > 50)
            moreThan50days = true;

        if(userRepository.findById(hostId).get().getAvgGrade() > 4.7)
            avgGrade = true;


        return cancellationRate & leastFiveReservations & moreThan50days & avgGrade;
    }

    public void updateHostHighlighted(Long hostId){
        User u = userRepository.findById(hostId).get();
        boolean oldStatus = u.isHighlighted();
        u.setHighlighted(calculateHighlighted(hostId));
        boolean newStatus = u.isHighlighted();
        doesStatusChanged(hostId,oldStatus,newStatus);
        userRepository.save(u);
    }

    private void doesStatusChanged(Long hostId, boolean oldStatus, boolean newStatus) {
        if(oldStatus==false && newStatus == true)
            rateService.createNotification(hostId,"Congratulations. You have become a highlighted host.");
        else if(oldStatus==true && newStatus == false)
            rateService.createNotification(hostId,"Unfortunately, you have lose a status of highlighted host.");
    }


}
