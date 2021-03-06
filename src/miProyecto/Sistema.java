package miProyecto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sistema {

    Scanner teclado = new Scanner(System.in);
    private Usuario usuarioActivo;

    public Sistema() {

        ImprimirNombre();
    }

    public void ImprimirNombre() {
        System.out.println("BANCO NACIONAL BANANA REPUBLIC");
        login();

    }

    public void login() {
        int eleccion;
        System.out.println("Ingresa 1 si ya cuentas con usuario, 2 para crear tu usuario");
        eleccion = teclado.nextInt();
        if (eleccion == 1) {
            int i = 0;
            do {
                Usuario usuarioLogin;
                usuarioLogin = datosLogin();
                if (usuarioLogin != null) {
                    System.out.println("Bienvenido " + usuarioLogin.getNombre());
                    opcionesCuenta(usuarioLogin);
                    break;
                } else {
                    System.out.println("Error en login, revisa tus datos");
                }
                i++;
            } while (i < 3);
        }
        if (eleccion == 2) {
            crearUsuario();
            login();
        } else {
            ImprimirNombre();
        }
    }

    /*public void validar(Usuario usuarioLogin) {
        int i;
        if (usuarioLogin != null) {
            System.out.println("Bienvenido " + usuarioLogin.getNombre());
            opcionesCuenta(usuarioLogin);
        } else {
            do {
                login();
                i = 0;
                i++;
            } while (i < 3);
        }/*public void validar(Usuario usuarioLogin) {
        int i;
        if (usuarioLogin != null) {
            System.out.println("Bienvenido " + usuarioLogin.getNombre());
            opcionesCuenta(usuarioLogin);
        } else {
            do {
                login();
                i = 0;
                i++;
            } while (i < 3);
        }
    }*/ //Se convierte en un bucle infinito
    public boolean buscarUser(String idUser, String contraseñaUser) {
        Cifrador cifrado = new Cifrador();
        String contraseñaCifrada = cifrado.cifrar(contraseñaUser);
        File archivo;  //manipular un archivo
        FileReader leer; //lector
        String cadena, idUsuario = "", contraseña = "", usuario = "", idCuenta = "", fondos = "";
        BufferedReader almacenamiento;
        archivo = new File("cajeroUsuario.txt");
        try {
            leer = new FileReader(archivo);
            almacenamiento = new BufferedReader(leer);
            cadena = "";
            usuarioActivo = null;
            do {
                try {
                    cadena = almacenamiento.readLine();
                    idUsuario = cadena;
                    cadena = almacenamiento.readLine();
                    usuario = cadena;
                    cadena = almacenamiento.readLine();
                    contraseña = cadena;
                    cadena = almacenamiento.readLine();
                    idCuenta = cadena;
                    cadena = almacenamiento.readLine();
                    fondos = cadena;
                    if (cadena != null && contraseña.equals(contraseñaCifrada) && idUser.equals(idUsuario)) {
                        Usuario userBusqueda = new Usuario(usuario, contraseña, idUsuario);
                        float fondosCuenta = Float.valueOf(fondos);
                        userBusqueda.agregarCuenta(new Cuenta(idCuenta, fondosCuenta));
                        usuarioActivo = userBusqueda;
                        leer.close();
                        return true;
                    } else {
                        System.out.println("Contraseña no encontrada" + "Usuario: " + usuario + "\nid: " + idUsuario + " \ncontraseña: " + contraseña);
                    }
                } catch (IOException ex) {
                    System.out.println("error encontrar" + ex);

                    Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
                }
            } while (cadena != null || (idUser.equals(usuario) && contraseña.equals(contraseñaCifrada)));
            try {
                almacenamiento.close();
                leer.close();
            } catch (IOException ex) {
                Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*if (usuario.equals(idUser) && contraseña.equals(contraseña)) {
                Usuario user = new Usuario(usuario, contraseña, idUsuario);
                float fondosCuenta = Float.valueOf(fondos);
                user.agregarCuenta(new Cuenta(idCuenta, fondosCuenta));
                Bdatos.add(user);
                System.out.println("Buscar user sí sirve" + user);
                return user;
            }*/
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Usuario no existe");
        return false;
    }

    public Usuario datosLogin() {
        String contraseñaUser, idUser;
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingresa tu id");
        idUser = sc.nextLine();
        System.out.println("Ingresa tu contraseña");
        contraseñaUser = sc.nextLine();
        if (buscarUser(idUser, contraseñaUser)) {
            return usuarioActivo;
        }
        return null;
    }

    public int opcionesCuenta(Usuario usuarioLogin) {
        int operacionUsuario;
        System.out.println("Operaciones disponibles");
        System.out.println("1 retiro, 2 deposito, 3 consulta de saldo, 4 pago de servicios, 5 compra aire, 6 cambiar contraseña, 7 salir ");
        operacionUsuario = teclado.nextInt();
        InterfazUsuario interfaz = new InterfazUsuario(usuarioLogin, usuarioLogin.getCuenta());
        switch (operacionUsuario) {
            case 1:

                if (interfaz.retiro()) {
                    actualizarUsuario(usuarioLogin);
                }
                opcionesCuenta(usuarioLogin);
                break;
            case 2:
                if (interfaz.abono() >= 50) {
                    actualizarUsuario(usuarioLogin);
                }
                opcionesCuenta(usuarioLogin);
                break;
            case 3:
                interfaz.consulta();
                opcionesCuenta(usuarioLogin);
                break;
            case 4:
                if (interfaz.pagoServicios()) {
                    actualizarUsuario(usuarioLogin);
                }
                opcionesCuenta(usuarioLogin);
                break;
            case 5:

                if (interfaz.tiempoAire()) {
                    actualizarUsuario(usuarioLogin);
                }
                opcionesCuenta(usuarioLogin);
                break;
            case 6:
                if (cambiarContraseña(usuarioLogin)) {
                    System.out.println("Operacion exitosa");
                }
                opcionesCuenta(usuarioLogin);
                break;
            case 7:
                System.out.println("Adios");
                break;
        }
        return operacionUsuario;
    }

    public Usuario actualizarUsuario(Usuario user) {
        File archivoTemporal;
        archivoTemporal = new File("temp.txt");
        File archivoLectura;
        archivoLectura = new File("cajeroUsuario.txt");
        try {
            BufferedWriter escribir = new BufferedWriter(new FileWriter(archivoTemporal));
            BufferedReader lectura = new BufferedReader(new FileReader(archivoLectura));
            String cadena;
            while ((cadena = lectura.readLine()) != null) { //comparamos cadena, que alberga lectura de linea, con null
                String borrarEspacios = cadena.trim();
                if (borrarEspacios.equals(user.getId())) {
                    escribir.write(user.getId() + System.getProperty("line.separator"));
                    escribir.write(user.getNombre() + System.getProperty("line.separator"));

                    escribir.write(user.getContraseña() + System.getProperty("line.separator"));
                    escribir.write(user.getCuenta().getId() + System.getProperty("line.separator"));
                    escribir.write(user.getCuenta().consultar() + System.getProperty("line.separator"));
                    for (int i = 0; i < 4; i++) {
                        cadena = lectura.readLine();
                    }
                    continue; //sale de la iteracion. No ejecuta nada continuo
                }
                escribir.write(cadena + System.getProperty("line.separator"));
            }

            lectura.close();
            escribir.close();

            System.out.println("Buffer cerrado");

            if (archivoLectura.exists()) {
                //Boolean resultados = archivoTemporal.renameTo(new File());
                System.out.println("El archivo si existe");

                Files.move(Paths.get(archivoTemporal.getAbsolutePath()), Paths.get(archivoLectura.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
                //Files.move(archivoTemporal.toPath(), archivoLectura.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("ok");
            } else {
                System.out.println("No archivo lectura");
            }

        } catch (IOException x) {
            System.out.println("Error: " + x);
        }

        return null;
    }

    private boolean cambiarContraseña(Usuario usuario) {
        String contraseña1, contraseña2, contraseñaNueva1, contraseñaNueva2;
        Scanner teclado = new Scanner(System.in);
        System.out.println("Ingresa tu contraseña");
        contraseña1 = teclado.nextLine();
        System.out.println("Confirma tu contreseña");
        contraseña2 = teclado.nextLine();
        if (buscarUser(usuario.getId(), contraseña1)) {
            System.out.println("Ingresa tu nueva contraseña (minimo 4 digitos)");
            contraseñaNueva1 = teclado.nextLine();
            System.out.println("Confirma tu nueva contraseña");
            contraseñaNueva2 = teclado.nextLine();
            if (contraseñaNueva1.equals(contraseñaNueva2) && contraseñaNueva1.length() >= 4) {
                usuario.restableceContraseña(contraseñaNueva1);
                actualizarUsuario(usuario);
                return true;
            }
        } else {
            System.out.println("Error, revisa tus datos");

        }
        return false;
    }

    public void crearUsuario() {
        Scanner sc = new Scanner(System.in);
        String nombre;
        Random rnd = new Random();
        int idAleatorio = (int) (rnd.nextDouble() * 1000 + 100);
        System.out.println("Ingresa tu nombre");
        nombre = sc.nextLine();
        if ((!nombre.isEmpty()) && nombre.length() >= 4) {

            String contraseña1;
            String contraseña2;
            System.out.println("Ingresa nueva contraseña");
            contraseña1 = sc.nextLine();
            System.out.println("Confirma tu contraseña");
            contraseña2 = sc.nextLine();

            System.out.println("Deposita a tu nueva cuenta. Depósito mínimo de 50.00 MXN");
            float montoApertura = sc.nextInt();
            System.out.println(montoApertura);
            if (contraseña1.equals(contraseña2) && contraseña1.length() >= 4 && montoApertura >= 50) {
                System.out.println("Proceso completado");
                Cifrador cifrado = new Cifrador();
                String contraseñaCifrada = cifrado.cifrar(contraseña2);
                Usuario usuario = new Usuario(nombre, String.valueOf(idAleatorio));
                usuario.agregarCuenta(new Cuenta("333", montoApertura));
                usuario.restableceContraseña(contraseñaCifrada);
                BaseDatos(usuario);
                //System.out.println(usuario);
                usuarioActivo = usuario;
                System.out.println("Tu id es: " + usuario.getId());

            } else {
                System.out.println("Error revisa contraseña y deposito");
            }
        } else {
            System.out.println("Error al ingresar usuario, mínimo 4 caracteres");
            login();
        }

    }

    public void BaseDatos(Usuario usuario) {
        File archivo; //manipula el archivo
        FileWriter escribir; // escribir en el archivo
        PrintWriter linea; // 
        archivo = new File("cajeroUsuario.txt");
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                escribir = new FileWriter(archivo, true);
                linea = new PrintWriter(escribir);
                //escribir en el archivo
                linea.println(usuario.getId());
                linea.println(usuario.getNombre());
                linea.println(usuario.getContraseña());//Esta cifrada al acceder al método getContraseña
                linea.println(usuario.getCuenta().getId());
                linea.println(usuario.getCuenta().consultar());
                //System.out.println(usuario.getId());
                //System.out.println(contraseña2);
                //linea.println();
                linea.close();
                escribir.close();
            } catch (Exception e) {
            }
        } else {
            try {
                escribir = new FileWriter(archivo, true);
                linea = new PrintWriter(escribir);
                //escribir en el archivo
                linea.println(usuario.getId());
                linea.println(usuario.getNombre());
                linea.println(usuario.getContraseña());
                linea.println(usuario.getCuenta().getId());
                linea.println(usuario.getCuenta().consultar());
                //linea.println(email);
                linea.close();
                escribir.close();
            } catch (Exception e) {
            }
        }
    }

}
