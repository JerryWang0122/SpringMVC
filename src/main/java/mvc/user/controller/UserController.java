package mvc.user.controller;

import mvc.user.dao.BaseDataDao;
import mvc.user.model.dto.UserDto;
import mvc.user.model.po.*;
import mvc.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 定義 URI 服務
 * ---------------------------------------------------------------------------
 * Method | URI     | Description
 * ---------------------------------------------------------------------------
 * GET    | /user   | 取得所有使用者資料，並重導到 /WEB-INF/view/user/user.jsp 頁面
 * GET    | /user/1 | 根據 userId，取得單筆使用者資料，給予修改頁面用
 * POST   | /user/  | 新增使用者資料，會自動夾帶 User 物件資料上來
 * PUT    | /user/1 | 修改指定 userId 的使用者資料，會自動夾帶要修改的 User 物件資料上來
 * DELETE | /user/1 | 刪除指定 userId 的使用者紀錄
 * ---------------------------------------------------------------------------
 * 整體的 URL : http://localhost:8080/SpringMVC_war_exploded/mvc/user
 */

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BaseDataDao baseDataDao;

    @GetMapping
    // model: 給jsp的資料要放在model容器中
    public String queryAllUsers(@ModelAttribute User user, Model model) {
        // 基本要傳給jsp的資訊
        addBasicModel(model);

        // @ModelAttribute User user -> model.setAttribute("user", user)
        user.setAge(18);    // 預設年齡

        // 完整 jsp(view) 路徑 = "/WEB-INF/view/user/user.jsp";
        // 因為在 springmvc-servlet.xml
        // 已經定義: prefix = "/WEB-INF/view/"
        //          suffix = ".jsp"
        // 所以只要寫成 "user/user"
        return "user/user";
    }

    @GetMapping("/{userId}")
    public String getUser(@PathVariable("userId") Integer userId, Model model) {
        User user = userService.getUser(userId);
        // 將 user 資料傳給 jsp
        model.addAttribute("user", user);

        // 將 _method="PUT" 傳給 jsp
        model.addAttribute("_method", "PUT");

        // 基本要傳給jsp的資訊
        addBasicModel(model);

        return "user/user";
    }

    @PostMapping("/")
    public String createUser(@Valid User user, BindingResult result, Model model) {
        // @Valid 宣告物件需要驗證，驗證結果會被放到 BindingResult

        // 判斷驗證是否通過
        if (result.hasErrors()) {   // 若有錯誤發生
            // 基本要傳給 user.jsp 的資訊
            addBasicModel(model);
            // 有錯誤的 user 資料一併帶入給表單使用(內含錯誤的原因)
            model.addAttribute("user", user);
            return "user/user";
        }
        Boolean success = userService.addUser(user);
        return "redirect:/mvc/user";
    }

    @PutMapping("/{userId}")
    public String updateUser(@PathVariable("userId") Integer userId, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // 基本要傳給 user.jsp 的資訊
            addBasicModel(model);
            // 有錯誤的 user 資料一併帶入給表單使用(內含錯誤的原因)
            model.addAttribute("user", user);
            // 重要！要傳 PUT 回去
            model.addAttribute("_method", "PUT");
            return "user/user";
            /**
             * Note: put 無法直接發 request 到 jsp (只接受GET/POST)，
             * 因此需要到 jsp 加一個標注 -> isErrorPage="true"
             * 允許該 jsp 處理各種不同的請求動作
             */
        }
        Boolean success = userService.updateUser(userId, user);
        return "redirect:/mvc/user";
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable("userId") Integer userId) {
        Boolean success = userService.deleteUser(userId);
        return "redirect:/mvc/user";
    }

    // 基本要傳給user.jsp的資料
    private void addBasicModel(Model model) {
        List<UserDto> userDtos = userService.findUserDtos();
        List<Education> educations = baseDataDao.findAllEducations();   // 所有學歷
        List<Gender> genders = baseDataDao.findAllGenders();   // 所有性別
        List<Interest> interests = baseDataDao.findAllInterests();
        List<Statistics> genderStatistics = userService.queryStatistics("Gender");
        List<Statistics> educationStatistics = userService.queryStatistics("Education");

        model.addAttribute("userDtos", userDtos);
        // 選單選項
        model.addAttribute("educations", educations);
        model.addAttribute("genders", genders);
        model.addAttribute("interests", interests);
        model.addAttribute("genderStatistics", genderStatistics);
        model.addAttribute("educationStatistics", educationStatistics);

    }
}
