package mvc.user.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import mvc.user.model.po.Education;
import mvc.user.model.po.Gender;
import mvc.user.model.po.Interest;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDto {
    private Integer id; // 序號
    private String name;  // 姓名
    private Integer age;  // 年齡

    @DateTimeFormat(pattern = "yyyy-MM-dd")  // 日期格式
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birth;  // 生日

    private String resume;  // 履歷
    private Education education;  // 教育程度物件
    private Gender gender;   // 性別物件

    private List<Interest> interests;  // 興趣列表

    // 顯示所有興趣名稱給 JSTL 使用
    public String getInterestNames() {
        if (interests != null) {
            return interests.stream().map(Interest::getName).collect(Collectors.joining(" "));
        }
        return "";
    }
}
