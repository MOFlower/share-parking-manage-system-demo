package top.moflowerlkh.shareparkingdemo.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class ParkingInfo {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    String parkingName;

    @Column(nullable = false)
    double longitude;

    @Column(nullable = false)
    double latitude;

    @Column(unique = true, nullable = false)
    String uniqueCode;

    @Column(unique = true, nullable = false)
    String clientReceiveTopic;

    @Column(unique = true, nullable = false)
    String clientSendTopic;

    @ManyToOne
    User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ParkingInfo that = (ParkingInfo) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
