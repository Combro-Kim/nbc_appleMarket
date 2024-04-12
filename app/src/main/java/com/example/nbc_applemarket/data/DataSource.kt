package com.example.nbc_applemarket.data

class DataSource { //todo SingleTon 이해하기
    companion object {
        private var INSTANCE: DataSource? = null
        fun getDataSource(): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource()
                INSTANCE = newInstance
                newInstance
            }
        }
    }
    fun getDataList() : MutableList<Data>{
        return dataList()
    }
}