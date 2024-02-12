package com.cafevery.repository;


import com.cafevery.domain.User;
import com.cafevery.dto.type.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findBySerialId(String serialId);

    @Query("select u.id as id from User u where u.serialId = :serialId")
    Optional<UserSecurityForm> findSecurityFormBySerialId(@Param("serialId") String serialId);

//    @Query("select u.id as id, u.role as role from User u where u.serialId = :serialId")
//    Optional<UserSecurityForm> findSecurityFormBySerialId(String serialId);


    @Query("select u.id as id from User u where u.id = :id")
    Optional<UserSecurityForm> findSecurityFormById(Long id);


    @Modifying(clearAutomatically = true)
    @Query("update User u set u.refreshToken = :refreshToken where u.id = :id")
    void updateRefreshTokenAndLoginStatus(Long id, String refreshToken);

    interface UserSecurityForm {
        Long getId();

    }

}
