package top.moflowerlkh.shareparkingdemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Car {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true)
    private String carNumber;

    @ManyToOne
    private User owner;

}
