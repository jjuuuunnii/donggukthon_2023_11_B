package com.mooko.dev.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_id_seq")
    @SequenceGenerator(name = "event_id_seq", sequenceName = "event_id_seq")
    @Column(name = "event_id")
    private Long id;

    private String title;

    private Boolean activeStatus;


    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private User roomMaker;

    @OneToOne(mappedBy = "barcode", fetch = FetchType.LAZY)
    private Barcode barcode;

    @OneToMany(mappedBy = "event", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> users = new ArrayList<>();

    private LocalDateTime createdAt;

}
