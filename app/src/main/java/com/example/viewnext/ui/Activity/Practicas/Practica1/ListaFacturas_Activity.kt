package com.example.viewnext.ui.Activity.Practicas.Practica1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.infinum.retromock.Retromock
import com.example.viewnext.R
import com.example.viewnext.data.retrofit.FacturaApiService
import com.example.viewnext.data.retrofit.Facturas
import com.example.viewnext.data.retrofit.FacturasAdapter
import com.example.viewnext.data.retromock.ResourceBodyFactory
import com.example.viewnext.data.retromock.RetroMockFacturaApiService
import com.example.viewnext.data.room.AppDatabase
import com.example.viewnext.data.room.FacturaDao
import com.example.viewnext.data.room.FacturaEntity
import com.example.viewnext.ui.Activity.Principal_Activity
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListaFacturas_Activity : AppCompatActivity() {

    private lateinit var facturasApiResponse: List<Facturas.Factura>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FacturasAdapter
    private lateinit var facturasDao: FacturaDao
    private lateinit var service: FacturaApiService
    private lateinit var service2: RetroMockFacturaApiService


    private var fechaDesde = "07/02/2000"
    private var fechaHasta = "07/02/2024"
    private var importeMinimo = 0
    private var importeMaximo = 300
    private var pagadas = false
    private var anuladas = false
    private var cuotaFija = false
    private var pendientesPago = false
    private var planPago = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lista_facturas)
        recyclerView = findViewById(R.id.recyclerViewFacturas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setOnClickListener {
            val intent = Intent(this@ListaFacturas_Activity, Principal_Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        val database = AppDatabase.getDatabase(applicationContext)
        facturasDao = database.facturaDao()

        val switchRetrofit: Switch = findViewById(R.id.switch_retrofit)
        setupRetrofit()

        switchRetrofit.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setupRetroMock()
                loadFacturas2() 
            } else {
                setupRetrofit()
                loadFacturas()
            }
        }

        val btnFiltro = findViewById<ImageButton>(R.id.btnFiltro)
        btnFiltro.setOnClickListener {
            val intent = Intent(this, FiltroFacturaActivity::class.java)

            startActivity(intent)
        }


        val extras = intent.extras
        extras?.let {
            fechaDesde = it.getString("fechaDesde", "07/02/2000")
            fechaHasta = it.getString("fechaHasta", "07/02/2024")
            importeMinimo = it.getInt("importeMinimo", 0)
            importeMaximo = it.getInt("importeMaximo", Int.MAX_VALUE)
            pagadas = it.getBoolean("pagadas", false)
            anuladas = it.getBoolean("anuladas", false)
            cuotaFija = it.getBoolean("cuotaFija", false)
            pendientesPago = it.getBoolean("pendientesPago", false)
            planPago = it.getBoolean("planPago", false)
        }

        loadFacturas() // Cargar facturas inicialmente
    }

    private fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://viewnextandroid2.wiremockapi.cloud/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(FacturaApiService::class.java)
    }

    private fun setupRetroMock() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://viewnextandroid2.wiremockapi.cloud/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retromock = Retromock.Builder()
            .retrofit(retrofit)
            .defaultBodyFactory(ResourceBodyFactory())
            .build()
        service2 = retromock.create(RetroMockFacturaApiService::class.java)
    }

    private fun loadFacturas() {
        service.getFacturas().enqueue(object : Callback<Facturas.ApiResponse> {
            override fun onResponse(
                call: Call<Facturas.ApiResponse>,
                response: Response<Facturas.ApiResponse>
            ) {
                if (response.isSuccessful) {
                    facturasApiResponse = response.body()?.facturas ?: emptyList()
                    applyFiltersAndLoadAdapter()
                    Toast.makeText(
                        applicationContext,
                        "Vista con retrofit",
                        Toast.LENGTH_SHORT
                    ).show()


                    GlobalScope.launch {
                        facturasDao.deleteAllFacturas()
                        facturasApiResponse.forEach { factura ->
                            facturasDao.insertFactura(
                                FacturaEntity(
                                    fecha = factura.fecha,
                                    importeOrdenacion = factura.importeOrdenacion,
                                    descEstado = factura.descEstado
                                )
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Facturas.ApiResponse>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Error al cargar retrofit",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun loadFacturas2() {
        service2.getFacturas().enqueue(object : Callback<Facturas.ApiResponse> {
            override fun onResponse(
                call: Call<Facturas.ApiResponse>,
                response: Response<Facturas.ApiResponse>
            ) {
                if (response.isSuccessful) {
                    facturasApiResponse = response.body()?.facturas ?: emptyList()
                    applyFiltersAndLoadAdapter()
                    Toast.makeText(
                        applicationContext,
                        "Vista con retromock",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Guardar las facturas en la base de datos local
                    GlobalScope.launch {
                        facturasDao.deleteAllFacturas()
                        facturasApiResponse.forEach { factura ->
                            facturasDao.insertFactura(
                                FacturaEntity(
                                    fecha = factura.fecha,
                                    importeOrdenacion = factura.importeOrdenacion,
                                    descEstado = factura.descEstado
                                )
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Facturas.ApiResponse>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Error al cargar retromock",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun applyFiltersAndLoadAdapter() {

        val facturasFiltradas = facturasApiResponse.filter { factura ->
            val fechaFactura = factura.fecha
            val importeFactura = factura.importeOrdenacion
            val estadoFactura = factura.descEstado

            val fechaDentroRango = fechaFactura in fechaDesde..fechaHasta
            val importeDentroRango: Boolean = importeFactura >= importeMinimo && importeFactura <= importeMaximo
            val estadoCoincide = when {
                pagadas && estadoFactura.equals("Pagada", ignoreCase = true) -> true
                anuladas && estadoFactura.equals("Anulada", ignoreCase = true) -> true
                cuotaFija && estadoFactura.equals("Cuota Fija", ignoreCase = true) -> true
                pendientesPago && estadoFactura.equals("Pendiente de pago", ignoreCase = true) -> true
                planPago && estadoFactura.equals("Plan de Pago", ignoreCase = true) -> true
                else -> false
            }

            // Verificar si al menos una de las condiciones se cumple
            fechaDentroRango || importeDentroRango || estadoCoincide
        }


        adapter = FacturasAdapter(facturasFiltradas, this@ListaFacturas_Activity)
        recyclerView.adapter = adapter
    }



    fun onItemFacturaClicked() {
        showCustomAlertDialog()
    }

    private fun showCustomAlertDialog() {
        val builder = android.app.AlertDialog.Builder(this)

        builder.setTitle("Información")
        builder.setMessage("Esta funcionalidad no está disponible.")

        builder.setPositiveButton("Cerrar") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }
}
