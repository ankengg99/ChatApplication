package com.example.chatApplication.dao;

import com.example.chatApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query(value="select * from tbl_user where user_name=:username and status_id=1", nativeQuery=true)
    public List<User> findByUserName(String username);
    @Query(value="select * from tbl_user where user_id=:userId and status_id=1", nativeQuery=true)
    public List<User> findUserByUserId(int userId);
    @Query(value="select * from tbl_user where status_id=1", nativeQuery=true)
    @Override
    public List<User> findAll();
    @Modifying
  @Transactional
    @Query(value="update tbl_user set status_id=2 where user_id=:userId",countQuery = "select count(*) from tbl_user",nativeQuery=true)
    public void deleteUserByUserId(int userId);

}
