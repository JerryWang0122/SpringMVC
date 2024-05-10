package mvc.user.dao;

import mvc.user.model.po.Education;
import mvc.user.model.po.Gender;
import mvc.user.model.po.Interest;
import mvc.user.model.po.User;

import java.util.List;

public interface BaseDataDao {
    List<Education> findAllEducations();
    Education getEducationById(Integer id);

    List<Gender> findAllGenders();
    Gender getGenderById(Integer id);

    List<Interest> findAllInterests();
    Interest getInterestById(Integer id);
    int addInterest(Integer userId, Integer InterestId);
}
