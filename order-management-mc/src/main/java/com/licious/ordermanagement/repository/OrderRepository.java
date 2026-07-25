package com.licious.ordermanagement.repository;

import com.licious.ordermanagement.entity.Order;
import com.licious.ordermanagement.model.OrderStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(OrderStatus status);

    /**
     * SELECT ... FOR UPDATE - two requests racing to transition the same
     * order (e.g. one confirming, another cancelling) must not both read
     * CREATED and both think their transition is valid. This serializes
     * them at the DB row level so only the first to acquire the lock sees
     * the pre-transition state; the second sees the already-updated status
     * and gets a proper 409 from Order's state-machine validation.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from Order o where o.id = :id")
    Optional<Order> findByIdForUpdate(@Param("id") Long id);
}
