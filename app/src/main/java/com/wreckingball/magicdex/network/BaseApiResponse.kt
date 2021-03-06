package com.wreckingball.magicdex.network

const val LOADING = 0
const val SUCCESS = 1
const val ERROR = 2

interface BaseApiResponse {
    var status: Int
    var message: String
}