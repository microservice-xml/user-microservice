package com.example.usermicroservice.service;

import com.example.usermicroservice.exception.CannotRateSameHost;
import com.example.usermicroservice.exception.ThisGuestHaventReservation;
import com.example.usermicroservice.model.Rate;
import com.example.usermicroservice.model.User;
import com.example.usermicroservice.repository.RateRepository;
import com.example.usermicroservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RateService {
    @Autowired
    RateRepository rateRepository;
    private final ReservationService reservationService;
    private final UserRepository userRepository;

    public List<Rate> findAll() {
        return rateRepository.findAll();
    }

    public float calculateAvgRate(Rate rate) {
        List<Rate> rates = rateRepository.findALlByHostId(rate.getHostId());
        float sum = 0;
        for(Rate rat : rates) {
            sum+= rat.getRateValue();
        }
        return sum / rates.size();
    }

    public Rate rateHost(Rate rate) {
        if(reservationService.checkCanUserRate(rate.getHostId(), rate.getGuestId())) {
            if(reservationService.checkReservationHistory(rate.getHostId(), rate.getGuestId()))
            {
                Rate newRate = rateRepository.save(rate);
                User u = userRepository.findById(rate.getHostId()).get();
                u.setAvgGrade(calculateAvgRate(rate));
                userRepository.save(u);
                return newRate;
            } else {
                throw new ThisGuestHaventReservation();
            }
        }
        throw new CannotRateSameHost();
    }

    public Rate changeRate(Rate rate) {
        Optional<Rate> newRate = rateRepository.findById(rate.getId());
        newRate.get().setRateValue(rate.getRateValue());
        Rate nr = rateRepository.save(newRate.get());
        User u = userRepository.findById(rate.getHostId()).get();
        u.setAvgGrade(calculateAvgRate(rate));
        userRepository.save(u);
        return nr;
    }

    public Rate deleteRate(Long id) {
        Optional<Rate> rateOptional = rateRepository.findById(id);
        if (rateOptional.isPresent()) {
            Rate rate = rateOptional.get();
            rateRepository.delete(rate);
            User u = userRepository.findById(rate.getHostId()).orElse(null);
            u.setAvgGrade(calculateAvgRate(rate));
            userRepository.save(u);
            return rate;
        }
        return null;
    }

    public Rate getById(Long id) {
        Optional<Rate> rate = rateRepository.findById(id);
        if (rate.isEmpty()) {
            return null;
        }
        return rate.get();
    }

    public List<Rate> getAllByHostId(Long id)
    {
        return rateRepository.findAllByHostId(id);
    }
}
