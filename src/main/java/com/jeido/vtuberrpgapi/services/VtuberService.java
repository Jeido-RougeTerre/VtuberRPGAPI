package com.jeido.vtuberrpgapi.services;

import com.jeido.vtuberrpgapi.dto.vtuber.VtuberDTOReceive;
import com.jeido.vtuberrpgapi.dto.vtuber.VtuberDTOSend;
import com.jeido.vtuberrpgapi.entites.User;
import com.jeido.vtuberrpgapi.entites.Vtuber;
import com.jeido.vtuberrpgapi.repositories.UserRepository;
import com.jeido.vtuberrpgapi.repositories.VtuberRepository;
import com.jeido.vtuberrpgapi.utils.exceptions.user.UserIdNotFoundException;
import com.jeido.vtuberrpgapi.utils.exceptions.vtuber.VtuberIdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VtuberService implements BaseService<VtuberDTOReceive, VtuberDTOSend> {
    private final VtuberRepository vtuberRepository;
    private final UserRepository userRepository;
    private final StatService statService;
    private final TriggerService triggerService;

    public VtuberDTOSend toDTOSend(Vtuber vtuber) {
        return VtuberDTOSend.builder()
                .id(vtuber.getId())
                .name(vtuber.getName())
                .userIds(vtuber.getUsers().stream().map(User::getId).toList())
                .stats(statService.toDTOSendLess(vtuber.getStats()))
                .triggers(triggerService.toDTOLess(vtuber.getTriggers()))
                .build();
    }

    public List<VtuberDTOSend> toDTOSend(List<Vtuber> vtubers) {
        List<VtuberDTOSend> vtuberDTOSends = new ArrayList<>();
        for (Vtuber vtuber : vtubers) {
            vtuberDTOSends.add(toDTOSend(vtuber));
        }
        return vtuberDTOSends;
    }

    @Autowired
    public VtuberService(VtuberRepository vtuberRepository, UserRepository userRepository, StatService statService, TriggerService triggerService) {
        this.vtuberRepository = vtuberRepository;
        this.userRepository = userRepository;
        this.statService = statService;
        this.triggerService = triggerService;
    }

    @Override
    public VtuberDTOSend create(VtuberDTOReceive vtuberDTOReceive) {

        return toDTOSend(vtuberRepository.save(Vtuber.builder()
                        .name(vtuberDTOReceive.getName())
                        .users(new ArrayList<>())
                        .stats(new ArrayList<>())
                        .triggers(new ArrayList<>())
                .build()));
    }

    public VtuberDTOSend create(VtuberDTOReceive vtuberDTOReceive, UUID userId) {
        return toDTOSend(vtuberRepository.save(Vtuber.builder()
                .name(vtuberDTOReceive.getName())
                .users(
                        Collections.singletonList(userRepository.findById(userId)
                                .orElseThrow(() -> new UserIdNotFoundException(userId))
                        )
                )
                .build()
        ));
    }

    @Override
    public VtuberDTOSend findById(UUID id) {
        return toDTOSend(vtuberRepository.findById(id).orElseThrow(() -> new VtuberIdNotFoundException(id)));
    }

    @Override
    public List<VtuberDTOSend> findAll() {
        return toDTOSend((List<Vtuber>) vtuberRepository.findAll());
    }

    @Override
    public VtuberDTOSend update(UUID id, VtuberDTOReceive vtuberDTOReceive) {

        Vtuber vtuber = vtuberRepository.findById(id).orElseThrow(() -> new VtuberIdNotFoundException(id));

        if (vtuberDTOReceive.getName() != null && !vtuberDTOReceive.getName().equals(vtuber.getName())) {
            vtuber.setName(vtuberDTOReceive.getName());
        }

        if (vtuberDTOReceive.getUserIds() != null) {
            vtuber.setUsers(vtuberDTOReceive.getUserIds().stream()
                    .map(uuid -> userRepository.findById(uuid).orElse(null)).filter(Objects::nonNull).toList());
        }
        return toDTOSend(vtuberRepository.save(vtuber));
    }

    @Override
    public boolean delete(UUID id) {
        if (!vtuberRepository.existsById(id)) return false;
        Vtuber vtuber = vtuberRepository.findById(id).orElse(null);
        List<User> users = userRepository.findAllByVtubersContains(vtuber);
        for (User user : users) {
            if (user.getVtubers().remove(vtuber)) {
                userRepository.save(user);
            }
        }
        statService.delete(id);
        triggerService.deleteByVtuber(id);
        vtuberRepository.deleteById(id);
        return !vtuberRepository.existsById(id);
    }

    public List<VtuberDTOSend> findAllByName(String name) {
        return toDTOSend(vtuberRepository.findAllByNameContainingIgnoreCase(name));
    }

    public List<VtuberDTOSend> findAllByUserId(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new VtuberIdNotFoundException(id));
        return toDTOSend(vtuberRepository.findAllByUsersContaining(user));
    }
}
