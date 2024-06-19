package es.uji.al341571.overwatch2app.classes

import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import es.uji.al341571.overwatch2app.R
import okhttp3.*
import java.io.IOException
import java.io.InputStream

class Utils {

    private val client = OkHttpClient()

    fun loadSvgFromUrl(url: String, targetImageView: ImageView) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                // Manejo de la falla de la solicitud, por ejemplo, mostrar una imagen de error en el ImageView
                targetImageView.setImageResource(R.drawable.ic_error) // Asegúrate de tener un recurso de imagen de error adecuado
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                }

                // Obtener el cuerpo de la respuesta como InputStream
                val inputStream: InputStream? = response.body()?.byteStream()

                inputStream?.use {
                    try {
                        // Parsear el SVG usando androidsvg
                        val svg = SVG.getFromInputStream(it)

                        // Crear un drawable desde el SVG
                        val drawable: Drawable = PictureDrawable(svg.renderToPicture())

                        // Mostrar el SVG en el ImageView en el hilo principal
                        targetImageView.post {
                            targetImageView.setImageDrawable(drawable)
                        }
                    } catch (e: SVGParseException) {
                        e.printStackTrace()
                        // Manejo de errores al parsear el SVG
                        targetImageView.setImageResource(R.drawable.ic_error_svg_parse) // Asegúrate de tener un recurso de imagen adecuado para errores de SVG
                    }
                } ?: run {
                    throw IOException("Null response body")
                }
            }
        })
    }
}
