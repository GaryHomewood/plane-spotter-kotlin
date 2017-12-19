package uk.co.garyhomewood.planespotter.di


import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import timber.log.Timber
import uk.co.garyhomewood.planespotter.BuildConfig
import uk.co.garyhomewood.planespotter.PlaneSpotterApp
import uk.co.garyhomewood.planespotter.api.PlanesService
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * Create the service endpoint
 */
class Injector {

    companion object {
        private val BASE_URL = "https://www.planespotters.net/photos/latest/"
        private val PRAGMA = "Pragma"
        private val CACHE_CONTROL = "Cache-Control"
        private val USER_AGENT = "User-Agent"
        private val USER_AGENT_VALUE = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36"

        private fun addHttpLogging(): HttpLoggingInterceptor {
            val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Timber.d(message) })
            httpLoggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.HEADERS else HttpLoggingInterceptor.Level.NONE
            return httpLoggingInterceptor
        }

        private fun addRequestHeader(): Interceptor {
            return Interceptor { chain ->
                val request: Request

                // server requires a browser user agent
                val builder = chain.request().newBuilder()
                        .addHeader(USER_AGENT, USER_AGENT_VALUE)

                request = if (PlaneSpotterApp.instance.hasNetwork()) {
                    builder.build()
                } else {
                    val cacheControl = CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build()

                    builder
                            .cacheControl(cacheControl)
                            .build()
                }

                chain.proceed(request)
            }
        }

        private fun addResponseCacheHeader(): Interceptor {
            return Interceptor { chain ->
                val response = chain.proceed(chain.request())

                val cacheControl = CacheControl.Builder()
                        .maxAge(2, TimeUnit.MINUTES)
                        .onlyIfCached()
                        .maxStale(300, TimeUnit.SECONDS)
                        .build()

                response.newBuilder()
                        .removeHeader(PRAGMA)
                        .header(CACHE_CONTROL, cacheControl.toString())
                        .build()
            }
        }

        fun providePlanesService(): PlanesService {
            val cache = Cache(File(PlaneSpotterApp.instance.cacheDir, "http-cache"), 10 * 1024 * 1024)

            val client = OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(addHttpLogging())
                    .addInterceptor(addRequestHeader())
                    .addNetworkInterceptor(addResponseCacheHeader())
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

            return retrofit.create(PlanesService::class.java)
        }
    }
}