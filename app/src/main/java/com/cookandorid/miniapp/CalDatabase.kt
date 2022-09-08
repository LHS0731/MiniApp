package com.cookandorid.miniapp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cookandorid.miniapp.dao.HistoryDao
import com.cookandorid.miniapp.model.History

@Database(entities = [History::class], version = 1)
//버전을 작성하는 이유는 앱을 업데이트 할수록 db가 변경이 될수있는데
//버전1에서 버전2로 넘어갈때 migration을 해줘서 데이터 테이블 구조가 변경되어도 사용가능하게함
abstract class CalDatabase : RoomDatabase(){
    abstract fun historyDao(): HistoryDao
}

//데이터베이스 정의에 해당하는 파일입니다.