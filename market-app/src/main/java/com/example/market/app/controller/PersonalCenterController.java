package com.example.market.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.market.common.base.entity.ResultMessage;
import com.example.market.common.base.entity.ro.RoUser;
import com.example.market.common.base.entity.vo.VoStorageUser;
import com.example.market.common.base.entity.vo.VoUser;
import com.example.market.common.base.service.ConstantService;
import com.example.market.common.base.web.AbstractController;
import com.example.market.common.mysql.entity.User;
import com.example.market.common.mysql.service.SignRecordService;
import com.example.market.common.mysql.service.UserImgService;
import com.example.market.common.mysql.service.UserService;
import com.example.market.common.utils.DateUtils;
import com.example.market.common.utils.RegularMatchUtils;
import com.example.market.common.utils.change.VoChangeEntityUtils;
import com.example.market.security.aop.DistributedLock;
import com.example.market.security.aop.SaveLog;
import com.example.market.security.base.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/23 1:12
 * description:
 */
@Api(tags = "个人中心")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("app/personalCenter")
public class PersonalCenterController extends AbstractController implements CurrentUser, ConstantService {

    private final UserService userService;

    private final UserImgService userImgService;

    private final SignRecordService signRecordService;

    @ApiOperation(value = "显示用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findUserInfo")
    public ResultMessage findUserInfo() {
        RoUser user = userService.findRoUserByUserId(getCurrentUserId());
        return success(user);
    }

    @ApiOperation(value = "保存用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveUserInfo")
    @SaveLog(desc = "保存用户信息")
    @DistributedLock
    public ResultMessage saveUserInfo(@RequestBody VoStorageUser voStorageUser) {
        User user = VoChangeEntityUtils.changeUser(voStorageUser);
        user.setId(getCurrentUserId());
        RoUser roUser = userService.saveUser(user);
        return success(roUser);
    }

    @ApiOperation(value = "保存用户图片信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveUserImg")
    @SaveLog(desc = "保存用户图片信息")
    @DistributedLock
    public ResultMessage saveUserImg(HttpServletRequest request) {
        userImgService.saveUserImg(request, getCurrentUserId());
        return success("保存成功");
    }

    @ApiOperation(value = "用户签到")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "signIn")
    @SaveLog(desc = "用户签到")
    @DistributedLock
    public ResultMessage signIn() {
        signRecordService.signIn(getCurrentUserId());
        return success("保存成功");
    }

    @ApiOperation(value = "签到日历")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "signInCalendar")
    public ResultMessage signInCalendar(@Pattern(regexp = RegularMatchUtils.YYYY_MM_DD, message = "时间格式错误，时间格式：yyyy-MM-dd")
                                        @RequestParam String dateMonth) {
        List<JSONObject> result = signRecordService.signInCalendar(getCurrentUserId(), DateUtils.parseDate(dateMonth));
        return success(result);
    }


    ///////////////////////////////////////////////////////////////////////////
    // 管理员操作
    ///////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "保存学生会用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveUser")
    @SaveLog(desc = "保存学生会用户")
    @DistributedLock
    public ResultMessage saveUser(@NotBlank(message = "手机号不能为空")
                                                  @Pattern(regexp = "^1([34578])\\d{9}$", message = "手机号码格式错误")
                                                  @RequestParam String phone,
                                                  @NotBlank(message = "密码不能为空") @RequestParam String password) {
        userService.saveUser(phone, password);
        return success("保存成功");
    }

    @ApiOperation(value = "展示用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findUser")
    public ResultMessage findUser(@Valid @RequestBody VoUser voUser) {
        PageImpl<RoUser> page = userService.findRoUser(voUser);
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    @ApiOperation(value = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "deleteUser")
    @SaveLog(desc = "删除用户")
    @DistributedLock
    public ResultMessage deleteUser(@NotNull(message = "用户id不能为空") @RequestParam Long userId) {
        userService.deleteUser(userId);
        return success("删除成功");
    }

    @ApiOperation(value = "冻结用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "normalUser")
    @SaveLog(desc = "冻结用户")
    @DistributedLock
    public ResultMessage normalUser(@NotNull(message = "用户id不能为空") @RequestParam Long userId) {
        userService.normalUser(userId);
        return success("冻结成功");
    }
}
