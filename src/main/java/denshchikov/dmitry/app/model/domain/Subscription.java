package denshchikov.dmitry.app.model.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@Table
public class Subscription {

    @Id
    @Column
    private UUID id;

    @Column
    private UUID userId;

    @Column
    private LocalDateTime startedOn;

    @Column
    private LocalDateTime endedOn;

}