package zipgoo.server.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;     // 게시물 아이디
    private Category category;
    private String title;    // 제목
    private String content;    // 내용
    @OneToOne
    private User user;// 작성자
    private int hits;    // 조회수
    private int recommend; // 추천 수

    @Embedded
    private Address address;
    private final LocalDateTime createDate = LocalDateTime.now(); // 생설일
    private LocalDateTime modifiedDate; // 수정일
    @Builder
    public Board(String title, String content, User user, int hits, int recommend, Address address){
        this.title = title;
        this.content = content;
        this.user = user;
        this.hits = hits;
        this.recommend = recommend;
        this.address = address;
    }
}
