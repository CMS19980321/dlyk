package com.hncu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hncu.constant.Constants;
import com.hncu.manager.RedisManager;
import com.hncu.mapper.TPermissionMapper;
import com.hncu.mapper.TRoleMapper;
import com.hncu.mapper.TUserMapper;
import com.hncu.model.TPermission;
import com.hncu.model.TRole;
import com.hncu.model.TUser;
import com.hncu.query.BaseQuery;
import com.hncu.query.UserQuery;
import com.hncu.service.UserService;
import com.hncu.util.CacheUtils;
import com.hncu.util.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/6/9 10:21
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private TUserMapper tUserMapper;

    @Resource
    //@Autowired
    private PasswordEncoder passwordEncode;

    @Resource
    private TRoleMapper tRoleMapper;

    @Resource
    private RedisManager redisManager;

    @Resource
    private TPermissionMapper tPermissionMapper;



    /**
     * 登陆查询
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TUser tUser = tUserMapper.selectByLoginAct(username);
        if (tUser == null) {
            throw new UsernameNotFoundException("登陆账号不存在");
        }

        //查询当前用户的角色
        //查询一下用户的角色
        List<TRole> tRoleList = tRoleMapper.selectByUserId(tUser.getId());
        List<String> roleStringList = new ArrayList<>();

        tRoleList.forEach( tRole -> {
            if (StringUtils.hasText(tRole.getRole())) {
                roleStringList.add(tRole.getRole());
            }
        });

        tUser.setRoleList(roleStringList); //设置用户的角色

        //查询用户的菜单权限
        List<TPermission> menuPermissionList = tPermissionMapper.selectMenuPermissionByUserId(tUser.getId());
        tUser.setMneuPermissionList(menuPermissionList);

        //查询用户的功能权限
        List<TPermission> buttonPermissionList = tPermissionMapper.selectButtonPermissionByUserId(tUser.getId());

        List<String> stringPermissionList = new ArrayList<>();

        buttonPermissionList.forEach(tPermission -> {
            stringPermissionList.add(tPermission.getCode());
        });

        tUser.setPermissionList(stringPermissionList);//设置用户的权限标识符


        return tUser;
    }

    @Override
    public PageInfo<TUser> getUserByPage(Integer current) {
        //1.设置PageHelper
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        //2.查询
        List<TUser> list = tUserMapper.selectUserByPage(BaseQuery.builder().build());
        //3.封装分页数据到PageInfo
        PageInfo<TUser> info = new PageInfo<>(list);

        return info;
    }

    @Override
    public TUser getUserById(Integer id) {
        return tUserMapper.selectById(id);
    }

    //增删改操作，异常时加上事务回滚
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveUser(UserQuery userquery) {

        TUser tUser = new TUser();
        //将UserQuery对象中的属性数据复制到TUser对象中(复制要求:两个对象的属性名相同，属性类型要相同，这样才能进行复制)
        BeanUtils.copyProperties(userquery,tUser);

        //密码加密
        tUser.setLoginPwd(passwordEncode.encode(userquery.getLoginPwd()));
        tUser.setCreateTime(new Date());

        //解析token，获取登陆人id
        Integer loginUserId = JWTUtils.parseJWTByUserId(userquery.getToken());
        tUser.setCreateBy(loginUserId);

        //对应sql语句存在空值判断，空值不插入
        return tUserMapper.insertSelective(tUser);
    }

    //增删改操作，异常时加上事务回滚
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateUser(UserQuery userquery) {
        TUser tUser = new TUser();
        //将UserQuery对象中的属性数据复制到TUser对象中(复制要求:两个对象的属性名相同，属性类型要相同，这样才能进行复制)
        BeanUtils.copyProperties(userquery,tUser);

        //密码加密
        if (StringUtils.hasText(userquery.getLoginPwd())) {
            tUser.setLoginPwd(passwordEncode.encode(userquery.getLoginPwd()));
        }

        tUser.setEditTime(new Date());


        //解析token，获取登陆人id
        Integer loginUserId = JWTUtils.parseJWTByUserId(userquery.getToken());
        tUser.setEditBy(loginUserId);

        //传入空值不更新
        return tUserMapper.updateByPrimaryKeySelective(tUser);
    }

    //增删改操作，异常时加上事务回滚
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int delUserById(Integer id) {
        return tUserMapper.deleteByPrimaryKey(id);
    }

    //增删改操作，异常时加上事务回滚
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int batchDelUserByIds(List<String> idList) {
        return tUserMapper.deleteByIds(idList);
    }

    @Override
    public List<TUser> getOwnerList() {
        //1.从redis查询
        //2.redis中查询不到数据，就从数据库中查询，并且把数据放入到redis中
        return CacheUtils.getCacheData(
                () -> {
                    //Lambda 表达式，生产，从redis中获取数据
                    return (List<TUser>)redisManager.getValue(Constants.REDIS_OWNER_KEY);
                },
                () ->{
                    //Lambda 表达式，生产，从mysql中获取数据
                    return (List<TUser>)tUserMapper.selectByOwner();
                },
                (t) ->{
                    //消费，把数据放入到缓存Redis中
                    redisManager.setValue(Constants.REDIS_OWNER_KEY,t);
                }
        );
    }

}
