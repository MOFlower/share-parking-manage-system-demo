package top.moflowerlkh.shareparkingdemo.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true)
    String username;
    @Column(nullable = false)
    String password;
    @Column(nullable = false)
    String role;

    @OneToMany(cascade = CascadeType.REFRESH)
    @LazyCollection(LazyCollectionOption.FALSE)
    @ToString.Exclude
    List<ParkingInfo> owningParking;

    @OneToMany(cascade = CascadeType.REFRESH)
    @LazyCollection(LazyCollectionOption.FALSE)
    List<Car> owningCar;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.owningParking = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
