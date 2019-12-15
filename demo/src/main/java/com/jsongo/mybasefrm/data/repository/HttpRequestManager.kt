package com.jsongo.mybasefrm.data.repository

import com.jsongo.core.bean.DataWrapper
import com.jsongo.core.bean.toErrorDataWrapper
import com.jsongo.core.network.ApiManager
import com.jsongo.mybasefrm.bean.User
import com.jsongo.mybasefrm.data.api.ApiService

/**
 * @author ： jsongo
 * @date ： 2019/11/9 23:26
 * @desc :
 */

object HttpRequestManager : IRemoteRequest {

    /*@Override
    public void getSongsResult(MutableLiveData<SongResult.DataBean.SongsResult> liveData, String keyword) {

        //TODO 分页后续改为 paging 来处理，
        String url = String.format(APIs.SEARCH_SONG, 1, 20, keyword);

        OkGo.<SongResult>get(url)
                .execute(new JsonCallback<SongResult>() {
                    @Override
                    public void onSuccess(Response<SongResult> response) {
                        super.onSuccess(response);
                        liveData.setValue(response.body().getData().getSong());
                    }
                });
    }

    @Override
    public void getAlbumsResult(MutableLiveData<AlbumResult.DataBean.AlbumsResult> liveData, String keyword) {

        //TODO 分页后续改为 paging 来处理，
        String url = String.format(APIs.SEARCH_ALBUM, 1, 20, keyword);

        OkGo.<AlbumResult>get(url)
                .execute(new JsonCallback<AlbumResult>() {
                    @Override
                    public void onSuccess(Response<AlbumResult> response) {
                        super.onSuccess(response);
                        liveData.setValue(response.body().getData().getAlbum());
                    }
                });
    }

    @Override
    public void getSingerImg(MutableLiveData<SingerImg.SingerResult> liveData, String singerName) {

        String url = String.format(APIs.SINGLE_IMG, singerName);

        OkGo.<SingerImg>post(url)
                .headers(APIs.HEADER_KEY_OF_USER_AGENT, APIs.HEADER_VALUE_OF_USER_AGENT)
                .execute(new JsonCallback<SingerImg>() {
                    @Override
                    public void onSuccess(Response<SingerImg> response) {
                        super.onSuccess(response);
                        liveData.setValue(response.body().getResult());
                    }
                });
    }

    @Override
    public void getSongInfo(MutableLiveData<SongInfo.DataBean> liveData, String albumMid) {

        String url = String.format(APIs.ALBUM_DETAIL, albumMid);

        OkGo.<SongInfo>get(url)
                .execute(new JsonCallback<SongInfo>() {
                    @Override
                    public void onSuccess(Response<SongInfo> response) {
                        super.onSuccess(response);
                        liveData.setValue(response.body().getData());
                    }
                });
    }

    @Override
    public void getSongUrl(MutableLiveData<String> liveData, String songMid) {

        String url = APIs.SONG_URL.replace("replaceHere", songMid);

        OkGo.<SongUrl>get(url)
                .execute(new JsonCallback<SongUrl>() {
                    @Override
                    public void onSuccess(Response<SongUrl> response) {
                        super.onSuccess(response);
                        if (response.body() != null
                                && response.body().getReq_0() != null
                                && response.body().getReq_0().getData() != null) {

                            SongUrl.Req0Bean.DataBean.MidurlinfoBean midurlinfoBean =
                                    response.body().getReq_0().getData().getMidurlinfo().get(0);
                            String baseUrl = response.body().getReq_0().getData().getSip().get(0);

                            liveData.setValue(baseUrl + midurlinfoBean.getPurl());
                        }
                    }
                });
    }*/

    /*fun getFreeMusic(liveData: MutableLiveData<TestAlbum>) {

        val gson = Gson()
        val type = object : TypeToken<TestAlbum>() {

        }.type
        val testAlbum = gson.fromJson(Utils.getApp().getString(R.string.free_music_json), type)

        liveData.setValue(testAlbum)
    }*/

    @Throws
    override suspend fun checkUser(username: String, password: String): String {
        val checkUserWrapper: DataWrapper<String?>
        try {
            checkUserWrapper =
                ApiManager.createApiService(ApiService::class.java).checkUser(username, password)
        } catch (e: Exception) {
            throw NetFailedException(e.message.toErrorDataWrapper())
        }
        if (checkUserWrapper.code > 0 && !checkUserWrapper.data.isNullOrEmpty()) {
            return checkUserWrapper.data!!
        } else {
            throw NetFailedException(checkUserWrapper.toErrorDataWrapper())
        }
    }

    @Throws
    override suspend fun getUserInfo(userguid: String): User {
        val userWrapper: DataWrapper<User?>
        try {
            userWrapper =
                ApiManager.createApiService(ApiService::class.java).getUserInfo(userguid)
        } catch (e: Exception) {
            throw NetFailedException(e.message.toErrorDataWrapper())
        }
        if (userWrapper.code > 0 && userWrapper.data != null) {
            return userWrapper.data!!
        } else {
            throw NetFailedException(userWrapper.toErrorDataWrapper())
        }
    }

    /* fun getLibraryInfo(liveData: MutableLiveData<List<LibraryInfo>>) {
         val gson = Gson()
         val type = object : TypeToken<List<LibraryInfo>>() {

         }.type
         val list = gson.fromJson(Utils.getApp().getString(R.string.library_json), type)

         liveData.setValue(list)
     }
 */
}
