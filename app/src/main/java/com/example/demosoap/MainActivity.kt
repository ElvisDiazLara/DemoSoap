package com.example.demosoap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.ksoap2.HeaderProperty
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE


class MainActivity : AppCompatActivity() {

    private val NAMESPACE = "http://cencelgo.nubek.mx"
    private val METHOD_NAME = "News "

    private val SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME
    private val URL = "http://cencel.teria.mx/wscencelgodev/wscencelgo.asmx?wsdl"
    private var response: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        doAsync {
            performCall()
            uiThread {
                text_response.text = response
            }
        }

    }

    private fun performCall() {
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER12)
        envelope.dotNet = true
        envelope.implicitTypes = true
        envelope.isAddAdornments = false

        val request = SoapObject(NAMESPACE, METHOD_NAME)
        request.addProperty("op", "s")

        val headerProperties = ArrayList<HeaderProperty>()
        headerProperties.add(HeaderProperty("AuthHeader", "Username:android"))
        headerProperties.add(HeaderProperty("AuthHeader", "Password:e10adc3949ba59abbe56e057f20f883e"))

        envelope.setOutputSoapObject(request)

        val httpTransport = HttpTransportSE(URL)
        httpTransport.debug = true
        try {
            httpTransport.call(SOAP_ACTION, envelope, headerProperties)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        response = httpTransport.requestDump.toString()
    }
}
