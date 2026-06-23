package project.training.com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import project.training.com.example.demo.entity.TaskLog;

@Repository
public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {

    @Query("""
        select coalesce(sum(tl.hours), 0)
        from TaskLog tl
        where tl.task.id = :taskId
    """)
    int sumHoursByTaskId(Long taskId);
} 
