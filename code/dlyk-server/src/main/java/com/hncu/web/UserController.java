package com.hncu.web;

import com.github.pagehelper.PageInfo;
import com.hncu.model.TUser;
import com.hncu.query.UserQuery;
import com.hncu.result.R;
import com.hncu.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/6/15 10:02
 * @Version 1.0
 */

@RestController
public class UserController {


    @Resource
    private UserService userService;

    @GetMapping("/api/login/info")
    public R loginInfo(Authentication authentication){
        TUser tUser = (TUser)authentication.getPrincipal();
        return R.OK(tUser);
    }

    /**
     *
     * 进入该接口时存在过滤器验证token，此处不作任何处理
     */
    @GetMapping("/api/login/free")
    public R freeInfo(){
        return R.OK();
    }


    /**
     * 用户信息分页查询
     * @param current
     * @return
     */
    @PreAuthorize(value = "hasAnyAuthority('user:list')")
    @GetMapping("/api/users")
    public R userPage(@RequestParam(value = "current" ,required = false) Integer current){
        // 传入参数为非必须传入
        if (current == null) {
            current = 1;
        }

        PageInfo<TUser> pageInfo = userService.getUserByPage(current);
        return R.OK(pageInfo);
    }

    @PreAuthorize(value = "hasAnyAuthority('user:list')")
    @GetMapping("/api/user/{id}")
    public R userDetail(@PathVariable(value = "id") Integer id){
        TUser tUser = userService.getUserById(id);
        return R.OK(tUser);
    }

    /**
     *  添加用户
     * @return R
     */
    @PreAuthorize(value = "hasAnyAuthority('user:view')")
    @PostMapping("/api/user")
    public R addUser(UserQuery userquery , @RequestHeader(value = "Authorization") String token){
        userquery.setToken(token);
        int save = userService.saveUser(userquery);
        return save >= 1 ? R.OK() : R.FAIL();
    }

    /**
     *  编辑用户
     * @return R
     */
    @PreAuthorize(value = "hasAnyAuthority('user:edit')")
    @PutMapping("/api/user")
    public R editUser(UserQuery userquery , @RequestHeader(value = "Authorization") String token){
        userquery.setToken(token);
        int update = userService.updateUser(userquery);
        return update >= 1 ? R.OK() : R.FAIL();
    }

    /**
     *  删除用户
     * @return R
     */
    @PreAuthorize(value = "hasAnyAuthority('user:delete')")
    @DeleteMapping("/api/user/{id}")
    public R delUser(@PathVariable(value = "id") Integer id){
        int del= userService.delUserById(id);
        return del >= 1 ? R.OK() : R.FAIL();
    }

    /**
     *  删除用户
     * @return R
     */
    @PreAuthorize(value = "hasAnyAuthority('user:delete')")
    @DeleteMapping("/api/user")
    public R batchDelUser(@RequestParam(value = "ids") String ids){
        List<String> idList = Arrays.asList(ids.split(","));
        int batchDel= userService.batchDelUserByIds(idList);
        return batchDel >= idList.size() ? R.OK() : R.FAIL();
    }

    /**
     * 加载负责人
     * @return
     */
    @GetMapping(value = "/api/owner")
    public R owner(){
        List<TUser> ownerList= userService.getOwnerList();
        return R.OK(ownerList);
    }
}
