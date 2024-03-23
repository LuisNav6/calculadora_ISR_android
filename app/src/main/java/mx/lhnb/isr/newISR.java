package mx.lhnb.isr;
//importamos las librerías necesarias
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ToggleButton;
import android.graphics.Color;
import android.widget.CompoundButton;
public class newISR extends AppCompatActivity {
    //variables a las cuales les asignaremos más tarde un componente
    private EditText ingresoEditText;
    private RadioGroup periodoRadioGroup;
    private ImageButton calcularISRButton;
    private ListView listViewResultados;
    private ArrayAdapter<String> listViewAdapter;
    private ToggleButton colorToggleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity); //le decimos a cual layout hacer referencia
        //asignamos componente a variable
        colorToggleButton = findViewById(R.id.colorToggleButton);
        ingresoEditText = findViewById(R.id.ingreso);
        periodoRadioGroup = findViewById(R.id.periodoRadioGroup);
        calcularISRButton = findViewById(R.id.calcularISRButton);
        listViewResultados = findViewById(R.id.listViewResultados);
        listViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listViewResultados.setAdapter(listViewAdapter);
//llamada a la función que hace el cálculo del isr
        calcularISRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularISR();
            }
        });
//Declaración y listener del evento de redirección
        Button redireccionarButton = findViewById(R.id.redireccionar);
        redireccionarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la MainActivity
                Intent intent = new Intent(newISR.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //función que sirve para el cambio del background
        colorToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int backgroundColor = isChecked ? Color.parseColor("#C0D2FF") : Color.WHITE;

                // Cambiar el color de fondo del layout principal
                View layout = findViewById(R.id.layout_activity);
                layout.setBackgroundColor(backgroundColor);

                // Cambiar el color de fondo y texto del EditText
                ingresoEditText.setBackgroundColor(backgroundColor);

                // Cambiar el color de fondo y texto de los elementos de la RadioGroup
                for (int i = 0; i < periodoRadioGroup.getChildCount(); i++) {
                    View view = periodoRadioGroup.getChildAt(i);
                    if (view instanceof RadioButton) {
                        RadioButton radioButton = (RadioButton) view;
                        radioButton.setBackgroundColor(backgroundColor);
                    }
                }
                listViewResultados.setBackgroundColor(backgroundColor);
            }
        });
    }
//función madre del cálculo de ISR
    private void calcularISR() {
        double ingreso;
        try {
            ingreso = Double.parseDouble(ingresoEditText.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese un valor válido para el ingreso", Toast.LENGTH_SHORT).show();
            return;
        }
//definimos variables, las cuales tomarán el return de una función en especifico que nos sirve para el calculo del ISR y la muestra de datos
        String periodicidad = obtenerPeriodicidadSeleccionada();

        double limiteInferior = calcularLimiteInferior(ingreso, periodicidad);
        double diferencia = calcularDiferencia(ingreso, periodicidad);
        double tasa = calcularTasa(ingreso, periodicidad);
        double impuestoMarginal = calcularImpuestoMarginal(ingreso, periodicidad);
        double cuotaFija = calcularCuotaFija(ingreso, periodicidad);
        double impuestoRetener = calcularImpuestoRetener(ingreso, periodicidad);
        double percepcionMenosImpuestos = calcularPercepcionMenosImpuestos(ingreso, impuestoRetener);
//llenamos la lista
        listViewAdapter.clear(); // Limpiamos lo que haya antes de esto
        listViewAdapter.add("Ingreso: $" + ingreso);
        listViewAdapter.add("Ingreso calculado: $" + ingreso);
        listViewAdapter.add("Límite inferior: $" + limiteInferior);
        listViewAdapter.add("Diferencia: $" + diferencia);
        listViewAdapter.add("Tasa: " + tasa + "%");
        listViewAdapter.add("Impuesto marginal: $" + impuestoMarginal);
        listViewAdapter.add("Cuota fija: $" + cuotaFija);
        listViewAdapter.add("Impuesto a retener: $" + impuestoRetener);
        listViewAdapter.add("Percepción menos impuestos: $" + percepcionMenosImpuestos);
        listViewAdapter.add("Periodicidad: " + periodicidad);

        // mostramos la lista
        listViewResultados.setVisibility(View.VISIBLE);
    }
//por fines de practicidad definidos y le retornamos la opción que eligió el usuario
    private String obtenerPeriodicidadSeleccionada() {
        int selectedId = periodoRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        return selectedRadioButton.getText().toString();
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
        //la tasa ya viene definida solo se declarán 3 por periodo con fines de practicidad
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
        //la cuota fija ya viene definida solo se declarán 3 por periodo con fines de practicidad
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


