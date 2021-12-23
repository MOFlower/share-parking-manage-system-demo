package top.moflowerlkh.shareparkingdemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.moflowerlkh.shareparkingdemo.model.Car;

@Repository
public interface CarDao extends JpaRepository<Car, Long> {
    Car findByCarNumber(String carNumber);
}
