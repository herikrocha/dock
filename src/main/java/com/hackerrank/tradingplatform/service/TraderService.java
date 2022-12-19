package com.hackerrank.tradingplatform.service;

import com.hackerrank.tradingplatform.dto.AddMoneyTraderDTO;
import com.hackerrank.tradingplatform.dto.UpdateTraderDTO;
import com.hackerrank.tradingplatform.exception.InvalidArgumentException;
import com.hackerrank.tradingplatform.model.Trader;
import com.hackerrank.tradingplatform.repository.TraderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TraderService {
    @Autowired
    private TraderRepository traderRepository;
    private static final String TRADER_EMAIL_ALREADY_EXISTS = "Trader already exists with informed email.";

    public void registerTrader(Trader trader) throws ParseException {
        validateEmail(trader.getEmail());
        trader.setCreatedAt(Timestamp.from(Instant.now()));
        traderRepository.save(trader);
    }

    public Optional<Trader> getTraderById(Long id) {
        return traderRepository.findById(id);
    }

    public Optional<Trader> getTraderByEmail(String email) {
        return traderRepository.findByEmail(email);
    }

    public List<Trader> getAllTraders() {
        return traderRepository.findAll();
    }

    public void updateTrader(UpdateTraderDTO trader) throws ParseException {
        Trader existingTrader = traderRepository.findByEmail(trader.getEmail()).get();

        existingTrader.setName(trader.getName());
        existingTrader.setUpdatedAt(Timestamp.from(Instant.now()));

        traderRepository.save(existingTrader);
    }

    public void addMoney(AddMoneyTraderDTO trader) {
        Trader existingTrader = traderRepository.findByEmail(trader.getEmail()).get();

        existingTrader.setBalance(trader.getAmount() + existingTrader.getBalance());

        traderRepository.save(existingTrader);
    }

    private void validateEmail(String email) {
        if (getTraderByEmail(email).isPresent()) {
            throw new InvalidArgumentException(TRADER_EMAIL_ALREADY_EXISTS);
        }
    }
}
