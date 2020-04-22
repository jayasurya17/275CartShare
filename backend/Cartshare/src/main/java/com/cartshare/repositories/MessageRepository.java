package com.cartshare.repositories;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cartshare.models.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    
    public List<Message> findByFromId(Long fromId);

    public List<Message> findByToId(Long toId);

    public List<Message> findByDate(Date date);

    public List<Message> findByFromIdAndToIdOrderByDateDesc(Long fromId, Long toId);

    public boolean existsByFromId(Long fromId);

    public boolean existsByToId(Long toId);

    public boolean existsByDate(Date date);
}