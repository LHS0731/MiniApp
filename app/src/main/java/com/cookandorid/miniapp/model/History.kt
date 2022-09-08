package com.cookandorid.miniapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History(
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name = "expression") val expression: String?,
    @ColumnInfo(name = "result")val result: String?,
)

//DB Entity(테이블) 정의 에 해당하는 파일입니다