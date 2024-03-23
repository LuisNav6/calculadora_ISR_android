package mx.lhnb.isr;
//declaramos todas las librerías necesarias
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.graphics.Color;
import android.content.Intent;
import android.widget.ToggleButton;
import android.widget.Button;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.widget.CompoundButton;
public class MainActivity extends AppCompatActivity {
    //declaramos las variables que más tarde referenciaremos a su respectivo componente
    private EditText ingresoEditText;
    private Spinner periodicidadSpinner;
    private TextView resultadoTextView;
    private TextView ingresoCalculadoTextView;
    private TextView limiteInferiorTextView;
    private TextView diferenciaTextView;
    private TextView tasaTextView;
    private TextView impuestoMarginalTextView;
    private TextView cuotaFijaTextView;
    private TextView impuestoRetenerTextView;
    private TextView percepcionMenosImpuestosTextView;
    private TextView periodicidadText;
    private ToggleButton colorToggleButton;
    private ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //establecemos el layout a trabajar
        //Damos su referencia a cada variable con su componente
        colorToggleButton = findViewById(R.id.colorToggleButton);
        constraintLayout = findViewById(R.id.constraintLayout);
        ingresoEditText = findViewById(R.id.ingresoEditText);
        periodicidadSpinner = findViewById(R.id.periodicidadSpinner);
        resultadoTextView = findViewById(R.id.resultadoTextView);
        ingresoCalculadoTextView = findViewById(R.id.ingresoCalculadoTextView);
        limiteInferiorTextView = findViewById(R.id.limiteInferiorTextView);
        diferenciaTextView = findViewById(R.id.diferenciaTextView);
        tasaTextView = findViewById(R.id.tasaTextView);
        impuestoMarginalTextView = findViewById(R.id.impuestoMarginalTextView);
        cuotaFijaTextView = findViewById(R.id.cuotaFijaTextView);
        impuestoRetenerTextView = findViewById(R.id.impuestoRetenerTextView);
        percepcionMenosImpuestosTextView = findViewById(R.id.percepcionMenosImpuestosTextView);
        periodicidadText = findViewById(R.id.periodicidadtexto);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.periodicidades_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodicidadSpinner.setAdapter(adapter);
        //aqui le declaramos la variable para el botón que nos servirá para reedireccionar o hacer el cambio de actividad
        Button botonRedireccion = findViewById(R.id.botonRedireccion);
        botonRedireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, newISR.class); //le decimos para donde se va a ir
                startActivity(intent); //con esta línea hacemos el cambio
            }
        });
//aqui declaramos el evento que nos hará el cambio de background
        colorToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int backgroundColor = isChecked ? Color.parseColor("#C0D2FF") : Color.WHITE; //definimos colores acorde a un ternario que evalua el estado de la variable del ToggleButton
                constraintLayout.setBackgroundColor(backgroundColor);
            }
        });
    }

