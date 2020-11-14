package com.example.finalproject.models.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finalproject.models.Account;

import java.util.List;

@Dao
public interface AccountDAO {
    @Insert
    void addAccount(Account account);

    @Query("select * from account")
    List<Account> getAllAccount();

    @Query("select * from account limit 1")
    Account getFirstAccount();

    @Query("select * from account where id = :id")
    Account getAccountById(String id);

    @Update
    void updateAccount(Account account);

    @Query("delete from account")
    void deleteAll();
}
