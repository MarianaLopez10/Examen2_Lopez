package co.edu.poli.examen2_lopez.controlador;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import co.edu.poli.examen2_lopez.modelo.Casa;
import co.edu.poli.examen2_lopez.modelo.Apartamento;
import co.edu.poli.examen2_lopez.modelo.Inmueble;
import co.edu.poli.examen2_lopez.modelo.Propietario;
import co.edu.poli.examen2_lopez.servicios.DAOInmueble;
import co.edu.poli.examen2_lopez.servicios.DAOPropietario;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ControlFormProperty {
    
    // ================= CONSULTA =================
    @FXML
    private Button bttConsulta;
    @FXML
    private TextField txtInmueble1;
    @FXML
    private TextArea txtAreaResultado;

    // ================= CREAR =================
    @FXML
    private Button bttCreacion;
    @FXML
    private TextField txtInmueble2;
    @FXML
    private DatePicker datepk1;
    @FXML
    private ComboBox<Propietario> cmbPropietario;

    @FXML
    private RadioButton radioCasa;
    @FXML
    private RadioButton radioApartamento;
    @FXML
    private ToggleGroup tipoInmueble;

    @FXML
    private TextField txtCantidadPisos;
    @FXML
    private TextField txtNumeroPiso;

    // ================= DAO =================
    private DAOInmueble daoInmueble;
    private DAOPropietario daoPropietario;

    // ================= INIT =================
    @FXML
    private void initialize() {

        daoInmueble = new DAOInmueble();
        daoPropietario = new DAOPropietario();

        datepk1.setValue(LocalDate.now());

        try {
            List<Propietario> lista = daoPropietario.readall();
            cmbPropietario.getItems().setAll(lista);
        } catch (Exception e) {
            mostrarAlerta(e.getMessage());
        }

        // Validación básica números
        txtInmueble2.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validarSoloNumeros(txtInmueble2);
        });

        txtCantidadPisos.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validarSoloNumeros(txtCantidadPisos);
        });

        txtNumeroPiso.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validarSoloNumeros(txtNumeroPiso);
        });
    }

    // ================= CONSULTAR =================
    @FXML
    private void pressConsulta(ActionEvent event) {

        txtAreaResultado.setText("");

        if (txtInmueble1.getText().isBlank()) {
            mostrarAlerta("Ingrese número de inmueble");
            return;
        }

        try {
            Inmueble i = daoInmueble.readone(txtInmueble1.getText());

            if (i != null)
                txtAreaResultado.setText(i.toString());
            else
                mostrarAlerta("No existe el inmueble");

        } catch (Exception e) {
            mostrarAlerta(e.getMessage());
        }
    }

    // ================= CREAR =================
    @FXML
    private void pressCreacion(ActionEvent event) {

        String numero = txtInmueble2.getText().trim();

        if (numero.isEmpty()) {
            mostrarAlerta("Ingrese número del inmueble");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String fecha_compra = datepk1.getValue().format(formatter);
        if (fecha_compra == null) {
            mostrarAlerta("Seleccione fecha de compra");
            return;
        }

        Propietario propietario = cmbPropietario.getValue();

        if (propietario == null) {
            mostrarAlerta("Seleccione un propietario");
            return;
        }

        Inmueble nuevo;

        try {

            // ================= CASA =================
            if (radioCasa.isSelected()) {

                if (txtCantidadPisos.getText().isBlank()) {
                    mostrarAlerta("Ingrese cantidad de pisos");
                    return;
                }

                nuevo = new Casa(
                        numero,
                        fecha_compra,
                        true,
                        propietario,
                        Integer.parseInt(txtCantidadPisos.getText())
                );

            }
            // ================= APARTAMENTO =================
            else {

                if (txtNumeroPiso.getText().isBlank()) {
                    mostrarAlerta("Ingrese número de piso");
                    return;
                }

                nuevo = new Apartamento(
                        numero,
                        fecha_compra,
                        true,
                        propietario,
                        Integer.parseInt(txtNumeroPiso.getText())
                );
            }

            String resultado = daoInmueble.create(nuevo);

            mostrarAlerta(resultado);

            if (resultado.startsWith("✔")) {
                limpiarFormCrear();
            }

        } catch (Exception e) {
            mostrarAlerta(e.getMessage());
        }
    }

    // ================= UTILIDADES =================
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Resultado");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarFormCrear() {
        txtInmueble2.clear();
        datepk1.setValue(LocalDate.now());
        cmbPropietario.setValue(null);
        radioCasa.setSelected(true);
        txtCantidadPisos.clear();
        txtNumeroPiso.clear();
    }

    private void validarSoloNumeros(TextField txt) {
        String texto = txt.getText().trim();

        if (!texto.isBlank()) {
            if (!texto.matches("\\d+")) {
                txt.setStyle("-fx-border-color: red;");
                mostrarAlerta("Solo números permitidos");
                txt.clear();
                Platform.runLater(txt::requestFocus);
            } else {
                txt.setStyle("");
            }
        }
    }
}
