package pe.openlab.bombita

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View.VISIBLE
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var mSensorManager: SensorManager
    private lateinit var mSensorListener: ShakeEventListener

    private var jugadores = listOf("Bryam", "Greisy", "Jhoan", "Chinina")
    private var turno = 0

    private val todito = listOf("TOMAS", "ORDENAS", "DERECHA", "IZQUIERDA", "TODOS", "ORDENAS")
    private val colors = listOf(R.color.TOMAS, R.color.ORDENAS, R.color.DERECHA, R.color.IZQUIERDA, R.color.TODOS, R.color.ORDENAS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorListener = ShakeEventListener()

        mSensorListener.setOnShakeListener(object : ShakeEventListener.OnShakeListener {

            override fun onShake() {
                val position = Random().nextInt(todito.size)
                lyShake.setBackgroundColor(ContextCompat.getColor(baseContext, colors[position]))
                tvJugador.visibility = VISIBLE
                tvTodito.text = todito[position]
                tvJugador.text = jugadores[turno]
                if (turno == jugadores.size-1) {
                    turno = 0
                } else {
                    turno++
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause()
    }

}
