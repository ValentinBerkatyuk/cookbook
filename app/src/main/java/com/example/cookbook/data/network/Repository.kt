package com.example.cookbook.data.network

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource :RemoteDataSource,
    localDataSource: LocalDataSource
){
    val local=localDataSource
    val remote=remoteDataSource
}