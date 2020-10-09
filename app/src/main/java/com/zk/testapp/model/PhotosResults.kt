/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zk.testapp.model

import java.lang.Exception

/**
 * PhotosResults from a network fetch, which contains List<Photo> holding query data,
 * and a String of network error state.
 */
sealed class PhotosResults {
    data class Success(val data: List<Photo>) : PhotosResults()
    data class Error(val error: Exception) : PhotosResults()
}