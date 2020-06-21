package com.dhu.mapper;

import com.dhu.pojo.Member;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @author zhou
 * @create 2020/5/26
 */
public interface MemberMapper {
    public List<Member> findAll();
    public Page<Member> selectByCondition(String queryString);
    public void add(Member member);
    public void deleteById(Integer id);
    public Member findById(Integer id);
    public Member findByTelephone(String telephone);
    public void edit(Member member);
    public Integer findMemberCountBeforeDate(String date);
    public Integer findMemberCountByDate(String date);
    public Integer findMemberCountAfterDate(String date);
    public Integer findMemberTotalCount();
}
