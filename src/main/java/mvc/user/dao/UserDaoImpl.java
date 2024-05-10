package mvc.user.dao;

import mvc.user.model.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private BaseDataDao baseDataDao;

    @Override
    public int addUser(User user) {
        String sql = "insert into user(name, age, birth, resume, education_id, gender_id)" +
                " values (?, ?, ?, ?, ?, ?)";
//        return jdbcTemplate.update(sql, user.getName(), user.getAge(), user.getBirth(), user.getResume(),
//                user.getEducationId(), user.getGenderId());

        // 自動將 user 物件的屬性值給 sql 參數(?) 使用
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(user);
        // KeyHolder
        KeyHolder keyHolder = new GeneratedKeyHolder();
        // keyHolder, new String[] {"id"} => 將主鍵欄位 id 所自動生成的序號放到 keyHolder 中
        namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[] {"id"});

        int userId = keyHolder.getKey().intValue(); // 最新 新增紀錄的 user id

        // 新增該 user 的興趣紀錄
        for (Integer interstId : user.getInterestIds()) {
            baseDataDao.addInterest(userId, interstId);
        }
        return userId;
    }

    @Override
    public int updateUser(Integer id, User user) {
        return 0;
    }

    @Override
    public int deleteUser(Integer id) {
        return 0;
    }

    @Override
    public User getUserById(Integer id) {
        return null;
    }

    @Override
    public List<User> findAllUsers() {
        return null;
    }
}