package rednosed.app.repository.rds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.rds.LikeSeal;
import rednosed.app.domain.rds.Seal;
import rednosed.app.domain.rds.User;
import rednosed.app.dto.response.SealLikeDataTmpDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeSealRepository extends JpaRepository<LikeSeal, Long> {
    List<LikeSeal> findByUser(User user);

    @Query("SELECT new rednosed.app.dto.response.SealLikeDataTmpDto(s.sealClientId, s.sealImgUrl, s.sealName, COUNT(ls)) " +
            "FROM LikeSeal ls JOIN ls.seal s " +
            "GROUP BY s.sealClientId, s.sealImgUrl, s.sealName")
    List<SealLikeDataTmpDto> findAllSealLikeData();

    @Query("SELECT ls.seal.sealClientId FROM LikeSeal ls WHERE ls.user.userClientId = :userClientId")
    List<String> findLikedSealIdsByUserClientId(@Param("userClientId") String userClientId);

    Optional<LikeSeal> findByUserAndSeal(User user, Seal seal);

    @Query("SELECT new rednosed.app.dto.response.SealLikeDataTmpDto(s.sealClientId, s.sealImgUrl, s.sealName, COUNT(ls)) " +
            "FROM LikeSeal ls JOIN ls.seal s " +
            "WHERE s.sealClientId = :sealClientId " +
            "GROUP BY s.sealClientId, s.sealImgUrl, s.sealName")
    Optional<SealLikeDataTmpDto> findAllSealLikeDataBySealClientId(@Param("sealClientId") String sealClientId);

}
