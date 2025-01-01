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
                .title(vtuber.getTitle())
                .thumbnailPath(vtuber.getThumbnailPath())
                .version(vtuber.getVersion())
                .author(vtuber.getAuthor())
                .contact(vtuber.getContact())
                .reference(vtuber.getReference())
                .performer(vtuber.getPerformer())
                .allowViolent(vtuber.isAllowViolent())
                .allowSexual(vtuber.isAllowSexual())
                .allowCommercial(vtuber.isAllowCommercial())
                .otherLicenseUrl(vtuber.getOtherLicenseUrl())
                .redistribution(vtuber.getRedistribution())
                .otherRedistributionUrl(vtuber.getOtherRedistributionLicenseUrl())
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
                .title(vtuberDTOReceive.getTitle())
                .thumbnailPath(vtuberDTOReceive.getThumbnailPath())
                .version(vtuberDTOReceive.getVersion())
                .author(vtuberDTOReceive.getAuthor() == null ? "no-author" : vtuberDTOReceive.getAuthor())
                .contact(vtuberDTOReceive.getContact())
                .reference(vtuberDTOReceive.getReference())
                .performer(vtuberDTOReceive.getPerformer())
                .allowViolent(vtuberDTOReceive.isAllowViolent())
                .allowSexual(vtuberDTOReceive.isAllowSexual())
                .allowCommercial(vtuberDTOReceive.isAllowCommercial())
                .otherLicenseUrl(vtuberDTOReceive.getOtherLicenseUrl())
                .redistribution(vtuberDTOReceive.getRedistribution())
                .otherRedistributionLicenseUrl(vtuberDTOReceive.getOtherRedistributionUrl())
                .build()));
    }

    public VtuberDTOSend create(VtuberDTOReceive vtuberDTOReceive, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserIdNotFoundException(userId));
        return toDTOSend(vtuberRepository.save(Vtuber.builder()
                .title(vtuberDTOReceive.getTitle())
                .thumbnailPath(vtuberDTOReceive.getThumbnailPath())
                .version(vtuberDTOReceive.getVersion())
                .author(vtuberDTOReceive.getAuthor() == null ? user.getUsername() : vtuberDTOReceive.getAuthor())
                .contact(vtuberDTOReceive.getContact())
                .reference(vtuberDTOReceive.getReference())
                .performer(vtuberDTOReceive.getPerformer())
                .allowViolent(vtuberDTOReceive.isAllowViolent())
                .allowSexual(vtuberDTOReceive.isAllowSexual())
                .allowCommercial(vtuberDTOReceive.isAllowCommercial())
                .otherLicenseUrl(vtuberDTOReceive.getOtherLicenseUrl())
                .redistribution(vtuberDTOReceive.getRedistribution())
                .otherRedistributionLicenseUrl(vtuberDTOReceive.getOtherRedistributionUrl())
                .users(Collections.singletonList(user))
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

        if (vtuberDTOReceive.getTitle() != null && !vtuberDTOReceive.getTitle().equals(vtuber.getTitle())) {
            vtuber.setTitle(vtuberDTOReceive.getTitle());
        }

        if (vtuberDTOReceive.getThumbnailPath() != null && !vtuberDTOReceive.getThumbnailPath().isEmpty() && !vtuberDTOReceive.getThumbnailPath().equals(vtuber.getThumbnailPath())) {
            vtuber.setThumbnailPath(vtuberDTOReceive.getThumbnailPath());
        }

        if (vtuberDTOReceive.getVersion() != null && !vtuberDTOReceive.getVersion().isEmpty() && !vtuberDTOReceive.getVersion().equals(vtuber.getVersion())) {
            vtuber.setVersion(vtuberDTOReceive.getVersion());
        }

        if (vtuberDTOReceive.getAuthor() != null && !vtuberDTOReceive.getAuthor().isEmpty() && !vtuberDTOReceive.getAuthor().equals(vtuber.getAuthor())) {
            vtuber.setAuthor(vtuberDTOReceive.getAuthor());
        }

        if (vtuberDTOReceive.getContact() != null && !vtuberDTOReceive.getContact().isEmpty() && !vtuberDTOReceive.getContact().equals(vtuber.getContact())) {
            vtuber.setContact(vtuberDTOReceive.getContact());
        }

        if (vtuberDTOReceive.getReference() != null && !vtuberDTOReceive.getReference().isEmpty() && !vtuberDTOReceive.getReference().equals(vtuber.getReference())) {
            vtuber.setReference(vtuberDTOReceive.getReference());
        }

        if (vtuberDTOReceive.getPerformer() != null && !vtuberDTOReceive.getPerformer().equals(vtuber.getPerformer())) {
            vtuber.setPerformer(vtuberDTOReceive.getPerformer());
        }

        if (vtuberDTOReceive.isAllowViolent() != vtuber.isAllowViolent()) {
            vtuber.setAllowViolent(vtuberDTOReceive.isAllowViolent());
        }

        if (vtuberDTOReceive.isAllowSexual() != vtuber.isAllowSexual()) {
            vtuber.setAllowSexual(vtuber.isAllowSexual());
        }

        if (vtuberDTOReceive.isAllowCommercial() != vtuber.isAllowCommercial()) {
            vtuber.setAllowCommercial(vtuberDTOReceive.isAllowCommercial());
        }

        if (vtuberDTOReceive.getOtherLicenseUrl() != null && !vtuberDTOReceive.getOtherLicenseUrl().isEmpty() && !vtuberDTOReceive.getOtherLicenseUrl().equals(vtuber.getOtherLicenseUrl())) {
            vtuber.setOtherLicenseUrl(vtuberDTOReceive.getOtherLicenseUrl());
        }

        if (vtuberDTOReceive.getRedistribution() != null && !vtuberDTOReceive.getRedistribution().equals(vtuber.getRedistribution())) {
            vtuber.setRedistribution(vtuberDTOReceive.getRedistribution());
        }

        if (vtuberDTOReceive.getOtherRedistributionUrl() != null && !vtuberDTOReceive.getOtherRedistributionUrl().isEmpty() && !vtuberDTOReceive.getOtherRedistributionUrl().equals(vtuber.getOtherRedistributionLicenseUrl())) {
            vtuber.setOtherRedistributionLicenseUrl(vtuberDTOReceive.getOtherRedistributionUrl());
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
        return toDTOSend(vtuberRepository.findAllByTitleContainingIgnoreCase(name));
    }

    public List<VtuberDTOSend> findAllByUserId(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new VtuberIdNotFoundException(id));
        return toDTOSend(vtuberRepository.findAllByUsersContaining(user));
    }
}
