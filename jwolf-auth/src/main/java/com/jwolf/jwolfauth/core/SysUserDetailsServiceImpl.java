package com.jwolf.jwolfauth.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SysUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

     /**
     * 模拟从数据库查询,这里写死了3个测试用户——系统用户
     * 如果需要扩展字段implement UserDetails或extends User即可
     * @return
     */
    private List<SysUser> selectUserFromDB() {
        List<SysUser> userList = new ArrayList<>(8);
        userList.add(new SysUser(1L,"sysuser1", passwordEncoder.encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("super")));
        userList.add(new SysUser(2L,"sysuser2", passwordEncoder.encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("admin")));
        userList.add(new SysUser(3L,"sysuser3", passwordEncoder.encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("common")));
        return userList;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SysUser> optionalUser = this.selectUserFromDB().stream().filter(item -> item.getUsername().equals(username)).findAny();
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        //这里把查到的用户返回就可以了,spring security内部会进行密码比对
        return optionalUser.get();
    }
}
