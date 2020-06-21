package com.dhu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dhu.mapper.MemberMapper;
import com.dhu.pojo.Member;
import com.dhu.service.MemberService;
import com.dhu.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhou
 * @create 2020/5/27
 */
@Service(interfaceClass = MemberService.class) //暴露服务
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Member findByTelephone(String telephone) {
        return memberMapper.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if(password != null) {
            //MD5加密
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberMapper.add(member);
    }
}
