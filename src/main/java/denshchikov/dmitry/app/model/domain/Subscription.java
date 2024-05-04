package denshchikov.dmitry.app.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("subscription")
public class Subscription {

    @Id
    @Column("id")
    private UUID id;

    @Column("user_id")
    private UUID userId;

    @Column("started_on")
    private LocalDateTime startedOn;

    @Column("ended_on")
    private LocalDateTime endedOn;

    @Column("created_on")
    private LocalDateTime createdOn;

    @Column("updated_on")
    private LocalDateTime updatedOn;

}