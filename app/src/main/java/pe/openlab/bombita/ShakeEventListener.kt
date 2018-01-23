package pe.openlab.bombita

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 * Created by Bryam Soto on 22/01/2018.
 */
class ShakeEventListener : SensorEventListener {

    private val MIN_FORCE = 3

    private val MIN_DIRECTION_CHANGE = 2

    private val MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE = 400

    private val MIN_TOTAL_DURATION_OF_SHAKE = 1500 // 1 seconds

    private val ZERO: Long = 0

    private var mFirstDirectionChangeTime: Long = 0

    private var mLastDirectionChangeTime: Long = 0

    private var mDirectionChangeCount = 0

    private var lastX = 0f

    private var lastY = 0f

    private var lastZ = 0f

    private lateinit var mShakeListener: OnShakeListener

    fun setOnShakeListener(onShakeListener: OnShakeListener) {
        mShakeListener = onShakeListener
    }

    override fun onAccuracyChanged(se: Sensor, p1: Int) {

    }

    override fun onSensorChanged(se: SensorEvent) {

        val x = se.values[SensorManager.DATA_X]
        val y = se.values[SensorManager.DATA_Y]
        val z = se.values[SensorManager.DATA_Z]

        val totalMovement = Math.abs(x + y + z - lastX - lastY - lastZ)

        if (totalMovement > MIN_FORCE) {

            val now = System.currentTimeMillis()

            if (mFirstDirectionChangeTime == ZERO) {

                mFirstDirectionChangeTime = now
                mLastDirectionChangeTime = now

            }

            val lastChangeWasAgo: Long = now - mLastDirectionChangeTime

            if (lastChangeWasAgo < MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE) {

                mLastDirectionChangeTime = now
                mDirectionChangeCount++

                lastX = x
                lastY = y
                lastZ = z

                if (mDirectionChangeCount >= MIN_DIRECTION_CHANGE) {

                    val totalDuration: Long = now - mFirstDirectionChangeTime

                    if (totalDuration >= MIN_TOTAL_DURATION_OF_SHAKE) {

                        mShakeListener.onShake()
                        resetShakeParameters()

                    }

                }
            } else {

                resetShakeParameters()
            }

        }

    }

    private fun resetShakeParameters() {
        mFirstDirectionChangeTime = 0
        mDirectionChangeCount = 0
        mLastDirectionChangeTime = 0
        lastX = 0F
        lastY = 0F
        lastZ = 0F
    }

    interface OnShakeListener {
        fun onShake()
    }

}