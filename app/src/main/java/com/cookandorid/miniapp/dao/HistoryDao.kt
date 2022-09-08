package com.cookandorid.miniapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cookandorid.miniapp.model.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    //히스토리라는 테이블에서 모든 엔티티를 가져오게된다.
    fun getAll(): List<History>

    @Insert
    fun insertHistory(history: History)

    @Query("DELETE FROM history")
    fun deleteAll()

//    @Delete
//    fun delete(history: History)
//
//    @Query("SELECT * FROM history WHERE result LIKE :result LIMIT 1")
//    fun findByResult(result: String): History

    // DAO(쿼리)정의 에 해당하는 파일입니다.
    //쿼리(Query) : 데이터베이스나 파일의 내용 중 원하는 내용을 검색하기 위하여
    //             몇 개의 코드(code)나 키(Key)를 기초로 질의하는 것을 말한다
}