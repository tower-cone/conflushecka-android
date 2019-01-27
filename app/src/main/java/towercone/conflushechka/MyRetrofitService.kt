package towercone.conflushechka

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface MyRetrofitService {
    @GET("sections")
    fun listSections(): Call<List<Section>>

    @GET("sections/{id}")
    fun getSection(@Path("id") id: String): Call<Section>
}

val myRetrofitService = Retrofit.Builder()
    .baseUrl("https://vast-mountain-64634.herokuapp.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(MyRetrofitService::class.java)

fun <T> Call<T>.call(context: Context, onSuccess: (T) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            context.showToast("Error: call failure")
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            val result = response.body()
            if (result != null)
                onSuccess(result)
            else
                context.showToast("Error: empty response")
        }
    })
}
