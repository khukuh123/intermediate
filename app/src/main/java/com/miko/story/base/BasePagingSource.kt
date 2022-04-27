package com.miko.story.base

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.miko.story.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single

abstract class BasePagingSource<V : Any> : PagingSource<Int, V>() {
    override fun getRefreshKey(state: PagingState<Int, V>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, V> =
        try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            when (val response = call(position, params.loadSize).single()) {
                is Resource.Success -> {
                    LoadResult.Page(
                        data = response.data!!,
                        prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                        nextKey = if (response.data.isNullOrEmpty()) null else position + 1
                    )
                }
                else -> throw IllegalStateException(response.errorMessage)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    abstract suspend fun call(position: Int, loadSize: Int): Flow<Resource<List<V>>>

    companion object {
        private const val INITIAL_PAGE_INDEX = 1
    }
}