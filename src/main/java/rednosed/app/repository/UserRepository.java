package rednosed.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
