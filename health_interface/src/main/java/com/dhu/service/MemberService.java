package com.dhu.service;

import com.dhu.pojo.Member;

/**
 * @author zhou
 * @create 2020/5/27
 */
public interface MemberService {
    //根据手机号查询会员
    Member findByTelephone(String telephone);

    void add(Member member);
}
