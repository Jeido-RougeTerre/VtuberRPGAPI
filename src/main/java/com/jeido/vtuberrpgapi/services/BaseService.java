package com.jeido.vtuberrpgapi.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BaseService<DTOReceive, DTOSend> {
    DTOSend create(DTOReceive receive);
    DTOSend findById(UUID id);
    List<DTOSend> findAll();
    DTOSend update(UUID id, DTOReceive receive);
    boolean delete(UUID id);
}
