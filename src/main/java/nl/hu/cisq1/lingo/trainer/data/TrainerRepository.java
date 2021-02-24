package nl.hu.cisq1.lingo.trainer.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<TrainerData, Long> {
    Optional<TrainerData> findTopByOrderByIdDesc();
}
