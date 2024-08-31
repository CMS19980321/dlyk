package com.hncu.service;

import com.github.pagehelper.PageInfo;
import com.hncu.model.TUser;
import com.hncu.query.UserQuery;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/6/9 10:20
 * @Version 1.0
 */
public interface UserService extends UserDetailsService {
    PageInfo<TUser> getUserByPage(Integer current);

    TUser getUserById(Integer id);

    int saveUser(UserQuery userquery);

    int updateUser(UserQuery userquery);

    int delUserById(Integer id);


    int batchDelUserByIds(List<String> idList);

    List<TUser> getOwnerList();
}
