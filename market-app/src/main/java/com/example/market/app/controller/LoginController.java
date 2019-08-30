package com.example.market.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.market.common.base.entity.ResultMessage;
import com.example.market.common.base.web.AbstractController;
import com.example.market.common.constant.SysConst;
import com.example.market.common.exception.custom.OperationException;
import com.example.market.common.mysql.entity.User;
import com.example.market.common.mysql.service.UserService;
import com.example.market.common.service.VerificationCodeService;
import com.example.market.common.utils.NetworkUtil;
import com.example.market.common.validation.NoticeType;
import com.example.market.security.aop.DistributedLock;
import com.example.market.security.aop.SaveLog;
import com.example.market.security.shiro.service.CommonLoginService;
import com.example.market.security.shiro.token.PasswordToken;
import com.example.market.security.shiro.utils.RequestResponseUtil;
import com.google.common.base.Objects;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/28 17:38
 * description: 登录接口
 */

@Api(tags = "用户登录")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("app/login")
public class LoginController extends AbstractController {

    private final CommonLoginService commonLoginService;

    private final UserService userService;

    private final VerificationCodeService verificationCodeService;

    private static final String deviceInfo = SysConst.DeviceInfo.MOBILE.getType();

    ///////////////////////////////////////////////////////////////////////////
    // 账户密码登录
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 登录
     *
     * @param username       用户名
     * @param password       密码
     * @param registrationId 极光推送，用户设备标识
     * @return ResultMessage
     */
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", dataType = "String", defaultValue = "15130097582"),
            @ApiImplicitParam(paramType = "query", name = "password", dataType = "String", defaultValue = "15130097582"),
            @ApiImplicitParam(paramType = "query", name = "registrationId", dataType = "String")
    })
    @PostMapping(value = "login")
    @SaveLog(desc = "app登录")
    @DistributedLock
    public ResultMessage login(HttpServletRequest request,
                               @NotBlank(message = "账户不能为空") @RequestParam String username,
                               @NotBlank(message = "密码不能为空") @RequestParam String password,
                               @NotBlank(message = "用户设备标识不能为空") @RequestParam String registrationId) {
        try {
            String ip = NetworkUtil.getLocalIp(RequestResponseUtil.getRequest(request));
            String LoginType = SysConst.LoginType.AJAX.getType();
            PasswordToken token = new PasswordToken(username, password, LoginType);
            String accessToken = commonLoginService.login(token, Boolean.TRUE, ip, deviceInfo, registrationId);
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            JSONObject result = new JSONObject();
            result.put("accessToken", accessToken);
            result.put("accountType", user.getAccountType());
            return success("登录成功", result);
        } catch (Exception e) {
            return failure(e.getMessage());
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 验证码登录
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 验证码登录
     *
     * @param request          request
     * @param noticeContent    通知内容
     * @param verificationCode 验证码
     * @param noticeType       通知类型{@link SysConst.NoticeType}
     * @return ResultMessage
     */
    @ApiOperation(value = "验证码登录")
    @PostMapping(value = "verificationCodeLogin")
    @SaveLog(desc = "验证码登录")
    public ResultMessage verificationCodeLogin(HttpServletRequest request,
                                               @NotBlank(message = "手机号或者邮件不能为空") @RequestParam(value = "noticeContent") String noticeContent,
                                               @NotBlank(message = "验证码不能为空") @RequestParam(value = "verificationCode") String verificationCode,
                                               @NoticeType @NotBlank(message = "验证类型不能为空") @RequestParam(value = "noticeType") String noticeType) {
        try {
            String ip = NetworkUtil.getLocalIp(RequestResponseUtil.getRequest(request));
            String LoginType = SysConst.LoginType.VERIFICATION_CODE.getType();
            PasswordToken token = new PasswordToken(noticeContent, "888888", true, verificationCode, noticeType, LoginType);
            String accessToken = commonLoginService.login(token, true, ip, deviceInfo);
            JSONObject result = new JSONObject();
            result.put("accessToken", accessToken);
            return success("登录成功", result);
        } catch (Exception e) {
            return failure(e.getMessage());
        }
    }


    @ApiOperation(value = "发送验证码执行登录操作")
    @PostMapping(value = "sendVerificationCodeByLogin")
    @SaveLog(desc = "发送验证码执行登录操作")
    public ResultMessage sendVerificationCodeByLogin(HttpServletRequest request,
                                                     @NotBlank(message = "手机号或者邮件不能为空") @RequestParam(value = "noticeContent") String noticeContent,
                                                     @NoticeType @NotBlank(message = "验证类型不能为空") @RequestParam(value = "noticeType") String noticeType) {
        if (Objects.equal(SysConst.NoticeType.PHONE.getType(), noticeType)) {
            userService.findByAccount(noticeContent);  //验证手机号的合法性
        } else {
            throw new OperationException("暂时只支持短信验证码登录");
        }

        String ip = NetworkUtil.getLocalIp(request);
        verificationCodeService.sendCode(ip, noticeContent, noticeType, SysConst.VerificationCodeType.LOGIN.getType());
        return success("发送成功");
    }


    ///////////////////////////////////////////////////////////////////////////
    // 注册
    ///////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "验证手机号执行注册操作")
    @PostMapping(value = "validateAccountByRegister")
    public ResultMessage validateAccountByRegister(@NotBlank(message = "手机号不能为空")
                                                   @Pattern(regexp = "^1([34578])\\d{9}$", message = "手机号码格式错误")
                                                   @RequestParam String account) {
        userService.validateAccountByRegister(account);
        return success();

    }

    @ApiOperation(value = "发送手机验证码执行注册操作")
    @PostMapping(value = "sendVerificationCodeByRegister")
    @SaveLog(desc = "发送手机验证码执行注册操作")
    public ResultMessage sendVerificationCodeByRegister(HttpServletRequest request,
                                                        @NotBlank(message = "手机号不能为空")
                                                        @Pattern(regexp = "^1([34578])\\d{9}$", message = "手机号码格式错误")
                                                        @RequestParam String account) {
        String ip = NetworkUtil.getLocalIp(request);
        userService.validateAccountByRegister(account);
        verificationCodeService.sendCode(ip, account, SysConst.NoticeType.PHONE.getType(), SysConst.VerificationCodeType.REGISTER.getType());
        return success("发送成功");
    }

    @ApiOperation(value = "注册")
    @PostMapping(value = "register")
    @SaveLog(desc = "注册")
    public ResultMessage register(@NotBlank(message = "手机号不能为空")
                                  @Pattern(regexp = "^1([34578])\\d{9}$", message = "手机号码格式错误")
                                  @RequestParam String account,
                                  @NotBlank(message = "验证码不能为空")
                                  @RequestParam String code,
                                  @NotBlank(message = "密码不能为空")
                                  @RequestParam String password) {
        boolean state = verificationCodeService.validateCode(account, SysConst.NoticeType.PHONE.getType(), code, SysConst.VerificationCodeType.REGISTER.getType());
        if (state) {
            userService.saveUser(account, password);
            return success("保存成功");
        } else {
            return failure("验证失败");
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 修改密码
    ///////////////////////////////////////////////////////////////////////////

    @PostMapping(value = "validateAccountCodeByForget")
    @ApiOperation(value = "检验手机验证码执行重置密码操作")
    public ResultMessage validateAccountCodeByForget(@NotBlank(message = "手机号不能为空")
                                                     @Pattern(regexp = "^1([34578])\\d{9}$", message = "手机号码格式错误")
                                                     @RequestParam String account) {
        userService.validateAccountByForget(account);
        return success();

    }

    @PostMapping(value = "sendVerificationCodeByForget")
    @ApiOperation(value = "发送手机验证码执行重置密码操作")
    @SaveLog(desc = "发送手机验证码执行重置密码操作")
    public ResultMessage sendVerificationCodeByForget(HttpServletRequest request,
                                                      @NotBlank(message = "手机号不能为空")
                                                      @Pattern(regexp = "^1([34578])\\d{9}$", message = "手机号码格式错误")
                                                      @RequestParam String account) {
        userService.validateAccountByForget(account);
        String ip = NetworkUtil.getLocalIp(request);
        verificationCodeService.sendCode(ip, account, SysConst.NoticeType.PHONE.getType(), SysConst.VerificationCodeType.FORGET.getType());
        return success("发送成功");
    }

    @PostMapping(value = "modifyPassword")
    @ApiOperation(value = "修改密码")
    @SaveLog(desc = "修改密码")
    @DistributedLock
    public ResultMessage modifyPassword(@NotBlank(message = "手机号不能为空")
                                        @Pattern(regexp = "^1([34578])\\d{9}$", message = "手机号码格式错误")
                                        @RequestParam String account,
                                        @NotBlank(message = "验证码不能为空")
                                        @RequestParam String code,
                                        @NotBlank(message = "密码不能为空") @RequestParam String password) {
        boolean state = verificationCodeService.validateCode(account, SysConst.NoticeType.PHONE.getType(), code, SysConst.VerificationCodeType.FORGET.getType());
        if (state) {
            userService.modifyPassword(account, password);
            return success("保存成功");
        } else {
            return failure("验证失败");
        }

    }
}
