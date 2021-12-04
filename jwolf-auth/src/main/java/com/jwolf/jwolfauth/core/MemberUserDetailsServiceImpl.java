package com.jwolf.jwolfauth.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 模拟从数据库查询,这里写死了3个测试用户——会员用户
     * 如果需要扩展字段implement UserDetails或extends User即可
     * @return
     */
    private List<User> selectUserFromDB() {
        List<User> userList = new ArrayList<>(8);
        userList.add(new User("memberuser1", passwordEncoder.encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("lv1")));
        userList.add(new User("memberuser2", passwordEncoder.encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("lv2")));
        userList.add(new User("memberuser3", passwordEncoder.encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("lv3")));
        return userList;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = this.selectUserFromDB().stream().filter(item -> item.getUsername().equals(username)).findAny();
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        //这里把查到的用户返回就可以了,spring security内部会进行密码比对
        return optionalUser.get();
    }
}