//función madre del cálculo del ISR
    public void calcularISR(View view) {
        double ingreso = Double.parseDouble(ingresoEditText.getText().toString()); //extraemos lo ingresado por el usuario
        String periodicidad = periodicidadSpinner.getSelectedItem().toString();

//ponemos en variable las llamadas a las funciones necesarias para el cálculo, y que posteriormente se muestran en pantalla
        double limiteInferior = calcularLimiteInferior(ingreso, periodicidad);
        double diferencia = calcularDiferencia(ingreso, periodicidad);
        double tasa = calcularTasa(ingreso, periodicidad);
        double impuestoMarginal = calcularImpuestoMarginal(ingreso, periodicidad);
        double cuotaFija = calcularCuotaFija(ingreso, periodicidad);
        double impuestoRetener = calcularImpuestoRetener(ingreso, periodicidad);
        double percepcionMenosImpuestos = calcularPercepcionMenosImpuestos(ingreso, impuestoRetener);

        // Luego, muestro estos valores en los TextView correspondientes.
        ingresoCalculadoTextView.setText("Ingreso: $" + ingreso);
        limiteInferiorTextView.setText("Límite inferior: $" + limiteInferior);
        diferenciaTextView.setText("Diferencia: $" + diferencia);
        tasaTextView.setText("Tasa: " + tasa + "%");
        impuestoMarginalTextView.setText("Impuesto marginal: $" + impuestoMarginal);
        cuotaFijaTextView.setText("Cuota Fija: $" + cuotaFija);
        impuestoRetenerTextView.setText("Impuesto a retener: $" + impuestoRetener);
        percepcionMenosImpuestosTextView.setText("Percepción menos impuestos: $" + percepcionMenosImpuestos);
        periodicidadText.setText("Periodicidad: " + periodicidad);
    }

    private double calcularLimiteInferior(double ingreso, String periodicidad) {
        //solo se pondrán 3 limites por periodicidad, tomar en cuenta que se hace con fines de practicidad por lo que el resultado no será del completamente exacto

        double limiteInferior = 0.0;
        if("Diario".equals(periodicidad)){
            if(ingreso >= 0.01 && ingreso <= 208.29 ){
                limiteInferior = 0.01;
            }else if(ingreso >= 208.3 && ingreso <= 366.05){
                limiteInferior = 208.3;
            }else{
                limiteInferior = 366.06;
            }
        }
        if("Semanal".equals(periodicidad)){
            if(ingreso >= 0.01 && ingreso <= 1468.03 ){
                limiteInferior = 0.01;
            }else if(ingreso >= 1458.04 && ingreso <= 2562.35){
                limiteInferior = 1458.04;
            }else{
                limiteInferior = 2562.36;
            }
        }
        if("Decenal".equals(periodicidad)){
            if(ingreso >= 0.01 && ingreso <= 2082.9 ){
                limiteInferior = 0.01;
            }else if(ingreso >= 2082.91 && ingreso <= 3660.5){
                limiteInferior = 2082.91;
            }else{
                limiteInferior = 3660.51;
            }
        }
        if("Quincenal".equals(periodicidad)){
            if(ingreso >= 0.01 && ingreso <= 3124.35 ){
                limiteInferior = 0.01;
            }else if(ingreso >= 3124.36 && ingreso <= 6490.75){
                limiteInferior = 3124.36;
            }else{
                limiteInferior = 6490.76;
            }
        }
        if("Mensual".equals(periodicidad)){
            if(ingreso >= 0.01 && ingreso <= 6332.05 ){
                limiteInferior = 0.01;
            }else if(ingreso >= 6332.06 && ingreso <= 11128.01){
                limiteInferior = 208.3;
            }else{
                limiteInferior = 366.06;
            }
        }
        return limiteInferior;
    }

    private double calcularDiferencia(double ingreso, String periodicidad) {
        //simplemente es calcular el ingreso menos el limite inferior que te toca acorde a periodicidad
        double limiteInferior = calcularLimiteInferior(ingreso, periodicidad);
        return ingreso - limiteInferior;
    }

    private double calcularTasa(double ingreso, String periodicidad) {
        //la tasa ya viene establecida, y solo se toman 3 valores con fines de practicidad como se menciona anteriormente
        double tasa = 0.0;
        if("Diario".equals(periodicidad)){
            if(ingreso >= 0.01 && ingreso <= 208.29 ){
                tasa = 1.92;
            }else if(ingreso >= 208.3 && ingreso <= 366.05){
                tasa = 6.4;
            }else{
                tasa = 10.0;
            }
        }
        if("Semanal".equals(periodicidad)){
            if(ingreso >= 0.01 && ingreso <= 1468.03 ){
                tasa = 1.92;
            }else if(ingreso >= 1458.04 && ingreso <= 2562.35){
                tasa = 6.4;
            }else{
                tasa = 10.0;
            }
        }
        if("Decenal".equals(periodicidad)){
            if(ingreso >= 0.01 && ingreso <= 2082.9 ){
                tasa = 1.92;
            }else if(ingreso >= 2082.91 && ingreso <= 3660.5){
                tasa = 6.4;
            }else{
                tasa = 10.0;
            }
        }
        if("Quincenal".equals(periodicidad)){
            if(ingreso >= 0.01 && ingreso <= 3124.35 ){
                tasa = 1.92;
            }else if(ingreso >= 3124.36 && ingreso <= 6490.75){
                tasa = 6.4;
            }else{
                tasa = 10.0;
            }
        }
        if("Mensual".equals(periodicidad)){
            if(ingreso >= 0.01 && ingreso <= 6332.05 ){
                tasa = 1.92;
            }else if(ingreso >= 6332.06 && ingreso <= 11128.01){
                tasa = 6.4;
            }else{
                tasa = 10.0;
            }
        }
        return tasa;
    }

    private double calcularImpuestoMarginal(double ingreso, String periodicidad) {
        //impuesto que se paga adicional
        double tasa = calcularTasa(ingreso, periodicidad);
        double diferencia = calcularDiferencia(ingreso, periodicidad);
        double impuestoMarginal = diferencia * (tasa / 100);
        return impuestoMarginal;
    }

    private double calcularCuotaFija(double ingreso, String periodicidad) {
        //la cuota fija es otra variable ya definida, la cual solo se pone acorde a su periodicidad
        double cuotaFija = 0.0;
        if("Diario".equals(periodicidad)){
            if(ingreso >= 0.01 && ingreso <= 208.29 ){
                cuotaFija = 0.47;
            }else if(ingreso >= 208.3 && ingreso <= 366.05){
                cuotaFija = 12.23;
            }else{
                cuotaFija = 29.4;
            }
        }
        if("Semanal".equals(periodicidad)){
            if(ingreso >= 0.01 && ingreso <= 1468.03 ){
                cuotaFija = 3.29;
            }else if(ingreso >= 1458.04 && ingreso <= 2562.35){
                cuotaFija = 85.61;
            }else{
                cuotaFija = 205.8;
            }
        }
        if("Decenal".equals(periodicidad)){
            if(ingreso >= 0.01 && ingreso <= 2082.9 ){
                cuotaFija = 4.1;
            }else if(ingreso >= 2082.91 && ingreso <= 3660.5){
                cuotaFija = 122.3;
            }else{
                cuotaFija = 294.0;
            }
        }
        if("Quincenal".equals(periodicidad)){
            if(ingreso >= 0.01 && ingreso <= 3124.35 ){
                cuotaFija = 7.05;
            }else if(ingreso >= 3124.36 && ingreso <= 6490.75){
                cuotaFija = 183.45;
            }else{
                cuotaFija = 441.0;
            }
        }
        if("Mensual".equals(periodicidad)){
            if(ingreso >= 0.01 && ingreso <= 6332.05 ){
                cuotaFija = 14.32;
            }else if(ingreso >= 6332.06 && ingreso <= 11128.01){
                cuotaFija = 371.83;
            }else{
                cuotaFija = 893.63;
            }
        }
        return cuotaFija;
    }

    private double calcularImpuestoRetener(double ingreso, String periodicidad) {
        //Solo sumamos el impuesto marginal y la cuota fija
        double impuestoMarginal = calcularImpuestoMarginal(ingreso, periodicidad);
        double cuotaFija = calcularCuotaFija(ingreso, periodicidad);
        return impuestoMarginal + cuotaFija;
    }

    private double calcularPercepcionMenosImpuestos(double ingreso, double impuestoRetener) {
        // ya el calculo final
        return ingreso - impuestoRetener;
    }


}