package zipgoo.server.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;     // 게시물 아이디
    private Category category;
    private String title;    // 제목
    private String content;    // 내용
    @OneToOne
    private User user;// 작성자
    private int saves;    // 조회수
    private int recommend; // 추천 수

    @Embedded
    private Address address;

    @CreatedDate
    private final LocalDateTime createDate = LocalDateTime.now(); // 생설일
    @LastModifiedDate
    private LocalDateTime modifiedDate; // 수정일
    @OneToMany(mappedBy = "posts", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    private List<Comment> commentList;

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
    @Builder
    public Post(String title, String content, User user, Address address){
        this.title = title;
        this.content = content;
        this.user = user;
        this.saves = 0;
        this.recommend = 0;
        this.address = address;
    }
}
