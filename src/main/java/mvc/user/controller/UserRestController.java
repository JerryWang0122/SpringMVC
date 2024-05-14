package mvc.user.controller;

import com.google.gson.Gson;
import mvc.user.dao.BaseDataDao;
import mvc.user.model.po.User;
import mvc.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 定義 URI 服務
 * ---------------------------------------------------------------------------
 * Method | URI          | Description
 * ---------------------------------------------------------------------------
 * GET    | /rest/user   | 取得所有使用者json資料
 * GET    | /rest/user/1 | 根據 userId，取得單筆使用者json資料
 * POST   | /rest/user/  | 新增使用者資料，會自動夾帶 User 的 json 物件資料上來
 * PUT    | /rest/user/1 | 修改指定 userId 的使用者資料，會自動夾帶要修改的 User 的 json 物件資料上來
 * DELETE | /rest/user/1 | 刪除指定 userId 的使用者紀錄
 * ---------------------------------------------------------------------------
 * 整體的 URL : http://localhost:8080/SpringMVC_war_exploded/mvc/user
 */

@RestController
@RequestMapping("/rest/user")
public class UserRestController {
    @Autowired
    private UserService userService;

    @Autowired
    private BaseDataDao baseDataDao;


    Gson gson = new Gson();

    @GetMapping
    public String queryAllUsers() {
        List<User> users = userService.findUsers();
        // 回傳 json 字串
        return gson.toJson(users);
    }

}
