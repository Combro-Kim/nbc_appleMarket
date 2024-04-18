package com.example.nbc_applemarket.data

import com.example.nbc_applemarket.adapter.DataAdapter

class DataSource { //todo SingleTon 이해
    private var dataList: MutableList<Data> = dataList()

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

    fun getDataList(): MutableList<Data> {
//        return dataList()
        return dataList
    }


    fun removeItem(position: Int) {
        if (position in 0 until dataList.size) {
            dataList.removeAt(position)
        }
    }

    /*    fun removeItem(position: Int) {
            if (position in 0 until dataList().size) {
                dataList().removeAt(position)
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, dataList().size)
            }
        }*/
}