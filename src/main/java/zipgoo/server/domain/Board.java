package zipgoo.server.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Board {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private User author;
    private String name;
    @OneToMany
    private List<Commant> commantList;

}
