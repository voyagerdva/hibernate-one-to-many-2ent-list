package nn.ru.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToMany(cascade = CascadeType.REFRESH) // прямая Item-Docs
//    @OneToMany // прямая Item-Docs
    @JoinColumn(name = "item_id")
    private List<Doc> docs;

    public void addDocToItem(Doc doc) {
        if (docs == null) {
            docs = new ArrayList<>();
        }
        docs.add(doc);
    }


}
