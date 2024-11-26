package com.springboot.demo.controller;

import com.springboot.demo.dto.LoginRequest;
import com.springboot.demo.dto.LoginResponse;
import com.springboot.demo.dto.user.*;
import com.springboot.demo.exception.DemoBaseException;
import com.springboot.demo.exception.ErrorCode;
import com.springboot.demo.response.ApiResult;
import com.springboot.demo.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "登录", description = "登录")
@RestController
@RequestMapping("/service-a")
@Slf4j
public class LoginController {

    @Autowired
    private JwtUtils jwtUtils;

    @Operation(summary = "登录", description = "登录")
    @PostMapping("/login/account")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {

        System.out.println("loginRequest: " + loginRequest);

        // 1. 校验账户密码
        if (!validateCredentials(loginRequest.getUsername(), loginRequest.getPassword())) {
            throw new DemoBaseException(ErrorCode.UnauthorizedException_ErrorCode, HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        // 2. 生成JWT token
        String token = jwtUtils.generateToken(loginRequest.getUsername());

        // 3. 返回登录结果
        return new LoginResponse(
                "ok",                     // status
                "account",               // type
                token                // currentAuthority - 暂时硬编码为admin,实际应该根据用户角色设置
        );
    }

    @Operation(summary = "登出", description = "登出")
    @PostMapping("/login/outLogin")
    public ApiResult<Object> outLogin() {
        return ApiResult.ok().body(null);
    }

    @GetMapping("/parse-tenant")
    public ApiResult<Object> parseTenant(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            if (jwtUtils.validateToken(token)) {
                String tenant = jwtUtils.getTenantFromToken(token);
                // 使用tenant进行后续处理
                return ApiResult.ok().body("Tenant: " + tenant);
            }
        } else {
            return ApiResult.error("Invalid token format");
        }
        return null;
    }

    @GetMapping("/currentUser")
    public ApiResult<Object> currentUser(@RequestParam(required = false) String token) {
        System.out.println("currentUser : void " + token);
        UserTag tag1 = new UserTag("0", "很有想法的");
        UserTag tag2 = new UserTag("1", "专注设计");
        UserTag tag3 = new UserTag("2", "辣~");
        UserTag tag4 = new UserTag("3", "大长腿");
        UserTag tag5 = new UserTag("4", "川妹子");
        UserTag tag6 = new UserTag("5", "海纳百川");
        List<UserTag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
        tags.add(tag3);
        tags.add(tag4);
        tags.add(tag5);
        tags.add(tag6);

        Province province = new Province("江苏省", "330000");
        City city = new City("南京市", "330100");
        Geographic geographic = new Geographic(province, city);

        User user = new User("孔祥俊", "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png",
                "00000001", "123@digi.com", "海纳百川，有容乃大", "交互专家",
                "鼎捷－UED", tags, 12, 11, "China", "admin", geographic,
                "南京 777 号", "025-888888888");

        return ApiResult.ok().body(user);
    }

    /**
     * 校验用户名和密码
     */
    private boolean validateCredentials(String username, String password) {
        return true;
    }

}
