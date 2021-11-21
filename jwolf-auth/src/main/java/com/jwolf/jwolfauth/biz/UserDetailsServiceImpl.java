package com.jwolf.jwolfauth.biz;

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
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 模拟从数据库查询
     *
     * @return
     */
    private List<User> selectUserFromDB() {
        List<User> userList = new ArrayList<>(8);
        userList.add(new User("user1", passwordEncoder.encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("super")));
        userList.add(new User("user2", passwordEncoder.encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("admin")));
        userList.add(new User("user3", passwordEncoder.encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("common")));
        return userList;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = this.selectUserFromDB().stream().filter(item -> item.getUsername().equals(username)).findAny();
        if (optionalUser == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        //这里把查到的用户返回就可以了,spring security内部会进行密码比对
        return optionalUser.get();
    }
}
