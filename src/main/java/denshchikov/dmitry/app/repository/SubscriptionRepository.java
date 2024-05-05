package denshchikov.dmitry.app.repository;

import denshchikov.dmitry.app.model.domain.Subscription;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, UUID> {

    Optional<Subscription> findByUserId(UUID userId);

    @Query("select * from SUBSCRIPTION s where s.user_id = :user_id FOR UPDATE")
    Optional<Subscription> findByUserIdForUpdate(@Param("user_id") UUID userId);

}