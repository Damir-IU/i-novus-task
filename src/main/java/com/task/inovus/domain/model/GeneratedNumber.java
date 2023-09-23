package com.task.inovus.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class GeneratedNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generated_number")
    @SequenceGenerator(name = "seq_generated_number", sequenceName = "seq_generated_number_id", allocationSize = 1)
    Long id;

    Character firstChar;
    Integer number;
    Character secondChar;
    Character thirdChar;
    String location;

    @CreatedDate
    LocalDateTime createdAt;

    public String getFullNumber() {
        return String.format("%s%s%s%s %s", firstChar, String.format("%03d", number), secondChar, thirdChar, location);
    }
}