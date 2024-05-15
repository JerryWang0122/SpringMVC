package mvc.user.controller;

import mvc.user.dao.BaseDataDao;
import mvc.user.model.dto.UserDto;
import mvc.user.model.po.User;
import mvc.user.model.response.ApiResponse;
import mvc.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 查詢多筆紀錄
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> queryAllUsers() {
        List<UserDto> userDtos = userService.findUserDtos();
        ApiResponse apiResponse = new ApiResponse<>(true, "query success", userDtos);
        // 回傳 json 字串
        return ResponseEntity.ok(apiResponse);
    }

    // 查詢單筆紀錄
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable("id") Integer id) {
        try {
            User user = userService.getUser(id);
            ApiResponse<User> apiResponse = new ApiResponse<>(true, "get success", user);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse apiResponse = new ApiResponse<>(false, e.getMessage(), null);
            return ResponseEntity.ok(apiResponse);
        }
    }

    // 新增紀錄
    @PostMapping
    public ResponseEntity<ApiResponse<User>> addUser(@RequestBody User user) {
        Integer userId = userService.addUserAndGetId(user);
        if (userId != null) {
            user.setId(userId);
            ApiResponse<User> apiResponse = new ApiResponse<>(true, "add success", user);
            return ResponseEntity.ok(apiResponse);
        }
        ApiResponse<User> apiResponse = new ApiResponse<>(false, "add fail", user);
        return ResponseEntity.ok(apiResponse);

    }

    // 修改紀錄
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Integer id, @RequestBody User user) {
        // 修改
        Boolean state = userService.updateUser(id, user);
        // 將 id 注入到user物件中，有助於前端判讀
        user.setId(id);
        String message = state ? "success" : "fail";
        ApiResponse<User> apiResponse = new ApiResponse<>(state, "update " + message, user);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> deleteUser(@PathVariable Integer id) {
        User user = null;
        try {
            // 查詢該 user 是否存在
            user = userService.getUser(id);
            // 刪除
            Boolean state = userService.deleteUser(id);
            String message = state ? "success" : "fail";
            // 回應資料
            ApiResponse<User> apiResponse = new ApiResponse<>(state, "delete " + message, user);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<User> apiResponse = new ApiResponse<>(false, e.getMessage(), user);
            return ResponseEntity.ok(apiResponse);
        }
    }

}
