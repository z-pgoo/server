package zipgoo.server.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;

@Entity
public class Commant {
    @Id
    private Long id;
    @OneToOne
    private User author;
    @Lob
    private String content;

}
