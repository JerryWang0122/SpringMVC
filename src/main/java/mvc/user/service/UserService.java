package mvc.user.service;

import mvc.user.dao.BaseDataDao;
import mvc.user.dao.UserDao;
import mvc.user.model.dto.UserDto;
import mvc.user.model.po.Interest;
import mvc.user.model.po.Statistics;
import mvc.user.model.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private BaseDataDao baseDataDao;

    public List<User> findUsers() {
        return userDao.findAllUsers();
    }

    public List<UserDto> findUserDtos() {
        List<UserDto> userDtos = new ArrayList<>();
        // po
        List<User> users = findUsers();
        // po -> dto
        for (User user : users) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setAge(user.getAge());
            userDto.setBirth(user.getBirth());
            userDto.setResume(user.getResume());
            userDto.setEducation(baseDataDao.getEducationById(user.getEducationId()));
            userDto.setGender(baseDataDao.getGenderById(user.getGenderId()));

            // 取得所有興趣 id
            Integer[] interestIds = user.getInterestIds();
            // 準備一個興趣List，用來存放所有興趣物件
            List<Interest> interests = new ArrayList<>();
            for (Integer interestId : interestIds) {
                // 透過 interestId 得到興趣物件
                Interest interest = baseDataDao.getInterestById(interestId);
                // 將 興趣物件 注入到 興趣List
                interests.add(interest);
            }
            userDto.setInterests(interests);

            // 將 userDto 注入
            userDtos.add(userDto);
        }
        return userDtos;
    }

    public User getUser(Integer userId) {
        return userDao.getUserById(userId);
    }

    public Boolean addUser(User user) {
        return userDao.addUser(user) > 0;
    }

    public Boolean updateUser(Integer userId, User user) {
        return userDao.updateUser(userId, user) > 0;
    }

    public Boolean deleteUser(Integer userId) {
        return userDao.deleteUser(userId) > 0;
    }

    public List<Statistics> queryStatistics(String statisticsName) {
        switch (statisticsName) {
            case "Gender":
                return userDao.queryGenderStatistics();
            case "Education":
                return userDao.queryEducationStatistics();
        }
        return null;
    }
}
