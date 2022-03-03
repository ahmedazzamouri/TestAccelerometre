package udl.eps.testaccelerometre;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.widget.ScrollView;
import android.widget.TextView;


import udl.eps.testaccelerometre.listeners.AccelerometerSensor;
import udl.eps.testaccelerometre.listeners.LightSensor;

public class TestAccelerometreActivity extends Activity {
  private SensorManager sensorManager;
  private SensorEventListener lightListener;
  private SensorEventListener accelerometerListener;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    TextView topView = findViewById(R.id.ViewTop);
    ScrollView botView = findViewById(R.id.ViewBot);
    TextView accStatus = findViewById(R.id.acceleratorSensorStatus);
    TextView lightStatus = findViewById(R.id.LightSensorStatus);
    topView.setBackgroundColor(Color.GREEN);
    botView.setBackgroundColor(Color.YELLOW);

    lightListener = new LightSensor(this);
    accelerometerListener = new AccelerometerSensor(this);
    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    if (sensorManager != null && sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
      accStatus.setText(getResources().getString(R.string.accelerometer_sensor_good));
      showSensorData(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
      sensorManager.registerListener(accelerometerListener,
              sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
              SensorManager.SENSOR_DELAY_NORMAL);

    } else {
      accStatus.setText(getResources().getString(R.string.accelerometer_sensor_bad));
    }

    if (sensorManager != null && sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
      lightStatus.setText(String.format(getString(R.string.light_sensor_good),
              sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT).getMaximumRange()));
      sensorManager.registerListener(lightListener,
              sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
              SensorManager.SENSOR_DELAY_NORMAL);

    } else {
      lightStatus.setText(R.string.light_sensor_bad);
    }
  }

  private void showSensorData(Sensor sensor){
    showSensorData(sensor.getResolution(), sensor.getMaximumRange(), sensor.getPower());
  }

  private void showSensorData(float resolution, float range, float power){
    TextView sensorData = findViewById(R.id.acceleratorSensorData);
    sensorData.setText(String.format(getString(R.string.accelerometer_sensor_data),resolution, range, power));

  }

  @Override
  protected void onPause() {
    // unregister listener
    super.onPause();
    sensorManager.unregisterListener(lightListener);
    sensorManager.unregisterListener(accelerometerListener);
  }

  @Override
  protected void onStop() {
    super.onStop();
    sensorManager.unregisterListener(lightListener);
    sensorManager.unregisterListener(accelerometerListener);
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    if (sensorManager != null && sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null)
      sensorManager.registerListener(lightListener, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
    if (sensorManager != null && sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
      sensorManager.registerListener(accelerometerListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
  }
}